/*
 * Unidata Platform Community Edition
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 * This file is part of the Unidata Platform Community Edition software.
 *
 * Unidata Platform Community Edition is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Unidata Platform Community Edition is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.datasphere.mdm.core.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.context.DeleteLargeObjectContext;
import org.datasphere.mdm.core.context.FetchLargeObjectContext;
import org.datasphere.mdm.core.context.UpsertLargeObjectContext;
import org.datasphere.mdm.core.dao.LargeObjectsDAO;
import org.datasphere.mdm.core.dto.LargeObjectResult;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.po.lob.BinaryLargeObjectPO;
import org.datasphere.mdm.core.po.lob.CharacterLargeObjectPO;
import org.datasphere.mdm.core.po.lob.LargeObjectPO;
import org.datasphere.mdm.core.service.LargeObjectsService;
import org.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import org.datasphere.mdm.core.util.AutodeleteTempFileInputStream;
import org.datasphere.mdm.core.util.FileUtils;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.datasphere.mdm.system.exception.PlatformFailureException;
import org.datasphere.mdm.system.util.IdUtils;


/**
 * @author Mikhail Mikhailov
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
