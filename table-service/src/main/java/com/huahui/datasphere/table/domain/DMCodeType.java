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

public class DMCodeType implements Serializable {

	private static final long serialVersionUID = 1L;
	

	/** 数据对标元管理代码类信息表 */

	// 代码类主键
	protected Long codeTypeId;
	// 代码编码
	protected String codeEncode;
	// 代码类名
	protected String codeTypeName;
	// 代码描述
	protected String remark;
	// 代码状态（是否发布）(IsFlag):1-是<Shi> 0-否<Fou> 
	protected String status;
	// 创建人
	protected String createBy;
	// 日期创建
	protected String createDate;
	// 创建时间
	protected String createTime;
	
	public Long getCodeTypeId() {
		return codeTypeId;
	}
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}
	public String getCodeEncode() {
		return codeEncode;
	}
	public void setCodeEncode(String codeEncode) {
		this.codeEncode = codeEncode;
	}
	public String getCodeTypeName() {
		return codeTypeName;
	}
	public void setCodeTypeName(String codeTypeName) {
		this.codeTypeName = codeTypeName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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
}
