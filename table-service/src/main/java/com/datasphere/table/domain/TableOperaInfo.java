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
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

public class TableOperaInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "信息表ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	
	@Column(name = "preview_sql", columnDefinition = "TEXT")
	@NotNull(message = "预览sql语句不能为空")
	@ApiModelProperty(value="预览sql语句", example = "后置作业")
	private String previewSQL;
	
	
	@Column(name = "search_name", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value="JOIN类型:", example = "")
	private String searchName;
	
	
	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "备注", example = "")
	private String remark;
	
	
	
	@Column(name = "datatable_id", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据表ID不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "数据表ID", example = "")
	private String dataTableId;
	
	
	@Column(name = "execute_sql", columnDefinition = "TEXT")
	@Size(max = 1024)
	@ApiModelProperty(value="执行sql语句", example = "后置作业")
	private String executeSQL;
	
	
	@Column(name = "start_at", columnDefinition = "DATE")
	@NotNull(message = "开始日期不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "开始日期", example = "")
	private Date startAt;
	
	
	@Column(name = "end_at", columnDefinition = "DATE")
	@NotNull(message = "结束日期不能为空")
	@Size(max = 1024)
	@ApiModelProperty(value = "结束日期", example = "")
	
	private String endAt;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getPreviewSQL() {
		return previewSQL;
	}


	public void setPreviewSQL(String previewSQL) {
		this.previewSQL = previewSQL;
	}


	public String getSearchName() {
		return searchName;
	}


	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getDataTableId() {
		return dataTableId;
	}


	public void setDataTableId(String dataTableId) {
		this.dataTableId = dataTableId;
	}


	public String getExecuteSQL() {
		return executeSQL;
	}


	public void setExecuteSQL(String executeSQL) {
		this.executeSQL = executeSQL;
	}


	public Date getStartAt() {
		return startAt;
	}


	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}


	public String getEndAt() {
		return endAt;
	}


	public void setEndAt(String endAt) {
		this.endAt = endAt;
	}
	
	

}
