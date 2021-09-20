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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DMCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private transient static final Set<String> __PrimaryKeys;
	
	/** 集市分类信息 */
	static {
		Set<String> __tmpPKS = new HashSet<>();
		__tmpPKS.add("category_id");
		__PrimaryKeys = Collections.unmodifiableSet(__tmpPKS);
	}
	
	// 集市分类id
	protected String id;
	// 分类名称
	protected String name;
	
	protected String desc;
	// 创建日期
	protected String createDate;
	// 创建时间
	protected String createTime;
	// 创建人
	protected String createBy;
	// 分类编号
	protected String number;
	// 分类序号
	protected String sequence;
	// 父分类ID
	protected String parentId;
	
	// 父分类名称
	protected String parentName;
	
		
	// 数据集市ID
	protected String dataMartId;
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
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getDataMartId() {
		return dataMartId;
	}
	public void setDataMartId(String dataMartId) {
		this.dataMartId = dataMartId;
	}	

}
