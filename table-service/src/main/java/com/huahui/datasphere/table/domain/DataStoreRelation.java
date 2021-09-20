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
 * 数据表存储关系表
 */
@Table(tableName = "data_store_relation")
public class DataStoreRelation implements Serializable {

	private static final long serialVersionUID = 321566870187324L;
	
	
	
	@Column(name = "dsl_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "数据存储层ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String dslId;
	
	
	@Column(name = "table_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "对象采集任务编号:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String tableId;
	
	
	@Column(name = "data_source", nullable = false, updatable = false, columnDefinition = "VARCHAR")
	@NotNull(message = "name cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "存储层-数据来源(StoreLayerDataSource):1-db采集<DB> 2-数据库采集<DBA> 3-对象采集<OBJ> 4-数据集市<DM> 5-数据管控<DQ> 6-自定义<UD> ", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String dataSource;

	@Column(name = "is_successful", nullable = false, columnDefinition = "INT")
	@ApiModelProperty(required = true, value = "是否入库成功(JobExecuteState):100-等待<DengDai> 101-运行<YunXing> 102-暂停<ZanTing> 103-中止<ZhongZhi> 104-完成<WanCheng> 105-失败<ShiBai> ", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String isSuccessful;

	

	

	public String getDslId() {
		return dslId;
	}

	public void setDslId(String dslId) {
		this.dslId = dslId;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getIsSuccessful() {
		return isSuccessful;
	}

	public void setIsSuccessful(String isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	
	
	
	
}
