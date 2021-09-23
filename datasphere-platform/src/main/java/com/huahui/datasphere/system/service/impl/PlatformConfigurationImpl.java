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

package com.huahui.datasphere.system.service.impl;

import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.unidata.mdm.system.configuration.SystemConfigurationConstants;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.huahui.datasphere.system.exception.PlatformFailureException;
import com.huahui.datasphere.system.exception.SystemExceptionIds;
import com.huahui.datasphere.system.service.PlatformConfiguration;
import com.huahui.datasphere.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.system.type.format.DumpTargetFormat;
import com.huahui.datasphere.system.util.TextUtils;

/**
 * @author Mikhail Mikhailov
 * Platform configuration.
 */
@Component
public class PlatformConfigurationImpl implements PlatformConfiguration, InitializingBean {
    /**
     * The logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformConfigurationImpl.class);
    private static final String VALID_CHARS = "0123456789abcdefABCDEF";
    private static final int CORRECT_NODE_LENGTH = 12;
    /**
     * Node ID. Fail, if null.
     */
    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_NODE_ID)
    private ConfigurationValue<String> nodeId;
    /**
     * Version string. Fail, if null.
     */
    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_PLATFORM_VERSION)
    private ConfigurationValue<String> versionString;
    /**
     * The default dump target format.
     */
    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_DUMP_TARGET_FORMAT)
    private ConfigurationValue<DumpTargetFormat> dumpTargetFormat;
    /**
     * Whether we run in developer mode.
     */
    @ConfigurationRef(SystemConfigurationConstants.PROPERTY_DEVELOPER_MODE)
    private ConfigurationValue<Boolean> developerMode;
    /**
     * Major number.
     */
    private int platformMajor;
    /**
     * Minor number.
     */
    private int platformMinor;
    /**
     * Patch number.
     */
    private int platformPatch;
    /**
     * Revision number.
     */
    private int platformRevision;
    /**
     * V1 generator.
     */
    private TimeBasedGenerator v1Generator;
    /**
     * V4 generator.
     */
    private RandomBasedGenerator v4Generator;
    /**
     * Constructor.
     */
    PlatformConfigurationImpl() {
        super();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlatformMajor() {
        return platformMajor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlatformMinor() {
        return platformMinor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlatformPatch() {
        return platformPatch;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlatformRevision() {
        return platformRevision;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeId() {
        return nodeId.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DumpTargetFormat getDumpTargetFormat() {
        return dumpTargetFormat.getValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDeveloperMode() {
        return developerMode.getValue();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String v1IdString() {
        return v1Generator.generate().toString();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UUID v1Id() {
        return v1Generator.generate();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String v4IdString() {
        return v4Generator.generate().toString();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UUID v4Id() {
        return v4Generator.generate();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {

        if (!nodeId.hasValue()
          || nodeId.getValue().length() != CORRECT_NODE_LENGTH
          || !StringUtils.containsOnly(nodeId.getValue(), VALID_CHARS)) {
            String message = TextUtils.getTextWithLocaleAndDefault(Locale.ENGLISH, SystemExceptionIds.EX_SYSTEM_NODE_ID_UNDEFINED.message(), "");
            LOGGER.error(message);
            throw new PlatformFailureException(message, SystemExceptionIds.EX_SYSTEM_NODE_ID_UNDEFINED);
        }

        initIdGenerators();
        initVersionNumbers();
    }
    /**
     * Initializes node id
     */
    private void initIdGenerators() {

        byte[] octets = new byte[6];
        for (int i = 0; i < nodeId.getValue().length(); i += 2) {
            String octetAsString = nodeId.getValue().substring(i, i + 2);
            byte octet = (byte) Integer.parseInt(octetAsString, 16);
            octets[i / 2] = octet;
        }
        octets[0] |= (byte) 0x01; // Set multicast bit.

        this.v1Generator = Generators.timeBasedGenerator(new EthernetAddress(octets));
        this.v4Generator = Generators.randomBasedGenerator();
    }
    /**
     * Initializes version numbers.
     */
    private void initVersionNumbers() {

        if (!versionString.hasValue()) {
            final String message = "backend.properties doesn't contain 'unidata.platform.version' property or platform version is not defined. Exiting!";
            LOGGER.error(message);
            throw new PlatformFailureException(message, SystemExceptionIds.EX_SYSTEM_PLATFORM_VERSION_UNDEFINED);
        }

        boolean versionFieldValid = false;
        String[] versionTokens = StringUtils.split(versionString.getValue(), '.');
        if (versionTokens.length >= 2 && versionTokens.length <= 4) {
            try {
                platformMajor = Integer.parseInt(versionTokens[0]);
                platformMinor = Integer.parseInt(versionTokens[1]);
                platformPatch = versionTokens.length > 2 ? Integer.parseInt(versionTokens[2]) : 0;
                platformRevision = versionTokens.length > 3 ? Integer.parseInt(versionTokens[3]) : 0;
                versionFieldValid = true;
            } catch (NumberFormatException e) {
                LOGGER.warn("NumberFormatException caught while parsing version number. Input {}.", versionString, e);
            }
        }

        if (!versionFieldValid) {
            final String message = "'unidata.platform.version' property contains invalid data. Version must be given in the major.minor[.patch optional][.revision optional] (all integer numbers) format. Exiting!";
            LOGGER.error(message);
            throw new PlatformFailureException(message, SystemExceptionIds.EX_SYSTEM_PLATFORM_VERSION_INVALID);
        }
    }
}
