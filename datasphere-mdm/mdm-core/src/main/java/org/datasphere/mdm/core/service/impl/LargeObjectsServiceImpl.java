/*
 * Apache License
 * 
 * Copyright (c) 2021 HuahuiData
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.huahui.datasphere.mdm.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.util.IdUtils;

import com.huahui.datasphere.mdm.core.context.DeleteLargeObjectContext;
import com.huahui.datasphere.mdm.core.context.FetchLargeObjectContext;
import com.huahui.datasphere.mdm.core.context.UpsertLargeObjectContext;
import com.huahui.datasphere.mdm.core.dao.LargeObjectsDAO;
import com.huahui.datasphere.mdm.core.dto.LargeObjectResult;
import com.huahui.datasphere.mdm.core.exception.CoreExceptionIds;
import com.huahui.datasphere.mdm.core.po.lob.BinaryLargeObjectPO;
import com.huahui.datasphere.mdm.core.po.lob.CharacterLargeObjectPO;
import com.huahui.datasphere.mdm.core.po.lob.LargeObjectPO;
import com.huahui.datasphere.mdm.core.service.LargeObjectsService;
import com.huahui.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import com.huahui.datasphere.mdm.core.util.AutodeleteTempFileInputStream;
import com.huahui.datasphere.mdm.core.util.FileUtils;
import com.huahui.datasphere.mdm.core.util.SecurityUtils;


/**
 * @author theseusyang
 * LOB service.
 */
@Service
public class LargeObjectsServiceImpl implements LargeObjectsService {
    /**
     * This logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LargeObjectsServiceImpl.class);
    /**
     * LOB DAO.
     */
    @Autowired
    private LargeObjectsDAO largeObjectsDao;
    /**
     * {@inheritDoc}
     */
    @Override
    public LargeObjectResult fetchLargeObject(FetchLargeObjectContext ctx) {

        LargeObjectPO po = largeObjectsDao.loadLargeObject(ctx.getLargeObjectId(), ctx.isBinary());
        if (Objects.isNull(po)) {
            return throwLoadFailure(ctx.isBinary(), null, ctx.getLargeObjectId());
        }

        try {

            InputStream data = null;
            if (ctx.isDirect()) {
                data = po.getData();
            } else {

                File temp = File.createTempFile("unidata-lob-fetch-", ".out");
                try (FileOutputStream fis = new FileOutputStream(temp)) {

                    int count;
                    byte[] buf = new byte[FileUtils.DEFAULT_BUFFER_SIZE];
                    while ((count = po.getData().read(buf, 0, buf.length)) != -1) {
                        fis.write(buf, 0, count);
                    }
                }

                data = new AutodeleteTempFileInputStream(temp);
            }

            LargeObjectResult result = new LargeObjectResult();

            result.setId(po.getId() == null ? null : po.getId().toString());
            result.setInputStream(data);
            result.setFileName(po.getFileName());
            result.setMimeType(po.getMimeType());
            result.setSize(po.getSize());
            result.setAcceptance(po.getState());

            return result;

        } catch (IOException e) {
            return throwLoadFailure(ctx.isBinary(), e, ctx.getLargeObjectId());
        }
    }
    /**
     * {@inheritDoc}
     */
    private<T> T throwLoadFailure(boolean binary, Throwable cause, UUID objectId) {

        final String message = "Unable to load {} LOB data. Object id [{}].";
        if (cause != null) {
            LOGGER.warn(message, binary ? "binary" : "character", objectId, cause);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_CANNOT_LOAD_LOB, cause);
        } else {
            LOGGER.warn(message, binary ? "binary" : "character", objectId);
            throw new PlatformFailureException(message, CoreExceptionIds.EX_DATA_CANNOT_LOAD_LOB);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LargeObjectResult saveLargeObject(UpsertLargeObjectContext ctx) {

        try {

            String user = SecurityUtils.getCurrentUserName();
            Date now = new Date();
            LargeObjectPO po = ctx.isBinary() ? new BinaryLargeObjectPO() : new CharacterLargeObjectPO();

            if (ctx.getLargeObjectId() == null) {
                po.setId(IdUtils.v1());
            } else {
                po.setId(ctx.getLargeObjectId());
            }

            po.setFileName(ctx.getFilename());
            po.setMimeType(ctx.getMimeType());
            po.setCreateDate(now);
            po.setCreatedBy(user);
            po.setSubject(ctx.getSubjectId());
            po.setState(ctx.getAcceptance() == null ? LargeObjectAcceptance.ACCEPTED : ctx.getAcceptance());
            po.setTags(CollectionUtils.isEmpty(ctx.getTags()) ? null : ctx.getTags().toArray(String[]::new));

            if (ctx.getInput() != null) {

                InputStream is = ctx.getInput().get();

                po.setData(is);
                po.setSize(is.available());
            }

            largeObjectsDao.upsertLargeObject(po);

            LargeObjectResult result = new LargeObjectResult();

            result.setId(po.getId().toString());
            result.setInputStream(null);
            result.setFileName(po.getFileName());
            result.setMimeType(po.getMimeType());
            result.setSize(po.getSize());
            result.setAcceptance(po.getState());

            return result;

        } catch (IOException e) {
            throw new PlatformFailureException("Unable to save LOB data.", CoreExceptionIds.EX_DATA_CANNOT_SAVE_LOB, e);
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteLargeObject(DeleteLargeObjectContext ctx) {
        return largeObjectsDao.wipeLargeObject(ctx.getLargeObjectId(), ctx.isBinary());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkExistLargeObject(FetchLargeObjectContext ctx) {
        return largeObjectsDao.checkLargeObject(ctx.getLargeObjectId(), ctx.isBinary());
    }
}
