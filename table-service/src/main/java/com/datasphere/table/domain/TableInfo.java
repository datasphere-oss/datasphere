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
/**Auto Created by VBScript Do not modify!*/
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
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
 * 数据库对应表
 */
@Table(name = "table_info")
public class TableInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "表名ID")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "表名ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	
	@Column(name = "table_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "表名不能为空")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "name", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String tableName;
	
	@Column(name = "cn_name", nullable = true, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "表中文名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String cnName;
	
	
	
	
	
	@Column(name = "table_count", nullable = false, columnDefinition = "BIGINT")
	@ApiModelProperty(required = true, value = "记录数", example = "1000000")
	
	private String tableCount;
	
	
	
	@Column(name = "source_table_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "源表ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String sourceTableId;
	
	
	@Column(name = "start_at", columnDefinition = "DATE")
	@NotNull(message = "开始日期不能为空")
	@ApiModelProperty(value = "开始日期", example = "")
	private Date startAt;
	
	
	@Column(name = "end_at", columnDefinition = "DATE")
	@NotNull(message = "结束日期不能为空")
	@ApiModelProperty(value = "结束日期", example = "")
	private String endAt;
	

	
	
	@Column(name = "sql", columnDefinition = "TEXT")
	@ApiModelProperty(value="自定义sql语句:", example = "自定义sql语句")
	
	private String sql;
	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "备注", example = "")
	private String remark;
	
	
	@Column(name = "is_user_defined", columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(value = "是否SQL抽取", example = "")
	private String isUserDefined;
	
	
	
	@Column(name = "database_id", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据库id")
	@Size(max = 1024)
	@ApiModelProperty(value = "数据库id", example = "")
	private String databaseId;
	
	
	@Column(name = "ti_or", columnDefinition = "VARCHAR(1024)")
	@ApiModelProperty(value = "清洗顺序", example = "")
	private String tiOr;
	
	
	@Column(name = "is_md5", columnDefinition = "CHAR(36)")
	@NotNull(message = "是否使用MD5")
	@Size(max = 36)
	@ApiModelProperty(value = "是否使用MD5", example = "")
	private String isMd5;
	
	@Column(name = "is_register", columnDefinition = "CHAR(36)")
	@NotNull(message = "是否仅登记")
	@Size(max = 36)
	@ApiModelProperty(value = "是否仅登记", example = "")
	private String isRegister;
	
	
	@Column(name = "is_parallel", columnDefinition = "CHAR(36)")
	@NotNull(message = "是否并行抽取")
	@Size(max = 36)
	@ApiModelProperty(value = "是否并行抽取", example = "")
	private String isParallel;
	
	@Column(name = "page_sql", columnDefinition = "TEXT")
	@ApiModelProperty(value="分页sql", example = "分页sql")
	private String pageSql;
	
	
	@Column(name = "page_parallels", columnDefinition = "INT")
	@ApiModelProperty(value = "分页并行数", example = "")
	private Integer pageParallels;
	
	
	@Column(name = "increment_num", columnDefinition = "INT")
	@ApiModelProperty(value = "每天数据增量", example = "")
	private Integer incrementNum;
	
	
	@Column(name = "unload_type", columnDefinition = "CHAR(36)")
	@Size(max = 36)
	@ApiModelProperty(value = "落地文件-卸数方式", example = "")
	
	private String unloadType;
	
	
	@Column(name = "is_customize_sql", columnDefinition = "TEXT")
	@NotNull(message = "是否并行抽取中的自定义")
	@ApiModelProperty(value="是否并行抽取中的自定义", example = "是否并行抽取中的自定义")
	
	private String isCustomizeSql;
	
	
	
	@Column(name = "crawl_time", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据获取时间")
	@Size(max = 1024)
	@ApiModelProperty(value="数据获取时间", example = "数据获取时间")
	
	private Timestamp crawlTime;



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
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





	public String getTableCount() {
		return tableCount;
	}



	public void setTableCount(String tableCount) {
		this.tableCount = tableCount;
	}



	public String getSourceTableId() {
		return sourceTableId;
	}



	public void setSourceTableId(String sourceTableId) {
		this.sourceTableId = sourceTableId;
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



	public String getSql() {
		return sql;
	}



	public void setSql(String sql) {
		this.sql = sql;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getIsUserDefined() {
		return isUserDefined;
	}



	public void setIsUserDefined(String isUserDefined) {
		this.isUserDefined = isUserDefined;
	}



	public String getDatabaseId() {
		return databaseId;
	}



	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}



	public String getTiOr() {
		return tiOr;
	}



	public void setTiOr(String tiOr) {
		this.tiOr = tiOr;
	}



	public String getIsMd5() {
		return isMd5;
	}



	public void setIsMd5(String isMd5) {
		this.isMd5 = isMd5;
	}



	public String getIsRegister() {
		return isRegister;
	}



	public void setIsRegister(String isRegister) {
		this.isRegister = isRegister;
	}



	public String getIsParallel() {
		return isParallel;
	}



	public void setIsParallel(String isParallel) {
		this.isParallel = isParallel;
	}



	public String getPageSql() {
		return pageSql;
	}



	public void setPageSql(String pageSql) {
		this.pageSql = pageSql;
	}



	public Integer getPageParallels() {
		return pageParallels;
	}



	public void setPageParallels(Integer pageParallels) {
		this.pageParallels = pageParallels;
	}



	public Integer getIncrementNum() {
		return incrementNum;
	}



	public void setIncrementNum(Integer incrementNum) {
		this.incrementNum = incrementNum;
	}



	public String getUnloadType() {
		return unloadType;
	}



	public void setUnloadType(String unloadType) {
		this.unloadType = unloadType;
	}



	public String getIsCustomizeSql() {
		return isCustomizeSql;
	}



	public void setIsCustomizeSql(String isCustomizeSql) {
		this.isCustomizeSql = isCustomizeSql;
	}



	public Timestamp getCrawlTime() {
		return crawlTime;
	}



	public void setCrawlTime(Timestamp crawlTime) {
		this.crawlTime = crawlTime;
	}

	
}
