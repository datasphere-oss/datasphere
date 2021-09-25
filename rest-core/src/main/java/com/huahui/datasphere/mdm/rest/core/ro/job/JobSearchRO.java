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
package com.huahui.datasphere.mdm.rest.core.ro.job;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * JobSearchRO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobSearchRO {

	/** The text. */
	private String text;

	/** The enabled status. */
	private String enabledStatus;

	/** The last execution status. */
	private String lastExecutionStatus;

	/** The page. */
	private int page;

	/** The count. */
	private int count;

	/** The start. */
	private long start;

	/** The sort fields. */
	private List<JobSortField> sortFields;
	
	/** The tags. */
	private List<String> tags;

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text
	 *            the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the enabled status.
	 *
	 * @return the enabled status
	 */
	public String getEnabledStatus() {
		return enabledStatus;
	}

	/**
	 * Sets the enabled status.
	 *
	 * @param enabledStatus
	 *            the new enabled status
	 */
	public void setEnabledStatus(String enabledStatus) {
		this.enabledStatus = enabledStatus;
	}

	/**
	 * Gets the last execution status.
	 *
	 * @return the last execution status
	 */
	public String getLastExecutionStatus() {
		return lastExecutionStatus;
	}

	/**
	 * Sets the last execution status.
	 *
	 * @param lastExecutionStatus
	 *            the new last execution status
	 */
	public void setLastExecutionStatus(String lastExecutionStatus) {
		this.lastExecutionStatus = lastExecutionStatus;
	}

	/**
	 * Gets the page.
	 *
	 * @return the page
	 */
	public int getPage() {
		return page;
	}

	/**
	 * Sets the page.
	 *
	 * @param page
	 *            the new page
	 */
	public void setPage(int page) {
		this.page = page;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count
	 *            the new count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * Gets the start.
	 *
	 * @return the start
	 */
	public long getStart() {
		return start;
	}

	/**
	 * Sets the start.
	 *
	 * @param start
	 *            the new start
	 */
	public void setStart(long start) {
		this.start = start;
	}

	/**
	 * Gets the sort fields.
	 *
	 * @return the sort fields
	 */
	public List<JobSortField> getSortFields() {
		return sortFields;
	}

	/**
	 * Sets the sort fields.
	 *
	 * @param sortFields
	 *            the new sort fields
	 */
	public void setSortFields(List<JobSortField> sortFields) {
		this.sortFields = sortFields;
	}

	/**
	 * Gets the tags.
	 *
	 * @return the tags
	 */
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Sets the tags.
	 *
	 * @param tags the new tags
	 */
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
