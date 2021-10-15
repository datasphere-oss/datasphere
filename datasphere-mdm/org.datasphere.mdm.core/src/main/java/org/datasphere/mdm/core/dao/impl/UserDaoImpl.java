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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.datasphere.mdm.core.dao.DaoSqlQueryMapper;
import org.datasphere.mdm.core.dao.LargeObjectsDAO;
import org.datasphere.mdm.core.dao.RoleDao;
import org.datasphere.mdm.core.dao.SecurityLabelDao;
import org.datasphere.mdm.core.dao.SqlQuery;
import org.datasphere.mdm.core.dao.UserDao;
import org.datasphere.mdm.core.dao.rm.APIRowMapper;
import org.datasphere.mdm.core.dao.rm.PasswordRowMapper;
import org.datasphere.mdm.core.dao.rm.PasswordWithUserRowMapper;
import org.datasphere.mdm.core.dao.rm.TokenRowMapper;
import org.datasphere.mdm.core.dao.rm.UserEventRowMapper;
import org.datasphere.mdm.core.dao.rm.UserPropertyRowMapper;
import org.datasphere.mdm.core.dao.rm.UserPropertyValueRowMapper;
import org.datasphere.mdm.core.dao.rm.UserRowMapper;
import org.datasphere.mdm.core.dto.UserPropertyDTO;
import org.datasphere.mdm.core.exception.CoreExceptionIds;
import org.datasphere.mdm.core.po.security.ApiPO;
import org.datasphere.mdm.core.po.security.BaseUserPO;
import org.datasphere.mdm.core.po.security.PasswordPO;
import org.datasphere.mdm.core.po.security.RolePO;
import org.datasphere.mdm.core.po.security.TokenPO;
import org.datasphere.mdm.core.po.security.UserEventPO;
import org.datasphere.mdm.core.po.security.UserPO;
import org.datasphere.mdm.core.po.security.UserPropertyPO;
import org.datasphere.mdm.core.po.security.UserPropertyValuePO;
import org.datasphere.mdm.core.po.security.UserPropertyValuePO.FieldColumns;
import org.datasphere.mdm.core.type.security.SecurityLabel;
import org.datasphere.mdm.core.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.datasphere.mdm.system.dao.impl.BaseDAOImpl;
import org.datasphere.mdm.system.exception.PlatformFailureException;
import org.datasphere.mdm.system.type.runtime.MeasurementPoint;
import org.datasphere.mdm.system.util.IdUtils;

/**
 * The Class UserDAOImpl.
 */
@Repository
public class UserDaoImpl extends BaseDAOImpl implements UserDao {

    private static final String CONNECTION_TABLE = "s_user_s_label_attribute_value";

    /** The password row mapper. */
    private final PasswordRowMapper passwordRowMapper = new PasswordRowMapper();

    /**
     * Password with user info row mapper
     */
    private final PasswordWithUserRowMapper passwordWithUserRowMapper = new PasswordWithUserRowMapper();

    /** The token row mapper. */
    private final TokenRowMapper tokenRowMapper = new TokenRowMapper();

    /** The role property row mapper. */
    private final UserPropertyRowMapper userPropertyRowMapper = new UserPropertyRowMapper();

    /**
     * Loads events by login and possibly given date.
     */
    @SqlQuery("LOAD_USER_EVENTS_BY_LOGIN_AND_DATE_SQL")
    private static String LOAD_USER_EVENTS_BY_LOGIN_AND_DATE_SQL;
    /**
     * Loads events by login and possibly given date and limit and offset.
     */
    @SqlQuery("LOAD_USER_EVENTS_BY_LOGIN_LIMIT_OFFSET_AND_DATE_SQL")
    private static String LOAD_USER_EVENTS_BY_LOGIN_LIMIT_OFFSET_AND_DATE_SQL;
    /**
     * Counts event by login.
     */
    @SqlQuery("COUNT_EVENT_BY_LOGIN")
    private static String COUNT_EVENT_BY_LOGIN;
    /**
     * Deletes event by id.
     */
    @SqlQuery("DELETE_EVENT_BY_EVENT_ID_SQL")
    private static String DELETE_EVENT_BY_EVENT_ID_SQL;
    /**
     * Delete selected events.
     */
    @SqlQuery("DELETE_EVENTS_BY_EVENT_IDS_SQL")
    private static String DELETE_EVENTS_BY_EVENT_IDS_SQL;
    /**
     * Deletes event by login and 'to' date.
     */
    @SqlQuery("DELETE_EVENTS_BY_LOGIN_AND_DATE_SQL")
    private static String DELETE_EVENTS_BY_LOGIN_AND_DATE_SQL;
    /**
     * Put an event by login.
     */
    @SqlQuery("PUT_USER_EVENT_BY_LOGIN_SQL")
    private static String PUT_USER_EVENT_BY_LOGIN_SQL;
    /**
     * Put an event by user id.
     */
    @SqlQuery("PUT_USER_EVENT_SQL")
    private static String PUT_USER_EVENT_SQL;

    /** The load all user properties. */
    @SqlQuery("LOAD_ALL_USER_PROPERTIES")
    private static String LOAD_ALL_USER_PROPERTIES;

    /** The load user property by name. */
    @SqlQuery("LOAD_USER_PROPERTY_BY_NAME")
    private static String LOAD_USER_PROPERTY_BY_NAME;

    /** The load user property by display name. */
    @SqlQuery("LOAD_USER_PROPERTY_BY_DISPLAY_NAME")
    private static String LOAD_USER_PROPERTY_BY_DISPLAY_NAME;

    /** The insert user property. */
    @SqlQuery("INSERT_USER_PROPERTY")
    private static String INSERT_USER_PROPERTY;

    /** The update user property by id. */
    @SqlQuery("UPDATE_USER_PROPERTY_BY_ID")
    private static String UPDATE_USER_PROPERTY_BY_ID;

    /** The delete user property values by user property id. */
    @SqlQuery("DELETE_USER_PROPERTY_VALUES_BY_USER_PROPERTY_ID")
    private static String DELETE_USER_PROPERTY_VALUES_BY_USER_PROPERTY_ID;

    /** The delete user property by id. */
    @SqlQuery("DELETE_USER_PROPERTY_BY_ID")
    private static String DELETE_USER_PROPERTY_BY_ID;

    /** The insert user property value. */
    @SqlQuery("INSERT_USER_PROPERTY_VALUE")
    private static String INSERT_USER_PROPERTY_VALUE;

    /** The update user property value by id. */
    @SqlQuery("UPDATE_USER_PROPERTY_VALUE_BY_ID")
    private static String UPDATE_USER_PROPERTY_VALUE_BY_ID;

    /** The delete user property values by ids. */
    @SqlQuery("DELETE_USER_PROPERTY_VALUES_BY_IDS")
    private static String DELETE_USER_PROPERTY_VALUES_BY_IDS;

    /** The delete user property values by user id. */
    @SqlQuery("DELETE_USER_PROPERTY_VALUES_BY_USER_ID")
    private static String DELETE_USER_PROPERTY_VALUES_BY_USER_ID;
    /** The load user property values by user ids. */
    @SqlQuery("LOAD_USER_PROPERTY_VALUES_BY_USER_IDS")
    private static String LOAD_USER_PROPERTY_VALUES_BY_USER_IDS;
    /**
     * Checks the user exists (simple count).
     */
    @SqlQuery("CHECK_USER_EXISTS_SQL")
    private static String CHECK_USER_EXISTS_SQL;
    @SqlQuery("SELECT_USERS_WITH_PROPERTIES")
    private static String selectUsersWithProperties;
    @SqlQuery("SELECT_USERS_ENDPOINTS")
    private static String selectUsersEndpoints;
    @SqlQuery("SELECT_USERS_ROLES")
    private static String selectUsersRoles;
    @SqlQuery("DELETE_USERS_PASSWORDS")
    private static String deleteUsersPasswords;
    @SqlQuery("DELETE_USERS_PROPERTIES_VALUES")
    private static String deleteUsersPropertiesValues;
    @SqlQuery("DELETE_USERS")
    private static String deleteUsers;
    @SqlQuery("INSERT_USERS")
    private static String insertUsers;
    @SqlQuery("INSERT_USERS_ROLES")
    private static String insertUsersRoles;
    @SqlQuery("INSERT_USERS_PROPERTIES_VALUES")
    private static String insertPropertiesValues;
    @SqlQuery("INSERT_USERS_APIS")
    private static String insertUsersApis;
    @SqlQuery("FETCH_ALL_USERS_PASSWORDS")
    private static String fetchAllUsersPasswords;
    @SqlQuery("FETCH_LAST_USER_PASSWORDS")
    private static String fetchLastUserPasswords;
    @SqlQuery("INSERT_USERS_PASSWORDS")
    private static String insertUsersPasswords;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private LargeObjectsDAO largeObjectsDAO;

    private final SecurityLabelDao securityLabelDao;

    /**
     * Instantiates a new user dao.
     *
     * @param dataSource
     *            the data source
     * @param sql
     *            the sql
     */
    @Autowired
    public UserDaoImpl(
            @Qualifier("coreDataSource") final DataSource dataSource,
            @Qualifier("security-sql") final Properties sql
    ) {
        super(dataSource);

        DaoSqlQueryMapper.fill(UserDaoImpl.class, this, sql);

        securityLabelDao = new SecurityLabelDaoImpl(CONNECTION_TABLE, dataSource, sql);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.impl.UserDao#create(com.unidata.mdm.backend.
     * service.security.po.UserPO, java.util.List)
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional
    public UserPO create(final UserPO user, final List<SecurityLabel> userLabels) {
        Map<String, Object> paramMap = new HashMap<>();
        final String createdBy = user.getCreatedBy() != null ? user.getCreatedBy() : SecurityUtils.getCurrentUserName();
        paramMap.put(UserPO.Fields.CREATED_AT, new Timestamp(new Date().getTime()));
        paramMap.put(UserPO.Fields.CREATED_BY, createdBy);
        paramMap.put(UserPO.Fields.ACTIVE, user.isActive());
        paramMap.put(UserPO.Fields.ADMIN, user.isAdmin());
        paramMap.put(UserPO.Fields.EMAIL, user.getEmail());
        paramMap.put(UserPO.Fields.LOCALE, user.getLocale());
        paramMap.put(UserPO.Fields.FIRST_NAME, user.getFirstName());
        paramMap.put(UserPO.Fields.LAST_NAME, user.getLastName());
        paramMap.put(UserPO.Fields.LOGIN, user.getLogin());
        paramMap.put(UserPO.Fields.NOTES, user.getNotes());
        paramMap.put(UserPO.Fields.SOURCE, user.getSource());
        paramMap.put(UserPO.Fields.EXTERNAL, user.isExternal());
        paramMap.put(UserPO.Fields.EMAIL_NOTIFICATION, user.isEmailNotification());
        int rowsAffected = namedJdbcTemplate.update(UserPO.Queries.INSERT_NEW, paramMap);

        if (rowsAffected == 0) {
            throw new PlatformFailureException("No record inserted while creating user",
                    CoreExceptionIds.EX_SECURITY_CANNOT_CREATE_USER);
        }
        final UserPO userPO = findByLogin(user.getLogin());
        user.setId(userPO.getId());
        securityLabelDao.saveLabelsForObject(userPO.getId(), userLabels);

        final List<RolePO> roles = user.getRoles();
        final List<Map<String, Object>> params = new ArrayList<>();
        for (final RolePO role : roles) {
            final Map<String, Object> toAttach = new HashMap<>();
            toAttach.put(UserPO.Fields.S_USERS_ID, userPO.getId());
            toAttach.put(RolePO.Fields.NAME, role.getName());
            params.add(toAttach);
        }
        namedJdbcTemplate.batchUpdate(UserPO.Queries.ATTACH_TO_ROLE, params.toArray(new Map[params.size()]));
        paramMap.put(PasswordPO.Fields.S_USER_ID, user.getId());
        attachToAPI(user, paramMap);

        namedJdbcTemplate.update(PasswordPO.Queries.DEACTIVATE_BY_USER_ID, paramMap);

        if (!user.isExternal() && !CollectionUtils.isEmpty(user.getPassword())) {
            updatePassword(userPO.getId(), user.getPassword().get(0), false);
        }

        return user;
    }

    private void attachToAPI(final UserPO user, Map<String, Object> paramMap) {
        if (user.getApis() != null && user.getApis().size() != 0) {
            Set<String> apiNames = user.getApis().stream().map(ApiPO::getName).collect(Collectors.toSet());
            paramMap.put("api_names", apiNames);
            namedJdbcTemplate.update(UserPO.Queries.DETACH_FROM_API, paramMap);
            namedJdbcTemplate.update(UserPO.Queries.ATTACH_TO_API, paramMap);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.impl.UserDao#isExist(java.lang.String)
     */
    @Override
    @Transactional
    public boolean isExist(final String login) {
        MeasurementPoint.start();
        try {
            return jdbcTemplate.queryForObject(CHECK_USER_EXISTS_SQL, Long.class, login) > 0;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#findByEmail(java.lang.String,
     * java.lang.String)
     */
    @Override
    public UserPO findByEmail(String email, String source) {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(UserPO.Fields.EMAIL, email);
        paramMap.put(UserPO.Fields.SOURCE, source);
        final List<UserPO> result = namedJdbcTemplate.query(UserPO.Queries.SELECT_BY_EMAIL_SOURCE, paramMap,
                UserRowMapper.DEFAULT_USER_ROW_MAPPER);
        if (result == null || result.size() == 0) {
            return null;
        }
        final UserPO user = result.get(0);

        fillUser(user);

        return user;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.impl.UserDao#update(java.lang.String,
     * com.unidata.mdm.backend.service.security.po.UserPO, java.util.List)
     */
    @Override
    @Transactional
    public UserPO update(final String login, final UserPO user, final List<SecurityLabel> userLabels) {

        final UserPO userPO = findByLogin(login);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(UserPO.Fields.ID, userPO.getId());
        paramMap.put(UserPO.Fields.S_USER_ID, userPO.getId());

        paramMap = new HashMap<>();
        paramMap.put(UserPO.Fields.ID, userPO.getId());
        paramMap.put(UserPO.Fields.UPDATED_AT, user.getUpdatedAt());
        paramMap.put(UserPO.Fields.UPDATED_BY, user.getUpdatedBy());
        paramMap.put(UserPO.Fields.ACTIVE, user.isActive());
        paramMap.put(UserPO.Fields.ADMIN, user.isAdmin());
        paramMap.put(UserPO.Fields.EMAIL, user.getEmail());
        paramMap.put(UserPO.Fields.LOCALE, user.getLocale());
        paramMap.put(UserPO.Fields.FIRST_NAME, user.getFirstName());
        paramMap.put(UserPO.Fields.LAST_NAME, user.getLastName());
        paramMap.put(UserPO.Fields.LOGIN, user.getLogin());
        paramMap.put(UserPO.Fields.NOTES, user.getNotes());
        paramMap.put(UserPO.Fields.SOURCE, user.getSource());
        paramMap.put(UserPO.Fields.EXTERNAL, user.isExternal());
        paramMap.put(UserPO.Fields.EMAIL_NOTIFICATION, user.isEmailNotification());

        namedJdbcTemplate.update(UserPO.Queries.UPDATE_BY_ID, paramMap);
        attachToAPI(user, paramMap);

        securityLabelDao.saveLabelsForObject(userPO.getId(), userLabels);

        final List<RolePO> roles = user.getRoles();
        final List<Map<String, Object>> params = new ArrayList<>();
        for (final RolePO role : roles) {
            final Map<String, Object> toAttach = new HashMap<>();
            toAttach.put(UserPO.Fields.S_USERS_ID, userPO.getId());
            toAttach.put(RolePO.Fields.NAME, role.getName());
            params.add(toAttach);
        }
        paramMap.put(UserPO.Fields.S_USERS_ID, userPO.getId());
        namedJdbcTemplate.update(UserPO.Queries.CLEAN_USERS, paramMap);
        namedJdbcTemplate.batchUpdate(UserPO.Queries.ATTACH_TO_ROLE, (params.toArray(new Map[params.size()])));
        if (!CollectionUtils.isEmpty(user.getPassword())) {
            updatePassword(userPO.getId(), user.getPassword().get(0), false);
        }

        return user;
    }

    @Override
    @Transactional
    public void updatePassword(Integer userId, PasswordPO password, boolean temp) {
        if (userId==null || password == null) {
            return;
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(PasswordPO.Fields.S_USER_ID, userId);
		if (!temp) {
			namedJdbcTemplate.update(PasswordPO.Queries.DEACTIVATE_BY_USER_ID, paramMap);
            namedJdbcTemplate.update(PasswordPO.Queries.DEACTIVATE_ACTIVATION_CODES_BY_USER_ID, paramMap);
		}
        paramMap = new HashMap<>();
        paramMap.put(PasswordPO.Fields.UPDATED_AT, password.getUpdatedAt());
        paramMap.put(PasswordPO.Fields.UPDATED_BY, password.getUpdatedBy());
        paramMap.put(PasswordPO.Fields.CREATED_AT, password.getCreatedAt());
        paramMap.put(PasswordPO.Fields.CREATED_BY, SecurityUtils.getCurrentUserName());
        paramMap.put(PasswordPO.Fields.PASSWORD_TEXT, password.getPasswordText());
        paramMap.put(PasswordPO.Fields.ACTIVE, temp?false:true);
        paramMap.put(PasswordPO.Fields.ACTIVATION_CODE, password.getActivationCode());
        paramMap.put(PasswordPO.Fields.S_USER_ID, userId);
        namedJdbcTemplate.update(PasswordPO.Queries.INSERT_NEW, paramMap);
    }

    @Override
    @Transactional
    public boolean updateLocale(String login, String locale) {
        final UserPO userPO = findByLogin(login);

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(UserPO.Fields.ID, userPO.getId());
        paramMap.put(UserPO.Fields.LOCALE, locale);
        return namedJdbcTemplate.update(UserPO.Queries.CHANGE_LOCALE_BY_ID, paramMap) > 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.impl.UserDao#getAll()
     */
    @Override
    @Transactional
    public List<UserPO> getAll() {
        List<UserPO> result = namedJdbcTemplate.query(UserPO.Queries.SELECT_ALL, UserRowMapper.DEFAULT_USER_ROW_MAPPER);
        return result;
    }

    /**
     * {@inheritDoc }.
     *
     * @return the list
     */
    @Override
    public List<UserPropertyPO> loadAllProperties() {
        return namedJdbcTemplate.query(LOAD_ALL_USER_PROPERTIES, userPropertyRowMapper);
    }

    /**
     * {@inheritDoc }.
     *
     * @param name
     *            the name
     * @return the user property po
     */
    @Override
    public UserPropertyPO loadPropertyByName(String name) {
        List<UserPropertyPO> list = namedJdbcTemplate.query(LOAD_USER_PROPERTY_BY_NAME,
                Collections.singletonMap("name", name), userPropertyRowMapper);

        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.UserDao#loadPropertyByDisplayName(java.lang.
     * String)
     */
    @Override
    public UserPropertyPO loadPropertyByDisplayName(final String displayName) {
        final List<UserPropertyPO> list = namedJdbcTemplate.query(LOAD_USER_PROPERTY_BY_DISPLAY_NAME,
                Collections.singletonMap("display_name", displayName), userPropertyRowMapper);

        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * {@inheritDoc }.
     *
     * @param property
     *            the property
     */
    @Override
    public void saveProperty(UserPropertyPO property) {
        if (property.getId() == null) {
            // Insert property
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

            sqlParameterSource.addValue("name", property.getName());
            sqlParameterSource.addValue("required", property.isRequired());
            sqlParameterSource.addValue("read_only", property.isReadOnly());
            sqlParameterSource.addValue("display_name", property.getDisplayName());
            sqlParameterSource.addValue("created_at", property.getCreatedAt());
            sqlParameterSource.addValue("updated_at", property.getUpdatedAt());
            sqlParameterSource.addValue("created_by", property.getCreatedBy());
            sqlParameterSource.addValue("updated_by", property.getUpdatedBy());
            sqlParameterSource.addValue("field_type", property.getFieldType());

            KeyHolder keyHolder = new GeneratedKeyHolder();

            namedJdbcTemplate.update(INSERT_USER_PROPERTY, sqlParameterSource, keyHolder, new String[] { "id" });

            property.setId(keyHolder.getKey().longValue());

        } else {
            // Update property
            MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();

            sqlParameterSource.addValue("name", property.getName());
            sqlParameterSource.addValue("required", property.isRequired());
            sqlParameterSource.addValue("read_only", property.isReadOnly());
            sqlParameterSource.addValue("field_type", property.getFieldType());
            sqlParameterSource.addValue("display_name", property.getDisplayName());
            sqlParameterSource.addValue("updated_at", property.getUpdatedAt());
            sqlParameterSource.addValue("updated_by", property.getUpdatedBy());
            sqlParameterSource.addValue("id", property.getId());

            namedJdbcTemplate.update(UPDATE_USER_PROPERTY_BY_ID, sqlParameterSource);
        }
    }

    /**
     * {@inheritDoc }.
     *
     * @param id
     *            the id
     */
    @Override
    public void deleteProperty(long id) {
        // Delete all values first.
        namedJdbcTemplate.update(DELETE_USER_PROPERTY_VALUES_BY_USER_PROPERTY_ID,
                Collections.singletonMap("userPropertyId", id));

        // Delete property.
        namedJdbcTemplate.update(DELETE_USER_PROPERTY_BY_ID, Collections.singletonMap("userPropertyId", id));
    }

    /**
     * {@inheritDoc }.
     *
     * @param propertyValues
     *            the property values
     */
    @Override
    public void saveUserPropertyValues(Collection<UserPropertyValuePO> propertyValues) {

        if (CollectionUtils.isEmpty(propertyValues)) {
            return;
        }

        List<UserPropertyValuePO> insertValues = new ArrayList<>();
        List<UserPropertyValuePO> updateValues = new ArrayList<>();

        for (UserPropertyValuePO propertyValue : propertyValues) {

            if (propertyValue.getId() == null) {
                insertValues.add(propertyValue);
            } else {
                updateValues.add(propertyValue);
            }
        }

        if (!CollectionUtils.isEmpty(insertValues)) {
            insertUserPropertyValues(insertValues);
        }

        if (!CollectionUtils.isEmpty(updateValues)) {
            Map<String, Object>[] map = createUserPropertyValueParams(updateValues);

            namedJdbcTemplate.batchUpdate(UPDATE_USER_PROPERTY_VALUE_BY_ID, map);
        }
    }

    /**
     * Insert user property values.
     *
     * @param propertyValues
     *            the property values
     */
    private void insertUserPropertyValues(List<UserPropertyValuePO> propertyValues) {

        if (CollectionUtils.isEmpty(propertyValues)) {
            return;
        }

        namedJdbcTemplate.batchUpdate(INSERT_USER_PROPERTY_VALUE, createUserPropertyValueParams(propertyValues));
    }

    /**
     * {@inheritDoc }.
     *
     * @param ids
     *            the ids
     */
    @Override
    public void deleteUserPropertyValuesByIds(Collection<Long> ids) {
        namedJdbcTemplate.update(DELETE_USER_PROPERTY_VALUES_BY_IDS, Collections.singletonMap("listId", ids));
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.UserDao#deleteUserPropertyValuesByUserId(
     * long)
     */
    @Override
    public void deleteUserPropertyValuesByUserId(long userId) {
        namedJdbcTemplate.update(DELETE_USER_PROPERTY_VALUES_BY_USER_ID, Collections.singletonMap("userId", userId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserPropertyValuePO> loadUserPropertyValuesByUserId(Integer userId) {
        return loadUserPropertyValuesByUserIds(Collections.singleton(userId)).get(userId);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.UserDao#loadUserPropertyValuesByUserIds(java.
     * util.Collection)
     */
    @Override
    public Map<Integer, List<UserPropertyValuePO>> loadUserPropertyValuesByUserIds(Collection<Integer> userIds) {

        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }

        return namedJdbcTemplate.query(LOAD_USER_PROPERTY_VALUES_BY_USER_IDS,
                Collections.singletonMap("listId", userIds), rs -> {
                    Map<Integer, List<UserPropertyValuePO>> result = new HashMap<>();

                    while (rs.next()) {
                        Integer userId = rs.getInt(FieldColumns.USER_ID.name());

                        List<UserPropertyValuePO> propValues = result.get(userId);
                        if (propValues == null) {
                            propValues = new ArrayList<>();
                            result.put(userId, propValues);
                        }

                        UserPropertyValuePO propValue = UserPropertyValueRowMapper.DEFAULT_ROW_MAPPER.mapRow(rs, 0);
                        propValues.add(propValue);
                    }

                    return result;
                });
    }

    /**
     * Fill user. TODO Reduce the number of queries performed in the loop
     *
     * @param user
     *            the user
     */
    private void fillUser(final UserPO user) {
        final Map<String, Object> paramMap = new HashMap<>();

        paramMap.put(PasswordPO.Fields.S_USER_ID, user.getId());
        user.setPassword(
                namedJdbcTemplate.query(PasswordPO.Queries.SELECT_BY_USER_ID_ACTIVE_ONLY, paramMap, passwordRowMapper)
        );

        user.setLabelAttributeValues(securityLabelDao.findLabelsAttributesValuesForObject(user.getId()));
        paramMap.put(UserPO.Fields.S_USERS_ID, user.getId());
        user.setRoles(roleDao.findRolesByUserLogin(user.getLogin()));
        user.setApis(namedJdbcTemplate.query(ApiPO.Queries.SELECT_BY_USER_ID, paramMap,
                APIRowMapper.DEFAULT_API_ROW_MAPPER));
        user.setTokens(namedJdbcTemplate.query(TokenPO.Queries.SELECT_BY_USER_ID, paramMap, tokenRowMapper));
        user.setProperties(loadUserPropertyValuesByUserId(user.getId()));
    }

    /**
     * Creates the user property value params.
     *
     * @param propertyValues
     *            the property values
     * @return the map[]
     */
    private Map<String, Object>[] createUserPropertyValueParams(List<UserPropertyValuePO> propertyValues) {
        @SuppressWarnings("unchecked")
        Map<String, Object>[] result = new Map[propertyValues.size()];

        for (int i = 0; i < propertyValues.size(); i++) {
            UserPropertyValuePO propertyValue = propertyValues.get(i);

            Map<String, Object> params = new HashMap<>();

            if (Objects.nonNull(propertyValue.getId())) {
                params.put("id", propertyValue.getId());
            }

            params.put("user_id", propertyValue.getUserId());

            if (propertyValue.getProperty() != null) {
                params.put("property_id", propertyValue.getProperty().getId());
            }

            params.put("value", propertyValue.getValue());
            params.put("created_at", propertyValue.getCreatedAt());
            params.put("updated_at", propertyValue.getUpdatedAt());
            params.put("created_by", propertyValue.getCreatedBy());
            params.put("updated_by", propertyValue.getUpdatedBy());

            result[i] = params;
        }

        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.impl.UserDao#saveProperties(java.util.List,
     * java.lang.String)
     */
    @Override
    @Transactional
    public void saveProperties(final List<UserPropertyDTO> properties, final String login) {
        jdbcTemplate.update("DELETE from s_user_property_value where user_id = (select id from s_user where login=?)",
                login);
        if (!CollectionUtils.isEmpty(properties)) {
            properties.stream().filter(userProperty -> userProperty.getValue() != null).forEach(userProperty -> {
                // TODO: warn! replace it with batch insert instead of loop.
                jdbcTemplate.update(
                        "insert into s_user_property_value(user_id, value, property_id, created_at, created_by) "
                                + "values((select id from s_user where login=?), ?, (select id from s_user_property where name=?),current_timestamp, ?)",
                        login, userProperty.getValue(), userProperty.getName(), SecurityUtils.getCurrentUserName());
            });
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.impl.UserDao#isLastAdmin()
     */
    @Override
    @Transactional
    public boolean isLastAdmin() {
        return jdbcTemplate.queryForObject("select count(id) from s_user where active=true and admin=true",
                Long.class) == 1;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.impl.UserDao#saveToken(com.unidata.mdm.
     * backend.service.security.po.TokenPO)
     */
    @Override
    @Transactional
    public void saveToken(TokenPO tokenPO) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(TokenPO.Fields.UPDATED_AT, tokenPO.getUpdatedAt());
        paramMap.put(TokenPO.Fields.UPDATED_BY, tokenPO.getUpdatedBy());
        paramMap.put(TokenPO.Fields.CREATED_AT, tokenPO.getCreatedAt());
        paramMap.put(TokenPO.Fields.CREATED_BY, tokenPO.getCreatedBy());
        paramMap.put(TokenPO.Fields.TOKEN, tokenPO.getToken());
        paramMap.put(TokenPO.Fields.S_USER_ID, tokenPO.getUser().getId());
        namedJdbcTemplate.update(TokenPO.Queries.INSERT_NEW, paramMap);

    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#loadUserEvents(java.lang.String,
     * java.util.Date, int, int)
     */
    @Override
    public List<UserEventPO> loadUserEvents(String login, Date from, int page, int count) {

        MeasurementPoint.start();
        try {
            boolean returnAll = page <= 0 || count <= 0;
            Timestamp point = from == null ? null : new Timestamp(from.getTime());
            if (returnAll) {
                return jdbcTemplate.query(LOAD_USER_EVENTS_BY_LOGIN_AND_DATE_SQL, UserEventRowMapper.DEFAULT_ROW_MAPPER,
                        login, point);
            } else {
                return jdbcTemplate.query(LOAD_USER_EVENTS_BY_LOGIN_LIMIT_OFFSET_AND_DATE_SQL,
                        UserEventRowMapper.DEFAULT_ROW_MAPPER, login, point, (page - 1) * count, count);
            }
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.UserDao#countUserEvents(java.lang.String)
     */
    @Override
    public Long countUserEvents(String login) {
        MeasurementPoint.start();
        try {
            return jdbcTemplate.queryForObject(COUNT_EVENT_BY_LOGIN, Long.class, login);

        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.UserDao#deleteUserEvent(java.lang.String)
     */
    @Override
    public boolean deleteUserEvent(String eventId) {

        MeasurementPoint.start();
        try {
            largeObjectsDAO.cleanForSubjectIds(Collections.singletonList(eventId));
            return jdbcTemplate.update(DELETE_EVENT_BY_EVENT_ID_SQL, eventId) > 0;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#deleteUserEvents(List)
     */
    @Override
    public boolean deleteUserEvents(List<String> eventIds) {

        MeasurementPoint.start();
        try {
            largeObjectsDAO.cleanForSubjectIds(eventIds);

            Map<String, Object> params = new HashMap<>();
            params.put(UserEventPO.FIELD_ID, eventIds);
            return namedJdbcTemplate.update(DELETE_EVENTS_BY_EVENT_IDS_SQL, params) > 0;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.unidata.mdm.backend.dao.UserDao#deleteAllUserEvents(java.lang.String,
     * java.util.Date)
     */
    @Override
    public boolean deleteAllUserEvents(String login, Date to) {

        MeasurementPoint.start();
        try {
            Timestamp point = to == null ? null : new Timestamp(to.getTime());
            return jdbcTemplate.update(DELETE_EVENTS_BY_LOGIN_AND_DATE_SQL, login, point) > 0;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#create(com.unidata.mdm.backend.
     * service.security.po.UserEventPO)
     */
    @Override
    public UserEventPO create(UserEventPO event) {
        MeasurementPoint.start();
        try {

            String newId = IdUtils.v1String();

            int result = jdbcTemplate.update(PUT_USER_EVENT_SQL, newId, event.getUserId(), event.getType(),
                    event.getContent(), event.getDetails(), event.getCreatedBy());

            if (result > 0) {
                event.setId(newId);
                return event;
            }

            return null;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#create(com.unidata.mdm.backend.
     * service.security.po.UserEventPO, java.lang.String, java.lang.String)
     */
    @Override
    public UserEventPO create(UserEventPO event, String login) {

        MeasurementPoint.start();
        try {

            String newId = IdUtils.v1String();

            int result = jdbcTemplate.update(PUT_USER_EVENT_BY_LOGIN_SQL, newId, login, event.getType(),
                    event.getContent(), event.getDetails(), event.getCreatedBy());

            if (result > 0) {
                event.setId(newId);
                return event;
            }

            return null;
        } finally {
            MeasurementPoint.stop();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#deleteToken(java.lang.String)
     */
    @Override
    public void deleteToken(String tokenString) {
        jdbcTemplate.update("delete from s_token cascade where token=?", tokenString);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.unidata.mdm.backend.dao.UserDao#findByLogin(java.lang.String)
     */
    @Override
    public UserPO findByLogin(String login) {
        final Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(UserPO.Fields.LOGIN, login);
        final List<UserPO> result = namedJdbcTemplate.query(UserPO.Queries.SELECT_BY_LOGIN, paramMap,
                UserRowMapper.DEFAULT_USER_ROW_MAPPER);

        if (result == null || result.size() == 0) {
            return null;
        }
        final UserPO user = result.get(0);

        fillUser(user);

        return user;
    }

    @Override
    public List<ApiPO> getAPIList() {
        final List<ApiPO> result = namedJdbcTemplate.query(ApiPO.Queries.SELECT_ALL,
                APIRowMapper.DEFAULT_API_ROW_MAPPER);
        return result;
    }

    @Override
    public List<UserPO> fetchUsersFullInfo() {
        final Map<Integer, UserPO> users = jdbcTemplate.query(selectUsersWithProperties, this::extractUsers);
        jdbcTemplate.query(selectUsersEndpoints, rs -> {
            while (rs.next()) {
                final ApiPO apiPO = new ApiPO();
                apiPO.setName(rs.getString("apiName"));
                users.get(rs.getInt("userId")).addApi(apiPO);
            }
            return null;
        });
        jdbcTemplate.query(selectUsersRoles, rs -> {
            while (rs.next()) {
                final RolePO rolePO = new RolePO();
                rolePO.setName(rs.getString("roleName"));
                users.get(rs.getInt("userId")).addRole(rolePO);
            }
            return null;
        });
        securityLabelDao.fetchObjectsSecurityLabelsValues().forEach((userId, labelAttributeValuePOS) ->
                users.get(userId).setLabelAttributeValues(labelAttributeValuePOS)
        );
        return new ArrayList<>(users.values());
    }

    private Map<Integer, UserPO> extractUsers(ResultSet rs) throws SQLException {
        final Map<Integer, UserPO> result = new HashMap<>();
        while (rs.next()) {
            final int id = rs.getInt("id");
            final UserPO userPO = result.computeIfAbsent(id, userId -> {
                final UserPO user = new UserPO();
                user.setId(userId);
                try {
                    user.setLogin(rs.getString("login"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setEmail(rs.getString("email"));
                    user.setSource(rs.getString("source"));
                    user.setLocale(rs.getString("locale"));
                    user.setActive(rs.getBoolean("active"));
                    user.setAdmin(rs.getBoolean("admin"));
                    user.setExternal(rs.getBoolean("external"));
                    user.setEmailNotification(rs.getBoolean("email_notification"));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return user;
            });
            if (rs.getString("propertyValue") != null) {
                userPO.addProperty(extractPropertyValueFromResultSet(rs));
            }
        }
        return result;
    }

    private UserPropertyValuePO extractPropertyValueFromResultSet(ResultSet rs) throws SQLException {
        final UserPropertyValuePO userPropertyValuePO = new UserPropertyValuePO();
        final UserPropertyPO property = new UserPropertyPO();
        property.setName(rs.getString("propertyName"));
        userPropertyValuePO.setProperty(property);
        userPropertyValuePO.setValue(rs.getString("propertyValue"));
        return userPropertyValuePO;
    }

    @Override
    public void deleteUsersByLogin(final List<String> logins) {
        if (CollectionUtils.isNotEmpty(logins)) {
            final Map<String, List<String>> loginParameter = Collections.singletonMap("logins", logins);
            namedJdbcTemplate.update(deleteUsersPasswords, loginParameter);
            namedJdbcTemplate.update(deleteUsersPropertiesValues, loginParameter);
            namedJdbcTemplate.update(deleteUsers, loginParameter);
        }
    }

    @Override
    public void saveUsers(List<UserPO> users, Map<String, List<SecurityLabel>> userLabels) {
        insertUsers(users);
        insertUsersPropertiesValues(users);
        insertUsersApis(users);
        insertUserRoles(users);
        insertUserLabels(userLabels);
    }

    private void insertUsers(final List<UserPO> users) {
        jdbcTemplate.batchUpdate(
                insertUsers,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        final UserPO user = users.get(i);
                        ps.setString(1, user.getLogin());
                        ps.setString(2, user.getEmail());
                        ps.setString(3, user.getFirstName());
                        ps.setString(4, user.getLastName());
                        ps.setBoolean(5, user.isActive());
                        ps.setBoolean(6, user.isAdmin());
                        ps.setBoolean(7, user.isExternal());
                        ps.setString(8, user.getSource());
                        ps.setString(9, user.getLocale());
                        ps.setBoolean(10, user.isEmailNotification());
                    }

                    @Override
                    public int getBatchSize() {
                        return users.size();
                    }
                }
        );
    }

    private void insertUsersPropertiesValues(final List<UserPO> users) {
        final Map[] values = users.stream()
                .filter(u -> CollectionUtils.isNotEmpty(u.getProperties()))
                .flatMap(u ->
                        u.getProperties().stream().map(p ->
                                Map.of(
                                        "login", u.getLogin(),
                                        "property", p.getProperty().getName(),
                                        "value", p.getValue(),
                                        "createdAt", new Date(),
                                        "createdBy", SecurityUtils.getCurrentUserName()
                                )
                        )
                )
                .toArray(Map[]::new);
        if (ArrayUtils.isNotEmpty(values)) {
            namedJdbcTemplate.batchUpdate(insertPropertiesValues, values);
        }
    }

    private void insertUsersApis(final List<UserPO> users) {
        final Map[] links = users.stream()
                .filter(u -> CollectionUtils.isNotEmpty(u.getApis()))
                .flatMap(u ->
                        u.getApis().stream().map(a ->
                                Map.of("login", u.getLogin(), "name", a.getName())
                        )
                )
                .toArray(Map[]::new);
        if (ArrayUtils.isNotEmpty(links)) {
            namedJdbcTemplate.batchUpdate(insertUsersApis, links);
        }
    }

    private void insertUserRoles(final List<UserPO> users) {
        final Map[] roles = users.stream()
                .filter(u -> CollectionUtils.isNotEmpty(u.getRoles()))
                .flatMap(u ->
                        u.getRoles().stream().map(r ->
                                Map.of(
                                        "login", u.getLogin(),
                                        "role", r.getName(),
                                        "createdAt", new Date(),
                                        "createdBy", SecurityUtils.getCurrentUserName()
                                )
                        )
                )
                .toArray(Map[]::new);
        if (ArrayUtils.isNotEmpty(roles)) {
            namedJdbcTemplate.batchUpdate(insertUsersRoles, roles);
        }
    }

    private void insertUserLabels(final  Map<String, List<SecurityLabel>> userLabels) {
        final Map<String, Integer> usersIds = getAll().stream()
                .collect(Collectors.toMap(BaseUserPO::getLogin, BaseUserPO::getId));
        if (MapUtils.isNotEmpty(userLabels)) {
            userLabels.forEach((key, value) -> securityLabelDao.saveLabelsForObject(usersIds.get(key), value));
        }
    }

    @Override
    public List<PasswordPO> fetchAllUsersPasswords() {
        return jdbcTemplate.query(fetchAllUsersPasswords, passwordWithUserRowMapper);
    }

    @Override
    public void saveUsersPasswords(List<PasswordPO> passwords) {
        if (CollectionUtils.isNotEmpty(passwords)) {
            final Map[] values = passwords.stream().map(p ->
                    Map.of(
                            "password", p.getPasswordText(),
                            "active", p.getActive(),
                            "login", p.getUser().getLogin()
                    )
            ).toArray(Map[]::new);
            namedJdbcTemplate.batchUpdate(insertUsersPasswords, values);
        }
    }

    @Override
    public List<PasswordPO> fetchLastUserPasswords(long userId, int lastCount) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put(PasswordPO.Fields.S_USER_ID, userId);
        paramMap.put(PasswordPO.ROWS_LIMIT, lastCount);
        return namedJdbcTemplate.query(fetchLastUserPasswords, paramMap, passwordWithUserRowMapper);
    }

    @Override
    public void addUsersRoles(Map<String, Set<String>> userRoleNames) {
        final List<Map<String, Object>> params = new ArrayList<>();

        userRoleNames.forEach((key, value) -> value.forEach(roleName -> {
            final Map<String, Object> toAttach = new HashMap<>();
            toAttach.put(UserPO.Fields.LOGIN, key);
            toAttach.put(RolePO.Fields.NAME, roleName);
            params.add(toAttach);
        }));

        namedJdbcTemplate.batchUpdate(UserPO.Queries.ATTACH_TO_ROLE_BY_LOGIN, (params.toArray(new Map[params.size()])));
    }

	@Override
	public void activatePassword(String activationCode) {
		List<Integer> result=jdbcTemplate.queryForList("select s_user_id from s_password where activation_code=?", Integer.class, activationCode);
		if(result ==null|| result.size()==0) {
			return;
		}
		Integer userId = result.get(0);
		Map<String, Object> params = new HashMap<>();
		params.put(UserPO.Fields.S_USER_ID, userId);
		params.put(PasswordPO.Fields.ACTIVATION_CODE, activationCode);
		namedJdbcTemplate.update(PasswordPO.Queries.DEACTIVATE_BY_USER_ID, params);
		namedJdbcTemplate.update(PasswordPO.Queries.ACTIVATE_PASSWORD, params);
	}
}
