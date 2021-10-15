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
import java.util.Date;
import java.sql.Timestamp;

public class DMDataTable implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected String id;
	protected String dataMartId;
	// 数据表中文名称:
	protected String cnName;
	// 数据表英文名称
	protected String enName;
	// 数据表描述
	protected String desc;
	// 创建日期
	protected Date createDate;
	// 创建时间
	protected Timestamp createTime;
	// 到期时间
	protected Date expireDate;
	// 生命周期
	protected String lifecycle;
	// DDL最后变更日期
	protected Date ddlLastUpdatedDate;
	// DDL最后变更时间
	protected Timestamp ddlLastUpdatedTime;
	// 数据最后变更日期
	protected Date dataLastUpdatedDate;
	// 数据最后变更时间
	protected Timestamp dataLastUpdatedTime;
	// 数据源大小
	protected int sourceSize;
	// ETL执行日期
	protected Date etlDate;
	// SQL执行引擎: 数据库、Hive、Spark
	protected String sqlEngine;
	// 抽取类型：2-追加 3-替换 4-全量拉链 5-增量拉链
	protected String ingestionType;
	// 数据表存储方式: 0-数据表 1-数据视图
	protected String tableStorage;
	
	protected String remark;
	
	protected String repeatFlag;
	
	
	// 预分区
	protected String prePartition;
	// 集市分类Id
	protected String categoryId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDataMartId() {
		return dataMartId;
	}
	public void setDataMartId(String dataMartId) {
		this.dataMartId = dataMartId;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	
	public String getLifecycle() {
		return lifecycle;
	}
	public void setLifecycle(String lifecycle) {
		this.lifecycle = lifecycle;
	}
	public int getSourceSize() {
		return sourceSize;
	}
	public void setSourceSize(int sourceSize) {
		this.sourceSize = sourceSize;
	}
	public Date getEtlDate() {
		return etlDate;
	}
	public void setEtlDate(Date etlDate) {
		this.etlDate = etlDate;
	}
	public String getSqlEngine() {
		return sqlEngine;
	}
	public void setSqlEngine(String sqlEngine) {
		this.sqlEngine = sqlEngine;
	}
	
	public String getTableStorage() {
		return tableStorage;
	}
	public void setTableStorage(String tableStorage) {
		this.tableStorage = tableStorage;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPrePartition() {
		return prePartition;
	}
	public void setPrePartition(String prePartition) {
		this.prePartition = prePartition;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	public Date getDdlLastUpdatedDate() {
		return ddlLastUpdatedDate;
	}
	public void setDdlLastUpdatedDate(Date ddlLastUpdatedDate) {
		this.ddlLastUpdatedDate = ddlLastUpdatedDate;
	}
	public Timestamp getDdlLastUpdatedTime() {
		return ddlLastUpdatedTime;
	}
	public void setDdlLastUpdatedTime(Timestamp ddlLastUpdatedTime) {
		this.ddlLastUpdatedTime = ddlLastUpdatedTime;
	}
	public Date getDataLastUpdatedDate() {
		return dataLastUpdatedDate;
	}
	public void setDataLastUpdatedDate(Date dataLastUpdatedDate) {
		this.dataLastUpdatedDate = dataLastUpdatedDate;
	}
	public Timestamp getDataLastUpdatedTime() {
		return dataLastUpdatedTime;
	}
	public void setDataLastUpdatedTime(Timestamp dataLastUpdatedTime) {
		this.dataLastUpdatedTime = dataLastUpdatedTime;
	}
	public String getIngestionType() {
		return ingestionType;
	}
	public void setIngestionType(String ingestionType) {
		this.ingestionType = ingestionType;
	}
	public String getRepeatFlag() {
		return repeatFlag;
	}
	public void setRepeatFlag(String repeatFlag) {
		this.repeatFlag = repeatFlag;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getEnName() {
		return enName;
	}
	public void setEnName(String enName) {
		this.enName = enName;
	}	
	
	
}
