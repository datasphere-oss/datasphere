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

package com.huahui.datasphere.mdm.system.service.impl;

import java.util.Locale;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.RandomBasedGenerator;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.huahui.datasphere.mdm.system.configuration.SystemConfigurationConstants;
import com.huahui.datasphere.mdm.system.exception.PlatformFailureException;
import com.huahui.datasphere.mdm.system.exception.SystemExceptionIds;
import com.huahui.datasphere.mdm.system.service.PlatformConfiguration;
import com.huahui.datasphere.mdm.system.type.annotation.ConfigurationRef;
import com.huahui.datasphere.mdm.system.type.configuration.ConfigurationValue;
import com.huahui.datasphere.mdm.system.type.format.DumpTargetFormat;
import com.huahui.datasphere.mdm.system.util.TextUtils;
import com.sun.xml.fastinfoset.sax.Properties;

/**
 * @author theseusyang
 * Platform configuration.
 */

@Component
@Configuration
@PropertySource("classpath:conf/backend.properties")
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
    
    @Autowired 
    Environment env;
    
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
            String message = TextUtils.getTextWithLocaleAndDefault(Locale.CHINESE, SystemExceptionIds.EX_SYSTEM_NODE_ID_UNDEFINED.message(), "");
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
