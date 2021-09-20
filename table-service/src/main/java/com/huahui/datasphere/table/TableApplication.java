/*
 * Apache License
 * 
 * Copyright (c) 2020 HuahuiData
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

package com.huahui.datasphere.table;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Launcher for the dataset service Spring-Boot app.
 */
@SpringBootApplication
public class TableApplication implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static final String CONFIG_ENV_VAR_NAME = "SPRING_APPLICATION_JSON";

	/**
	 * Emits a message about the environment configuration. Runs only when started
	 * on the command line via java -jar ..; does not run when started by test use
	 * of Spring Runner.
	 * 
	 * @param args
	 *            Command line
	 * @throws IOException
	 *             If JSON is present but cannot be parsed
	 */
	public static void main(String[] args) throws IOException {
		final String springApplicationJson = System.getenv(CONFIG_ENV_VAR_NAME);
		if (springApplicationJson != null && springApplicationJson.contains("{")) {
			final ObjectMapper mapper = new ObjectMapper();
			// ensure it's valid; Spring silently ignores if not parseable
			mapper.readTree(springApplicationJson);
			logger.info("main: successfully parsed configuration from environment {}", CONFIG_ENV_VAR_NAME);
		} else {
			logger.warn("main: no configuration found in environment {}", CONFIG_ENV_VAR_NAME);
		}
		ConfigurableApplicationContext cxt = SpringApplication.run(TableApplication.class, args);
		logger.info("main: context is {}", cxt);
		// Closing the context stops the application, so ignore
		// the Sonar advice that the context must be closed here!
	}
	

	@Override
	public void setApplicationContext(ApplicationContext context) {
		final String activeProfile = "src";
		logger.info("setApplicationContext: setting profile {}", activeProfile);
		((ConfigurableEnvironment) context.getEnvironment()).setActiveProfiles(activeProfile);
	}

}
