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
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class DMTableCycle implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	/** 数据库采集周期 */
	
	
	@Id
	@Column(name = "id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "ID不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "周期ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String id;
	
	@Id
	@Column(name = "table_id", nullable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "ID不能为空")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "表名ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String tableId;
	
	@Id
	@Column(name = "interval_time", nullable = false, columnDefinition = "INT")
	@NotNull(message = "频率间隔时间不能为空")
	@ApiModelProperty(required = true, value = "频率间隔时间(秒)", example = "5")
	private Long intervalTime;
	
	@CreationTimestamp
	@Column(name = "over_date", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
	// REST clients should not send this property
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "结束日期:", example = "2018-12-16T12:34:56.789Z")
	private Instant overDate;
	
	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(example = "备注")
	private String remark;
}
