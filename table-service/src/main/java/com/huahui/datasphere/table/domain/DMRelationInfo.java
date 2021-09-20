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

import io.swagger.annotations.ApiModelProperty;

public class DMRelationInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "作业相关ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	
	@Column(name = "pre_process", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(example = "前置作业")
	private String preProcess;
	
	
	@Column(name = "post_process", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(example = "后置作业")
	private String postProcess;
	
	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "备注", example = "")
	
	private String remark;
	
	
	@Column(name = "datatable_id", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据表ID不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "数据表ID", example = "")
	private String dataTableId;
	
	
}
