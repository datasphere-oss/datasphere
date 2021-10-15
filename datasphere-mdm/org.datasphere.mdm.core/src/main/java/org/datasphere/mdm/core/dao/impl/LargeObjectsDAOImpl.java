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

package org.datasphere.mdm.core.dao.impl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.datasphere.mdm.core.dao.LargeObjectsDAO;
import org.datasphere.mdm.core.po.AbstractObjectPO;
import org.datasphere.mdm.core.po.lob.BinaryLargeObjectPO;
import org.datasphere.mdm.core.po.lob.CharacterLargeObjectPO;
import org.datasphere.mdm.core.po.lob.LargeObjectPO;
import org.datasphere.mdm.core.type.lob.LargeObjectAcceptance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.stereotype.Repository;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;

/**
 * @author Mikhail Mikhailov
 * DAO for large objects implementation.
 */
@Repository
public class LargeObjectsDAOImpl extends BaseDAOImpl implements LargeObjectsDAO {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LargeObjectsDAOImpl.class);
    /**
     * BLOB mapper.
     */
    private static final RowMapper<BinaryLargeObjectPO> DEFAULT_BINARY_ROW_MAPPER = (rs, pos) -> {

        BinaryLargeObjectPO po = new BinaryLargeObjectPO();

        UUID val = rs.getObject(LargeObjectPO.FIELD_ID, UUID.class);
        po.setId(val);

        po.setSubject(rs.getString(LargeObjectPO.FIELD_SUBJECT));
        po.setCreateDate(rs.getDate(AbstractObjectPO.FIELD_CREATE_DATE));
        po.setCreatedBy(rs.getString(AbstractObjectPO.FIELD_CREATED_BY));
        po.setUpdateDate(rs.getDate(AbstractObjectPO.FIELD_UPDATE_DATE));
        po.setUpdatedBy(rs.getString(AbstractObjectPO.FIELD_UPDATED_BY));

        Array tags = rs.getArray(LargeObjectPO.FIELD_TAGS);
        po.setTags(rs.wasNull() ? null : (String[]) tags.getArray());

        po.setFileName(rs.getString(LargeObjectPO.FIELD_FILE_NAME));
        po.setMimeType(rs.getString(LargeObjectPO.FIELD_MIME_TYPE));
        po.setData(rs.getBinaryStream(LargeObjectPO.FIELD_DATA));
        po.setSize(rs.getLong(LargeObjectPO.FIELD_SIZE));
        po.setState(LargeObjectAcceptance.valueOf(rs.getString(LargeObjectPO.FIELD_STATE)));

        return po;
    };
    /**
     * CLOB mapper.
     */
    private static final RowMapper<CharacterLargeObjectPO> DEFAULT_CHARACTER_ROW_MAPPER = (rs, pos) -> {

        CharacterLargeObjectPO po = new CharacterLargeObjectPO();

        UUID val = rs.getObject(LargeObjectPO.FIELD_ID, UUID.class);
        po.setId(val);

        po.setSubject(rs.getString(LargeObjectPO.FIELD_SUBJECT));
        po.setCreateDate(rs.getDate(AbstractObjectPO.FIELD_CREATE_DATE));
        po.setCreatedBy(rs.getString(AbstractObjectPO.FIELD_CREATED_BY));
        po.setUpdateDate(rs.getDate(AbstractObjectPO.FIELD_UPDATE_DATE));
        po.setUpdatedBy(rs.getString(AbstractObjectPO.FIELD_UPDATED_BY));

        Array tags = rs.getArray(LargeObjectPO.FIELD_TAGS);
        po.setTags(rs.wasNull() ? null : (String[]) tags.getArray());

        po.setFileName(rs.getString(LargeObjectPO.FIELD_FILE_NAME));
        po.setMimeType(rs.getString(LargeObjectPO.FIELD_MIME_TYPE));
        po.setData(rs.getBinaryStream(LargeObjectPO.FIELD_DATA));
        po.setSize(rs.getLong(LargeObjectPO.FIELD_SIZE));
        po.setState(LargeObjectAcceptance.valueOf(rs.getString(LargeObjectPO.FIELD_STATE)));

        return po;
    };
    /**
     * Extracts first result or returns null.
     */
    private static final ResultSetExtractor<BinaryLargeObjectPO> BLOB_SINGLE_RESULT_EXTRACTOR
        = rs -> rs.next() ? DEFAULT_BINARY_ROW_MAPPER.mapRow(rs, rs.getRow()) : null;
    /**
     * Extracts first result or returns null.
     */
    private static final ResultSetExtractor<CharacterLargeObjectPO> CLOB_SINGLE_RESULT_EXTRACTOR
        = rs -> rs.next() ? DEFAULT_CHARACTER_ROW_MAPPER.mapRow(rs, rs.getRow()) : null;
    /**
     * Drafts filter SQL types.
     */
    private static final int[] WIPE_FILTER_TYPES = {
        // Subject
        Types.VARCHAR,
        Types.VARCHAR,
        // Tags
        Types.ARRAY,
        Types.ARRAY,
    };
    /**
     * Fetch data.
     */
    private final String fetchBinaryLargeObjectByIdSQL;
    /**
     * Fetch data.
     */
    private final String fetchCharacterLargeObjectByIdSQL;
    /**
     * Inserts/Updates binary data.
     */
    private final String upsertBinaryLargeObjectSQL;
    /**
     * Inserts/Updates character data.
     */
    private final String upsertCharacterLargeObjectSQL;
    /**
     * Checks BLOB existance.
     */
    private final String checkBinaryLargeObjectByIdSQL;
    /**
     * Checks CLOB existance.
     */
    private final String checkCharacterLargeObjectByIdSQL;
    /**
     * Deletes blob data.
     */
    private final String wipeBinaryLargeObjectSQL;
    /**
     * Deletes char data.
     */
    private final String wipeCharacterLargeObjectSQL;
    /**
     * Delete several objects.
     */
    private final String wipeBinaryLargeObjectsSQL;
    /**
     * Delete several objects.
     */
    private final String wipeCharacterLargeObjectsSQL;
    /**
     * Activates BLOB.
     */
    private final String submitBinaryLargeObjectSQL;
    /**
     * Activates CLOB.
     */
    private final String submitCharacterLargeObjectSQL;
    /**
     * Delete unused cdata values
     */
    private final String deleteUnusedCharacterDataSQL;
    /**
     * Delete unused bdata values
     */
    private final String deleteUnusedBinaryDataSQL;
    /**
     * Delete CDATA by subjects.
     */
    private final String deleteCharacterDataBySubjectIdsSQL;
    /**
     * Delete BDATA by subjects.
     */
    private final String deleteBinaryDataBySubjectIdsSQL;
    /**
     * LOB handler.
     */
    private LobHandler lobHandler = new DefaultLobHandler();
    /**
     * External utility support.
     */
    @Autowired
    public LargeObjectsDAOImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("binary-data-sql") final Properties sql) {

        super(dataSource);
        fetchBinaryLargeObjectByIdSQL = sql.getProperty("fetchBinaryLargeObjectByIdSQL");
        fetchCharacterLargeObjectByIdSQL = sql.getProperty("fetchCharacterLargeObjectByIdSQL");
        upsertBinaryLargeObjectSQL = sql.getProperty("upsertBinaryLargeObjectSQL");
        upsertCharacterLargeObjectSQL = sql.getProperty("upsertCharacterLargeObjectSQL");
        checkBinaryLargeObjectByIdSQL = sql.getProperty("checkBinaryLargeObjectByIdSQL");
        checkCharacterLargeObjectByIdSQL = sql.getProperty("checkCharacterLargeObjectByIdSQL");
        wipeBinaryLargeObjectSQL = sql.getProperty("wipeBinaryLargeObjectSQL");
        wipeCharacterLargeObjectSQL = sql.getProperty("wipeCharacterLargeObjectSQL");
        wipeBinaryLargeObjectsSQL = sql.getProperty("wipeBinaryLargeObjectsSQL");
        wipeCharacterLargeObjectsSQL = sql.getProperty("wipeCharacterLargeObjectsSQL");
        submitBinaryLargeObjectSQL = sql.getProperty("submitBinaryLargeObjectSQL");
        submitCharacterLargeObjectSQL = sql.getProperty("submitCharacterLargeObjectSQL");
        deleteUnusedBinaryDataSQL = sql.getProperty("deleteUnusedBinaryDataSQL");
        deleteUnusedCharacterDataSQL = sql.getProperty("deleteUnusedCharacterDataSQL");
        deleteCharacterDataBySubjectIdsSQL = sql.getProperty("deleteCharacterDataBySubjectIdsSQL");
        deleteBinaryDataBySubjectIdsSQL = sql.getProperty("deleteBinaryDataBySubjectIdsSQL");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LargeObjectPO loadLargeObject(UUID id, boolean isBinary) {
        if (isBinary) {
            return getJdbcTemplate().query(fetchBinaryLargeObjectByIdSQL, BLOB_SINGLE_RESULT_EXTRACTOR, id);
        } else {
            return getJdbcTemplate().query(fetchCharacterLargeObjectByIdSQL, CLOB_SINGLE_RESULT_EXTRACTOR, id);
        }
    }
    /**
     * {@inheritDoc}
     * @throws IOException
     */
    @Override
    public boolean upsertLargeObject(LargeObjectPO lob) throws IOException {

        int updatesCount = getJdbcTemplate().execute(
                lob.isBinary() ? upsertBinaryLargeObjectSQL : upsertCharacterLargeObjectSQL,
                new LobCreatingPreparedStatementCallback(lob, lobHandler));

        return updatesCount == 1;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkLargeObject(UUID id, boolean isBinary) {
        return getJdbcTemplate().queryForObject(isBinary ? checkBinaryLargeObjectByIdSQL : checkCharacterLargeObjectByIdSQL, Boolean.class, id);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wipeLargeObject(UUID id, boolean isBinary) {
        return getJdbcTemplate().update(isBinary ? wipeBinaryLargeObjectSQL : wipeCharacterLargeObjectSQL, id) == 1;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean wipeLargeObjects(String subject, String[] tags, boolean isBinary) {

        final Object[] filter = new Object[] {
            subject, subject,
            tags, tags
        };

        return getJdbcTemplate().update(isBinary ? wipeBinaryLargeObjectsSQL : wipeCharacterLargeObjectsSQL, filter, WIPE_FILTER_TYPES) > 1;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean submitLargeObjects(Collection<LargeObjectPO> spec) {

        if (CollectionUtils.isEmpty(spec)) {
            return false;
        }

        Map<Boolean, List<LargeObjectPO>> grouped = spec.stream().collect(Collectors.groupingBy(LargeObjectPO::isBinary));
        for (Entry<Boolean, List<LargeObjectPO>> entry : grouped.entrySet()) {

            String sql = entry.getKey().booleanValue() ? submitBinaryLargeObjectSQL : submitCharacterLargeObjectSQL;
            jdbcTemplate.batchUpdate(sql, entry.getValue(), entry.getValue().size(), (ps, obj) -> {
                ps.setObject(1, obj.getSubject());
                ps.setString(2, LargeObjectAcceptance.ACCEPTED.name());
                ps.setObject(3, obj.getId());
                ps.setString(4, LargeObjectAcceptance.PENDING.name());
            });
        }

        return true;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public long cleanUnusedBinaryData(long maxLifetime) {

        Timestamp dateForDelete = new Timestamp(Instant.now().minus(maxLifetime, ChronoUnit.MINUTES).toEpochMilli());

        int bd = jdbcTemplate.update(deleteUnusedBinaryDataSQL, LargeObjectAcceptance.PENDING.name(), dateForDelete);
        int cd = jdbcTemplate.update(deleteUnusedCharacterDataSQL, LargeObjectAcceptance.PENDING.name(), dateForDelete);

        return (bd + cd);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public long cleanForSubjectIds(List<String> subjects) {

        if (CollectionUtils.isNotEmpty(subjects)) {
            try (Connection c = getBareConnection()) {

                Array oia = c.createArrayOf("text", subjects.toArray(String[]::new));

                int cd = getJdbcTemplate().update(deleteCharacterDataBySubjectIdsSQL, oia);
                int bd = getJdbcTemplate().update(deleteBinaryDataBySubjectIdsSQL, oia);

                return (cd + bd);
            } catch (SQLException e) {
                LOGGER.error("Failed to wipe CLOB/BLOB data.", e);
            }
        }

        return 0;
    }
    /**
     * @author Mikhail Mikhailov
     * LOB handler jdbc template support.
     */
    private class LobCreatingPreparedStatementCallback extends AbstractLobCreatingPreparedStatementCallback {
        /**
         * The PO to handle.
         */
        private final LargeObjectPO po;
        /**
         * Constructor.
         * @param po the PO to handle
         * @param lobHandler the handler for data
         */
        LobCreatingPreparedStatementCallback(LargeObjectPO po, LobHandler lobHandler) {
            super(lobHandler);
            this.po = po;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        protected void setValues(PreparedStatement ps, LobCreator lobCreator) throws SQLException {

            try {

                ps.setObject(1, po.getId());
                if (po.isBinary()) {
                    lobCreator.setBlobAsBinaryStream(ps, 2, po.getData(), po.getData().available());
                } else {
                    lobCreator.setClobAsCharacterStream(ps, 2, new InputStreamReader(po.getData()), po.getData().available());
                }

                ps.setString(3, po.getFileName());
                ps.setString(4, po.getMimeType());
                ps.setTimestamp(5, new Timestamp(po.getCreateDate().getTime()));
                ps.setString(6, po.getCreatedBy());
                ps.setLong(7, po.getSize());
                ps.setObject(8, po.getSubject());
                ps.setString(9, po.getState().name());

                Array tags = ps.getConnection().createArrayOf("text", po.getTags());
                ps.setArray(10, tags);

            } catch (IOException ioe) {
                throw new DataAccessResourceFailureException("I/O exception caught, while saving LOB.", ioe);
            }
        }
    }
}
