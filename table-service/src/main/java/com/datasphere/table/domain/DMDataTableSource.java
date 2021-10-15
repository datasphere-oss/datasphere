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

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DMDataTableSource implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "source_table_id", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "已选数据源表ID")
	@Size(max = 1024)
	@ApiModelProperty(value = "数据来源类型:ISL-贴源层 DCL-贴源层 DPL-加工层-废弃 DML-加工层 SFL-系统层 AML-AI模型层 DQC-管控层 UDL-自定义层 ", example = "")
	private Long source_table_id;
	
	@Column(name = "source_table_name", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "已选数据源表名")
	@Size(max = 1024)
	@ApiModelProperty(value = "已选数据源表名", example = "")
	private String source_table_name;

	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "备注", example = "")
	private String remark;
	
	
	@Column(name = "source_type", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据来源类型不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "数据来源类型:ISL-贴源层 DCL-贴源层 DPL-加工层-废弃 DML-加工层 SFL-系统层 AML-AI模型层 DQC-管控层 UDL-自定义层 ", example = "")
	private String sourceType;
	
	
	@Column(name = "datatable_id", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据表ID不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "数据表ID", example = "")
	private Long dataTableId;
	
	

}
