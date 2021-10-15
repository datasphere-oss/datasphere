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
 * 数据存储附加信息表
 */
//@Table(tableName = "data_store_layer_added")
public class DataStoreAddition implements Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "附加信息ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	
	
	@Id
	@Column(name = "dsl_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "dsl_id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "存储层配置ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String dslId;
	
	
	@Column(name = "dsla_storelayer", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "dsla_storelayer cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "配置附加属性信息(StoreLayerAdded):01-主键<ZhuJian> 02-rowkey<RowKey> 03-索引列<SuoYinLie> 04-预聚合列<YuJuHe> 05-排序列<PaiXuLie> 06-分区列<FenQuLie> 07-Solr列<Solr> ", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String dsla_storelayer;
	
	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(example = "备注")	
	private String remark;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getDslId() {
		return dslId;
	}


	public void setDslId(String dslId) {
		this.dslId = dslId;
	}


	public String getDsla_storelayer() {
		return dsla_storelayer;
	}


	public void setDsla_storelayer(String dsla_storelayer) {
		this.dsla_storelayer = dsla_storelayer;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

	
}
