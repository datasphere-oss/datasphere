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

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class TableUsageInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	
	
	@DocBean(name ="table_blsystem",value="数据表所属系统(DataSourceType):ISL-贴源层_01<ISL> DCL-贴源层<DCL> DPL-加工层-废弃<DPL> DML-加工层<DML> SFL-系统层<SFL> AML-AI模型层<AML> DQC-管控层<DQC> UDL-自定义层<UDL> ",dataType = String.class,required = true)
	private String table_blsystem;
	@DocBean(name ="original_name",value="原始文件名称:",dataType = String.class,required = true)
	private String original_name;
	@DocBean(name ="user_id",value="用户ID:",dataType = Long.class,required = true)
	private Long user_id;
	
		
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "储存ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private Long id;
	
	
	
	@Column(name = "register_name", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "register_name cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(example = "系统登记表名")
	private String registerName;
	
	
	@Column(name = "orig_table_name", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "orig_table_name cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(value = "源表名", example = "")
	private String origTableName;
	
	
	@Column(name = "table_note", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "table_note cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(example = "表说明")
	private String tableNote;
	
	@Column(name = "table_store_id", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "table_store_id cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(value="数据表所属系统:ISL-贴源层 DCL-贴源层 DPL-加工层-废弃 DML-加工层 SFL-系统层 AML-AI模型层 DQC-管控层 UDL-自定义层 ", example = "")
	private String tableStoreId;
	
	@Id
	@Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "use_id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "用户ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private Long userId;
	
	
	

}
