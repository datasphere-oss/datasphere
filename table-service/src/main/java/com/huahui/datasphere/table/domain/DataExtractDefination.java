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
 * 数据抽取定义
 */
//@Table(tableName = "data_extraction_def")
public class DataExtractDefination implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public DataExtractDefination(){
		
	}

	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据抽取定义主键:")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "数据抽取定义主键:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	
	
	@Column(name = "is_header", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否需要表头")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "是否需要表头", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String isHeader;
	
	
	@Column(name = "data_file_source", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据文件源头:1-数据库抽取落地 2-原数据格式 3-数据加载格式 ")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据文件源头:1-数据库抽取落地 2-原数据格式 3-数据加载格式 ", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String dataFileSource;
	
	
	
	@Column(name = "extract_encode", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据抽取落地编码")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据文件源头:1-数据库抽取落地 2-原数据格式 3-数据加载格式 ", example = "1-UTF-8<UTF_8> 2-GBK<GBK> 3-UTF-16<UTF_16> 4-GB2312<GB2312> 5-ISO-8859-1<ISO_8859_1>")
	
	private String encoding;
	
	
	@Column(name = "row_separator", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "行分隔符", example = "|")
	private String rowSeparator;
	
	
	@Column(name = "column_separator", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "列分割符:", example = "|")
	private String columnSeparator;
	
	@Column(name = "remark", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "备注", example = "|")
	private String remark;
	
	
	
	
	@Column(name = "db_file_format", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据落地格式")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据落地格式:0-定长 1-非定长 2-CSV 3-SEQUENCEFILE 4-PARQUET 5-ORC ", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String dbFileFormat;
	
	@Column(name = "data_directory", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据落地目录", example = "|")
	
	private String dataDirectory;
	
	
	@Column(name = "file_suffix", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "落地文件后缀名", example = "|")
	
	private String fileSuffix;
	
	
	@Column(name = "table_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "表名ID")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "表名ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private Long tableId;
	
	
	
	@Column(name = "is_archived", nullable = false, updatable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否归档")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否归档", example = "是/否")
	
	private String isArchived;



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getIsHeader() {
		return isHeader;
	}



	public void setIsHeader(String isHeader) {
		this.isHeader = isHeader;
	}



	public String getDataFileSource() {
		return dataFileSource;
	}



	public void setDataFileSource(String dataFileSource) {
		this.dataFileSource = dataFileSource;
	}



	public String getEncoding() {
		return encoding;
	}



	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}



	public String getRowSeparator() {
		return rowSeparator;
	}



	public void setRowSeparator(String rowSeparator) {
		this.rowSeparator = rowSeparator;
	}



	public String getColumnSeparator() {
		return columnSeparator;
	}



	public void setColumnSeparator(String columnSeparator) {
		this.columnSeparator = columnSeparator;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getDbFileFormat() {
		return dbFileFormat;
	}



	public void setDbFileFormat(String dbFileFormat) {
		this.dbFileFormat = dbFileFormat;
	}



	public String getDataDirectory() {
		return dataDirectory;
	}



	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}



	public String getFileSuffix() {
		return fileSuffix;
	}



	public void setFileSuffix(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}



	public Long getTableId() {
		return tableId;
	}



	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}



	public String getIsArchived() {
		return isArchived;
	}



	public void setIsArchived(String isArchived) {
		this.isArchived = isArchived;
	}

	
	
}
