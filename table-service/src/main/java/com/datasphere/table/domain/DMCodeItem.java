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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DMCodeItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	

	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	/** 数据对标元管理代码项信息表 */
	
	
	// 代码项主键
	protected Long codeItemId;
	// 代码编码
	private String codeEncode;
	// 代码项名
	private String codeItemName;
	// 代码值
	private String codeValue;
	// 层级
	private String level;
	// 代码描述
	private String remark;
	// 代码类主键
	private Long codeTypeId;

	public Long getCodeItemId() {
		return codeItemId;
	}
	public void setCodeItemId(Long codeItemId) {
		this.codeItemId = codeItemId;
	}
	public String getCodeEncode() {
		return codeEncode;
	}
	public void setCodeEncode(String codeEncode) {
		this.codeEncode = codeEncode;
	}
	public String getCodeItemName() {
		return codeItemName;
	}
	public void setCodeItemName(String codeItemName) {
		this.codeItemName = codeItemName;
	}
	public String getCodeValue() {
		return codeValue;
	}
	public void setCodeValue(String codeValue) {
		this.codeValue = codeValue;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Long getCodeTypeId() {
		return codeTypeId;
	}
	public void setCodeTypeId(Long codeTypeId) {
		this.codeTypeId = codeTypeId;
	}
	

}
