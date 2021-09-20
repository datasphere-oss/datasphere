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

package com.huahui.datasphere.common.codes;



public enum DatabaseType {
	
	DATABASE("1","关系型数据库","59","存储层类型"),
	
	HIVE("2","hive","59","存储层类型"),

	MONGODB("6","MongoDB","59","存储层类型"),
	
	S3("7","S3","59","存储层类型");
	

	private final String code;
	private final String value;
	private final String catCode;
	private final String catValue;

	DatabaseType(String code,String value,String catCode,String catValue){
		this.code = code;
		this.value = value;
		this.catCode = catCode;
		this.catValue = catValue;
	}
	public String getCode(){return code;}
	public String getValue(){return value;}
	public String getCatCode(){return catCode;}
	public String getCatValue(){return catValue;}
	public static final String CodeName = "Store_type";

	/**
	 * translate an assigned code value to chinese name
	 */
	public static String ofValueByCode(String code) throws Exception {
		for (DatabaseType typeCode : DatabaseType.values()) {
			if (typeCode.getCode().equals(code)) {
				return typeCode.value;
			}
		}
		throw new Exception("根据"+code+"没有找到对应的代码项");
	}

	/**
	 * translate an assigned code value to object
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static DatabaseType ofEnumByCode(String code) throws Exception {
		for (DatabaseType typeCode : DatabaseType.values()) {
			if (typeCode.getCode().equals(code)) {
				return typeCode;
			}
		}
		throw new Exception("根据"+code+"没有找到对应的代码项");
	}


	/**
	 * get chinese name by category value
	 * @return
	 */
	
	public static String ofCatValue(){
		return DatabaseType.values()[0].getCatValue();
	}

	/**
	 * return category code
	 */

	public static String ofCatCode(){
		return DatabaseType.values()[0].getCatCode();
	}

	@Override
	public String toString() {
		return "There's no need for you to !";
	}
}
