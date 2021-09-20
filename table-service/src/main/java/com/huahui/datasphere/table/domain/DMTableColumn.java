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

import org.acumos.cds.domain.MLPUserCatFavMap;

import io.swagger.annotations.ApiModelProperty;

public class DMTableColumn implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "数据表字段ID不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "TableColumn ID (UUID)", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	@Column(name = "is_primary_key", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否为主键")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "0|1", example = "0|1")
	private String isPrimaryKey;
	
	@Column(name = "column_name", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "列名")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "Column Name", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String columnName;
	
	
	@Column(name = "cn_name", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "列中文名称:")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "Column Name(CN)", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String cnName;
	
	@Column(name = "start_date", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "有效开始日期")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "有效开始日期", example = "2020-01-01")
	private String startDate;
	
	@Column(name = "end_date", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "有效结束日期")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "有效结束日期", example = "2020-01-01")
	private String endDate;
	
	
	@Column(name = "is_crawl", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否采集")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否采集", example = "true")
	private String isCrawl;
	
	@Column(name = "is_new", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否新的")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否新的", example = "true")
	private String isNew;
	
	
	@Column(name = "is_alive", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否活着")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否活着", example = "true")
	private String isAlive;
	
	@Column(name = "column_type", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "列字段类型")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "列字段类型", example = "VARCHAR")
	private String columnType;
	
	
	@Column(name = "table_id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "表名ID")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "表名ID", example = "VARCHAR")
	private String tableId;
	
	
	@Column(name = "remark", nullable = true, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "备注", example = "VARCHAR")
	private String remark;
	
	
	@Column(name = "is_retain", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否保留原字段")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否保留原字段", example = "")
	private String isRetain;
	
	@Column(name = "or", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否保留原字段")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否保留原字段", example = "")
	private String or;
	
	
	@Column(name = "is_generated", nullable = true, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否为变化生成", example = "VARCHAR")
	private String isGenerated;
	
	
	@Column(name = "is_zipper", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "是否为拉链字段")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "是否为拉链字段", example = "")
	private String isZipper;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsPrimaryKey() {
		return isPrimaryKey;
	}
	public void setIsPrimaryKey(String isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
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
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getIsCrawl() {
		return isCrawl;
	}
	public void setIsCrawl(String isCrawl) {
		this.isCrawl = isCrawl;
	}
	public String getColumnType() {
		return columnType;
	}
	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	public String getTableId() {
		return tableId;
	}
	public void setTableId(String tableId) {
		this.tableId = tableId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getIsRetain() {
		return isRetain;
	}
	public void setIsRetain(String isRetain) {
		this.isRetain = isRetain;
	}
	
	
	public String getIsNew() {
		return isNew;
	}
	public void setIsNew(String isNew) {
		this.isNew = isNew;
	}
	public String getIsAlive() {
		return isAlive;
	}
	public void setIsAlive(String isAlive) {
		this.isAlive = isAlive;
	}
	public String getIsGenerated() {
		return isGenerated;
	}
	public void setIsGenerated(String isGenerated) {
		this.isGenerated = isGenerated;
	}
	public String getIsZipper() {
		return isZipper;
	}
	public void setIsZipper(String isZipper) {
		this.isZipper = isZipper;
	}
	public String getOr() {
		return or;
	}
	public void setOr(String or) {
		this.or = or;
	}
	
	
	
}
