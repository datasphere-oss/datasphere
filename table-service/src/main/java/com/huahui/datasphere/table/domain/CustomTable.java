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

public class CustomTable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "表ID不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "自定义表ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	@Column(name = "table_space", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "表空间")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "name", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String tableSpace;
	
	@Column(name = "table_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "表名不能为空")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "name", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String tableName;
	
	@Column(name = "cn_name", nullable = true, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "表中文名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String cnName;
	
	
	
	@Column(name = "remark", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "备注", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String remark;
	
	
	
	@Column(name = "create_at", columnDefinition = "DATE")
	@NotNull(message = "创建时间不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "创建时间", example = "")
	private String createdAt;
	
	@Column(name = "end_at", columnDefinition = "DATE")
	@NotNull(message = "结束时间不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "结束时间", example = "")
	private String endAt;
	
	
	
	@Column(name = "is_trace", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "is_trace cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否数据溯源", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String isTrace;
	
	
	@Id
	@Column(name = "user_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "use_id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "用户ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private Long userId;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTableSpace() {
		return tableSpace;
	}


	public void setTableSpace(String tableSpace) {
		this.tableSpace = tableSpace;
	}


	public String getTableName() {
		return tableName;
	}


	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public String getCnName() {
		return cnName;
	}


	public void setCnName(String cnName) {
		this.cnName = cnName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


	public String getEndAt() {
		return endAt;
	}


	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}


	public String getIsTrace() {
		return isTrace;
	}


	public void setIsTrace(String isTrace) {
		this.isTrace = isTrace;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
	
}
