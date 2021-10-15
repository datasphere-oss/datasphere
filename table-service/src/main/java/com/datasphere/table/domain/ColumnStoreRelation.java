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

package com.datasphere.table.domain;

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
 * 数据字段存储关系表
 */
@Table(tableName = "dcol_relation_store")
public class ColumnStoreRelation implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Column(name = "csi_number", nullable = false, columnDefinition = "BIGINT")
	@NotNull(message = "csiNumber不能为空")
	@ApiModelProperty(required = true, value = "序号位置", example = "12345678")
	private Long csiNumber;
	
	
	@Id
	@Column(name = "dslad_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "附加信息ID不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "附加信息ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String dsladId;
	
	
	@Id
	@Column(name = "column_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "结构信息id不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "结构信息id", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String columnId;
	
	@Column(name = "data_source", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "存储层-数据来源不能为空")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "存储层-数据来源(StoreLayerDataSource):1-db采集<DB> 2-数据库采集<DBA> 3-对象采集<OBJ> 4-数据集市<DM> 5-数据管控<DQ> 6-自定义<UD> ", example = "")
	private String dataSource;

	public Long getCsiNumber() {
		return csiNumber;
	}

	public void setCsiNumber(Long csiNumber) {
		this.csiNumber = csiNumber;
	}

	public String getDsladId() {
		return dsladId;
	}

	public void setDsladId(String dsladId) {
		this.dsladId = dsladId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	
}
