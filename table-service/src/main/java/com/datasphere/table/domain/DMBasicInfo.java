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
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class DMBasicInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "信息表ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	@Column(name = "name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据集市名称不能为空")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "name", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String name;
	
	@Column(name = "desc", nullable = false, columnDefinition = "TEXT")
	@ApiModelProperty(required = true, value = "数据集市描述", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String desc;
	
	
	@Column(name = "storage_path", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据集市存储路径")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "存储路径", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String storagePath;
	
	
	
	@Column(name = "remark", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "备注", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String remark;
	
	
	
	@Column(name = "create_at", columnDefinition = "DATE")
	@NotNull(message = "创建时间不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "创建时间", example = "")
	
	private Date createdAt;
	
	
	
	
	@Column(name = "number", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "number cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "数据库编号", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String number;
	
	
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


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getStoragePath() {
		return storagePath;
	}


	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
}
