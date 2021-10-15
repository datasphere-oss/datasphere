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

package com.datasphere.table.controller;

import java.lang.invoke.MethodHandles;

import com.huahui.datasphere.cds.CodeNameType;
import com.huahui.datasphere.cds.repository.TagRepository;
import com.huahui.datasphere.cds.service.CodeNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Base controller class that provides utility methods.
 */
public abstract class AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private CodeNameService codeNameService;
	// Shared among controllers
	@Autowired
	protected TagRepository tagRepository;

	protected static final String NO_ENTRY_WITH_ID = "No entry with ID ";
	protected static final String ENTRY_EXISTS_WITH_ID = "Entry exists with ID ";

	/**
	 * For general use in these methods and subclasses
	 */
	protected final ObjectMapper mapper;

	/**
	 * Hello Spring, here's your no-arg constructor.
	 */
	public AbstractController() {
		mapper = new ObjectMapper();
	}

	/**
	 * Searches the exception-cause stack for an exception caused by violated
	 * constraints.
	 * 
	 * @param thrown
	 *                   Exception
	 * @return A ConstraintViolationException if found in the exception-cause stack,
	 *         otherwise the original argument.
	 */
	protected Exception findConstraintViolationException(Exception thrown) {
		Exception bestMatch = thrown;
		// Use a new variable to silence Sonar
		Exception ex = thrown;
		while (ex != null) {
			if (logger.isDebugEnabled()) // silence Sonar complaint
				logger.debug("findConstraintViolationException: checking ex {}", ex.toString());
			// This once searched for database-specific exceptions by name, for example
			// com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException,
			// but the project is not using MySql and Sonar flagged the usage of a class
			// name as a critical bug.
			if (ex instanceof javax.validation.ConstraintViolationException
					|| ex instanceof org.hibernate.exception.ConstraintViolationException)
				bestMatch = ex;

			// Go as far up the stack as possible
			if (ex.getCause() instanceof Exception)
				ex = (Exception) ex.getCause();
			else
				break;
		}
		return bestMatch;
	}

	/**
	 * Validates the specified code against the specified type
	 * 
	 * @param code
	 *                 Code value
	 * @param type
	 *                 Value set name
	 * @throws IllegalArgumentException
	 *                                      if the code is null or not recognized
	 */
	protected void validateCode(String code, CodeNameType type) {
		if (code == null)
			throw new IllegalArgumentException("Unexpected null for CodeNameType " + type.name());
		if (!codeNameService.validateCode(code, type))
			throw new IllegalArgumentException("Unexpected code " + code + " for CodeNameType " + type.name());
	}

}
