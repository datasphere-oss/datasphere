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

public class CustomTableColumn implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "ID不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "字段域 ID (UUID)", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	@Column(name = "column_name", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "列名")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "Column Name", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String columnName;
	
	
	
	@Column(name = "cn_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "列中文名称")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "Column Name(CN)", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String cnName;
	
	@Column(name = "column_type", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "列类型")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "列类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String columnType;
	
	@Column(name = "column_length", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "列字段长度")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "列字段长度", example = "VARCHAR")
	private String columnLength;
	
	@Column(name = "is_null", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否采集", example = "true")
	private String isNull;
	
	@Column(name = "source_table", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "来源表")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "来源表", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String sourceTable;
	
	@Column(name = "source_column", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "来源列")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "来源列", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String sourceColumn;
	
	
	
	@Column(name = "remark", nullable = true, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "备注", example = "VARCHAR")
	private String remark;
	
	
	@Column(name = "table_id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "表名ID")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "表名ID", example = "VARCHAR")
	private Long tableId;


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getColumnName() {
		return columnName;
	}


	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}


	public String getCnName() {
		return cnName;
	}


	public void setCnName(String cnName) {
		this.cnName = cnName;
	}


	public String getColumnType() {
		return columnType;
	}


	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}


	public String getColumnLength() {
		return columnLength;
	}


	public void setColumnLength(String columnLength) {
		this.columnLength = columnLength;
	}


	public String getIsNull() {
		return isNull;
	}


	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}


	public String getSourceTable() {
		return sourceTable;
	}


	public void setSourceTable(String sourceTable) {
		this.sourceTable = sourceTable;
	}


	public String getSourceColumn() {
		return sourceColumn;
	}


	public void setSourceColumn(String sourceColumn) {
		this.sourceColumn = sourceColumn;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public Long getTableId() {
		return tableId;
	}


	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}
	
	
	
	
	
	

}
