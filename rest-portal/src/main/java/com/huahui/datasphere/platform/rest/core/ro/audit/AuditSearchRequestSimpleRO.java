/*
 * Unidata Platform
 * Copyright (c) 2013-2020, UNIDATA LLC, All rights reserved.
 *
 * Commercial License
 * This version of Unidata Platform is licensed commercially and is the appropriate option for the vast majority of use cases.
 *
 * Please see the Unidata Licensing page at: https://unidata-platform.com/license/
 * For clarification or additional options, please contact: info@unidata-platform.com
 * -------
 * Disclaimer:
 * -------
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 */
package com.huahui.datasphere.platform.rest.core.ro.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
 * The Class AuditSearchRequest.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuditSearchRequestSimpleRO {
	
	
	/** The username. */
	private String username;
	
	/** The registry. */
	private String registry;
	
	/** The operation. */
	private String operation;
	
	/** The type. */
	private String type;
	
	/** The etalon. */
	private String etalon;
	
	/** The count. */
	private String count;
	
	/** The page. */
	private String page;
	
	/** The start date. */
	private String startDate;
	
	/** The end date. */
	private String endDate;
	
	/** The external id. */
	private String externalId;
	private boolean isExport;

	/**
	 * Gets the username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username.
	 *
	 * @param username            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the registry.
	 *
	 * @return the registry
	 */
	public String getRegistry() {
		return registry;
	}

	/**
	 * Sets the registry.
	 *
	 * @param registry            the registry to set
	 */
	public void setRegistry(String registry) {
		this.registry = registry;
	}

	/**
	 * Gets the operation.
	 *
	 * @return the operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * Sets the operation.
	 *
	 * @param operation            the operation to set
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type.
	 *
	 * @param type            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets the etalon.
	 *
	 * @return the etalon
	 */
	public String getEtalon() {
		return etalon;
	}

	/**
	 * Sets the etalon.
	 *
	 * @param etalon            the etalon to set
	 */
	public void setEtalon(String etalon) {
		this.etalon = etalon;
	}

	/**
	 * Gets the count.
	 *
	 * @return the count
	 */
	public String getCount() {
		return count;
	}

	/**
	 * Sets the count.
	 *
	 * @param count            the count to set
	 */
	public void setCount(String count) {
		this.count = count;
	}

	/**
	 * Gets the page.
	 *
	 * @return the page
	 */
	public String getPage() {
		return page;
	}

	/**
	 * Sets the page.
	 *
	 * @param page            the page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/**
	 * Gets the start date.
	 *
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * Sets the start date.
	 *
	 * @param startDate            the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * Gets the end date.
	 *
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * Sets the end date.
	 *
	 * @param endDate            the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * Gets the external id.
	 *
	 * @return the externalId
	 */
	public String getExternalId() {
		return externalId;
	}

	/**
	 * Sets the external id.
	 *
	 * @param externalId            the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return the isExport
	 */
	public boolean isExport() {
		return isExport;
	}

	/**
	 * @param isExport the isExport to set
	 */
	public void setExport(boolean isExport) {
		this.isExport = isExport;
	}
}
