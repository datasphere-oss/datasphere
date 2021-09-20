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

package com.huahui.datasphere.table.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Collections;

/**
 * 数据存储信息表
 */
@Table(tableName = "data_store_layer")
public class DataStoreLayer implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "数据存储层ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	
	
	@Column(name = "name", nullable = false, updatable = false, columnDefinition = "VARCHAR")
	@NotNull(message = "name cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "存储层名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String name;
	
	@Column(name = "store_type", nullable = false, updatable = false, columnDefinition = "VARCHAR")
	@NotNull(message = "store type cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "存储类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String storeType;
	
	@Column(name = "remark", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "备注", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String remark;
	
	
	@Column(name = "dtcs_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "dtcs_id", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String dtcsId;
	
	@Column(name = "dlcs_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "dlcs_id", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String dlcsId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDtcsId() {
		return dtcsId;
	}

	public void setDtcsId(String dtcsId) {
		this.dtcsId = dtcsId;
	}

	public String getDlcsId() {
		return dlcsId;
	}

	public void setDlcsId(String dlcsId) {
		this.dlcsId = dlcsId;
	}
	
	
}
