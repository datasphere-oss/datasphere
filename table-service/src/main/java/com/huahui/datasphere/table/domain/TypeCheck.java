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
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Collections;

/**
 * 存储层数据类型对照表
 */
@Table(name = "type_contrast")
public class TypeCheck 
{
	private static final long serialVersionUID = 321566870187324L;

	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	
	@Id
	@Column(name = "id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "类型对照主键不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "类型对照主键", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	@Column(name = "source_type", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "源表数据类型")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "源表数据类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String sourceType;
	
	
	@Column(name = "target_type", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "目标表数据类型")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "目标表数据类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String targetType;
	
	
	@Column(name = "remark", nullable = true, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "备注", example = "VARCHAR")
	
	private String remark;
	
	
	@Column(name = "dtcs_id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "类型对照ID:")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "类型对照ID:", example = "VARCHAR")
	
	private Long dtcsId;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getSourceType() {
		return sourceType;
	}


	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}


	public String getTargetType() {
		return targetType;
	}


	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Long getDtcsId() {
		return dtcsId;
	}


	public void setDtcsId(Long dtcsId) {
		this.dtcsId = dtcsId;
	}


	

	
}
