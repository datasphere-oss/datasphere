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

package com.huahui.datasphere.system;

public enum RoleType {
	
	Administrator("00","系统管理员","17","角色类型"),
	
	IngestionManager("01","采集管理","17","角色类型"),
	IngestionOperator("02","数据采集","17","角色类型"),
	
	QueryOperator("03","数据查询","17","角色类型"),
	
	ScheduleOperator("04","作业调度","17","角色类型"),
	
	TaskOperator("05","作业操作员","17","角色类型"),
	
//	/**数据可视化管理<ShuJuKSHGuanLiYuan>  */
//	ShuJuKSHGuanLiYuan("06","数据可视化管理","17","用户类型"),
//	/**可视化数据源<ShuJuKSHSJY>  */
//	ShuJuKSHSJY("07","可视化数据源","17","用户类型"),
//	/**数据可视化分析<ShuJuKSHBianJI>  */
//	ShuJuKSHBianJI("08","数据可视化分析","17","用户类型"),
//	/**数据可视化查看<ShuJuKSHChaKan>  */
//	ShuJuKSHChaKan("09","数据可视化查看","17","用户类型"),
	
	
	MonitorManager("10","监控管理","17","角色类型"),
	
	RESTfulManager("11","服务接口管理","17","角色类型"),
	
	RESTfulOperator("12","服务接口用户","17","角色类型"),
	
	ETLOperator("14","数据加工","17","角色类型"),
	
	MLOperator("16","机器学习工作台","17","角色类型"),
	
//	/**机器学习业务<JiQiXueXiYongHu>  */
//	JiQiXueXiYongHu("17","机器学习业务","17","角色类型"),
//	/**流数据管理<LiuShuJuGuanLiYuan>  */
//	LiuShuJuGuanLiYuan("18","流数据管理","17","角色类型"),
//	
//	/**流数据生产<LiuShuJuShengChanYongHu>  */
//	LiuShuJuShengChanYongHu("19","流数据生产","17","角色类型"),
	
	DatabaseManager("19","数据库管理","17","角色类型"),
	DatabaseOperator("20","数据库管理","17","角色类型"),
	
	
	
	
	ReportManager("21","报表创建","17","角色类型"),
	ReportOperator("22","报表查看","17","角色类型"),
	
//	/**流数据消费<LiuShuJuXiaoFeiYongHu>  */
//	LiuShuJuXiaoFeiYongHu("23","流数据消费","17","角色类型"),
	
	
	WareHouseManager("23","数据管控","17","角色类型"),
	WareHouseOperator("24","数据管控","17","角色类型"),

//	/**自主分析管理<ZiZhuFenXiGuanLi>  */
//	ZiZhuFenXiGuanLi("25","自主分析管理","17","角色类型"),
	
//	/**资源管理<ZiYuanGuanLi>  */
//	ZiYuanGuanLi("27","资源管理","17","角色类型"),
//	/**自主分析操作<ZiZhuFenXiCaoZuo>  */
//	
//	ZiZhuFenXiCaoZuo("26","自主分析操作","17","角色类型"),
	

	GovernManager("37","数据治理管理","17","角色类型"),
	
//	/**表结构对标<BiaoJieGouDuiBiao>  */
//	BiaoJieGouDuiBiao("38","表结构对标","17","角色类型"),
//	/**表数据对标<BiaoShuJuDuiBiao>  */
//	BiaoShuJuDuiBiao("39","表数据对标","17","角色类型"),
//	
//	/**标准元查看<BiaoZhunYuanChaKan>  */
//	BiaoZhunYuanChaKan("55","标准元查看","17","角色类型"),
	
	
	UserManager("99","用户管理","17","角色类型"),
	DeptManager("98","部门管理","17","角色类型"),
	
//	/**系统参数管理<XiTongCanShuGuanLi>  */
//	XiTongCanShuGuanLi("97","系统参数管理","17","角色类型"),
	

	SurveyManager("96","数据调研","17","角色类型"),
	VersionManager("94","版本管理","17","角色类型");

	private final String code;
	private final String value;
	private final String catCode;
	private final String catValue;

	RoleType(String code,String value,String catCode,String catValue){
		this.code = code;
		this.value = value;
		this.catCode = catCode;
		this.catValue = catValue;
	}
	public String getCode(){return code;}
	public String getValue(){return value;}
	public String getCatCode(){return catCode;}
	public String getCatValue(){return catValue;}
	public static final String CodeName = "Role";

	/**根据指定的代码值转换成中文名字
	* @param code   本代码的代码值
	* @return
	 * @throws Exception 
	*/
	public static String ofValueByCode(String code) throws Exception {
		for (RoleType typeCode : RoleType.values()) {
			if (typeCode.getCode().equals(code)) {
				return typeCode.value;
			}
		}
		throw new Exception("根据"+code+"没有找到对应的代码项");
	}

	
	public static RoleType ofEnumByCode(String code) throws Exception {
		for (RoleType typeCode : RoleType.values()) {
			if (typeCode.getCode().equals(code)) {
				return typeCode;
			}
		}
		throw new Exception("根据"+code+"没有找到对应的代码项");
	}

	public static String ofCatValue(){
		return RoleType.values()[0].getCatValue();
	}


	public static String ofCatCode(){
		return RoleType.values()[0].getCatCode();
	}


	@Override
	public String toString() {
		return "There's no need for you to !";
	}
}
