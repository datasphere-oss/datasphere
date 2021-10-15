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
import java.math.BigDecimal;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * 数据表字段信息
 */
@Table(tableName = "datatable_field_info")
public class DMDataTableField 
{
	private static final long serialVersionUID = 321566870187324L;
	private transient static final Set<String> __PrimaryKeys;
	public static final String TableName = "datatable_field_info";
	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	/** 数据表字段信息 */
	
	@DocBean(name ="datatable_field_id",value="数据表字段id:",dataType = Long.class,required = true)
	private Long datatable_field_id;
	@DocBean(name ="field_cn_name",value="字段中文名称:",dataType = String.class,required = true)
	private String field_cn_name;
	@DocBean(name ="field_en_name",value="字段英文名称:",dataType = String.class,required = true)
	private String field_en_name;
	@DocBean(name ="field_type",value="字段类型:",dataType = String.class,required = true)
	private String field_type;
	@DocBean(name ="field_desc",value="字段描述:",dataType = String.class,required = false)
	private String field_desc;
	@DocBean(name ="field_seq",value="字段序号:",dataType = Long.class,required = true)
	private Long field_seq;
	@DocBean(name ="remark",value="备注:",dataType = String.class,required = false)
	private String remark;
	@DocBean(name ="field_length",value="字段长度:",dataType = String.class,required = false)
	private String field_length;
	@DocBean(name ="field_process",value="处理方式(ProcessType):1-定值<DingZhi> 2-自增<ZiZeng> 3-映射赋值<YingShe> 4-函数映射<HanShuYingShe> 5-分组映射<FenZhuYingShe> ",dataType = String.class,required = true)
	private String field_process;
	@DocBean(name ="process_mapping",value="映射规则mapping:",dataType = String.class,required = false)
	private String process_mapping;
	@DocBean(name ="datatable_id",value="数据表id:",dataType = Long.class,required = true)
	private Long datatable_id;
	@DocBean(name ="group_mapping",value="分组映射对应规则:",dataType = String.class,required = false)
	private String group_mapping;
	@DocBean(name ="start_date",value="开始日期:",dataType = String.class,required = true)
	private String start_date;
	@DocBean(name ="end_date",value="结束日期:",dataType = String.class,required = true)
	private String end_date;

	/** 取得：数据表字段id */
	public Long getDatatable_field_id(){
		return datatable_field_id;
	}
	/** 设置：数据表字段id */
	public void setDatatable_field_id(Long datatable_field_id){
		this.datatable_field_id=datatable_field_id;
	}
	/** 设置：数据表字段id */
	public void setDatatable_field_id(String datatable_field_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(datatable_field_id)){
			this.datatable_field_id=new Long(datatable_field_id);
		}
	}
	/** 取得：字段中文名称 */
	public String getField_cn_name(){
		return field_cn_name;
	}
	/** 设置：字段中文名称 */
	public void setField_cn_name(String field_cn_name){
		this.field_cn_name=field_cn_name;
	}
	/** 取得：字段英文名称 */
	public String getField_en_name(){
		return field_en_name;
	}
	/** 设置：字段英文名称 */
	public void setField_en_name(String field_en_name){
		this.field_en_name=field_en_name;
	}
	/** 取得：字段类型 */
	public String getField_type(){
		return field_type;
	}
	/** 设置：字段类型 */
	public void setField_type(String field_type){
		this.field_type=field_type;
	}
	/** 取得：字段描述 */
	public String getField_desc(){
		return field_desc;
	}
	/** 设置：字段描述 */
	public void setField_desc(String field_desc){
		this.field_desc=field_desc;
	}
	/** 取得：字段序号 */
	public Long getField_seq(){
		return field_seq;
	}
	/** 设置：字段序号 */
	public void setField_seq(Long field_seq){
		this.field_seq=field_seq;
	}
	/** 设置：字段序号 */
	public void setField_seq(String field_seq){
		if(!fd.ng.core.utils.StringUtil.isEmpty(field_seq)){
			this.field_seq=new Long(field_seq);
		}
	}
	/** 取得：备注 */
	public String getRemark(){
		return remark;
	}
	/** 设置：备注 */
	public void setRemark(String remark){
		this.remark=remark;
	}
	/** 取得：字段长度 */
	public String getField_length(){
		return field_length;
	}
	/** 设置：字段长度 */
	public void setField_length(String field_length){
		this.field_length=field_length;
	}
	/** 取得：处理方式 */
	public String getField_process(){
		return field_process;
	}
	/** 设置：处理方式 */
	public void setField_process(String field_process){
		this.field_process=field_process;
	}
	/** 取得：映射规则mapping */
	public String getProcess_mapping(){
		return process_mapping;
	}
	/** 设置：映射规则mapping */
	public void setProcess_mapping(String process_mapping){
		this.process_mapping=process_mapping;
	}
	/** 取得：数据表id */
	public Long getDatatable_id(){
		return datatable_id;
	}
	/** 设置：数据表id */
	public void setDatatable_id(Long datatable_id){
		this.datatable_id=datatable_id;
	}
	/** 设置：数据表id */
	public void setDatatable_id(String datatable_id){
		if(!fd.ng.core.utils.StringUtil.isEmpty(datatable_id)){
			this.datatable_id=new Long(datatable_id);
		}
	}
	/** 取得：分组映射对应规则 */
	public String getGroup_mapping(){
		return group_mapping;
	}
	/** 设置：分组映射对应规则 */
	public void setGroup_mapping(String group_mapping){
		this.group_mapping=group_mapping;
	}
	/** 取得：开始日期 */
	public String getStart_date(){
		return start_date;
	}
	/** 设置：开始日期 */
	public void setStart_date(String start_date){
		this.start_date=start_date;
	}
	/** 取得：结束日期 */
	public String getEnd_date(){
		return end_date;
	}
	/** 设置：结束日期 */
	public void setEnd_date(String end_date){
		this.end_date=end_date;
	}
}
