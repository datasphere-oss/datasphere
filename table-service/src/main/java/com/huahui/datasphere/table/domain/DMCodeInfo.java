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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class DMCodeInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "INT")
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "Generated", example = "12345")
	private Long id;

	
	@Column(name = "classified", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "编码分类")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "classified", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String classified;
	
	
	@Column(name = "type_value", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "编码类型值")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "type_value", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String typeValue;
	
	@Column(name = "classified_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "编码分类名称")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "编码分类名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String classifiedName;
	
	
	@Column(name = "type_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "编码类型名称")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "编码类型名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String typeName;
	
	
	@Column(name = "remark", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "备注", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String remark;


	
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getClassified() {
		return classified;
	}


	public void setClassified(String classified) {
		this.classified = classified;
	}



	public String getTypeValue() {
		return typeValue;
	}


	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}


	public String getClassifiedName() {
		return classifiedName;
	}


	public void setClassifiedName(String classifiedName) {
		this.classifiedName = classifiedName;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
