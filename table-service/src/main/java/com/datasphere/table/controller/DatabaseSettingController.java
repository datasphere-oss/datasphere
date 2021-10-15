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

package com.datasphere.table.controller;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.AbstractController;

import com.datasphere.table.domain.DatabaseSetting;
import com.datasphere.table.repository.DatabaseSetRepository;
import com.datasphere.table.utils.ObjectId;
import com.datasphere.table.utils.StringUtil;
import com.datasphere.table.utils.Validator;
import com.huahui.datasphere.common.codes.DatabaseType;
import com.huahui.datasphere.common.codes.IngestionType;
import com.huahui.datasphere.commons.CheckParam;
import com.huahui.datasphere.commons.utils.Constant;
import com.huahui.datasphere.commons.utils.key.PrimayKeyGener;

//@DocClass(desc = "数据库脱敏管理类", author = "Mr.Lee", createdate = "2020-08-17 14:32")
public class DatabaseSettingController extends AbstractController {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private static DatabaseSetRepository databaseSettingRepository;
	
	@Method(desc = "新增数据库脱敏信息", logicStep = "1: 根据用户和脱敏代理端信息进行关联查询上次为配置完成的任务信息,如果没有则说明是新增")
	@Param(name = "source_id", range = "不可为空", desc = "数据源ID编号")
	@Param(name = "agent_id", range = "不可为空", desc = "Agent ID编号")
	@Return(desc = "返回数据库采集配置信息", range = "可以为空,为空表示首次配置")
	public Result getInitDatabase(long source_id, long agent_id) {

		
		
//		return Dbo.queryResult(
//			"SELECT t1.*, t2.classify_id, t2.classify_num, "
//				+ " t2.classify_name, t2.remark "
//				+ " FROM "
//				+ Database_set.TableName
//				+ " t1 "
//				+ " JOIN "
//				+ Collect_job_classify.TableName
//				+ " t2 ON "
//				+ " t1.classify_id = t2.classify_id  JOIN "
//				+ Agent_info.TableName
//				+ " ai ON t1.agent_id = ai.agent_id "
//				+ "WHERE  t1.is_sendok = ? AND ai.agent_type = ? AND ai.user_id = ? "
//				+ "AND ai.source_id = ? AND ai.agent_id = ? AND t1.collect_type = ?",
//			IsFlag.Fou.getCode(),
//			AgentType.ShuJuKu.getCode(),
//			getUserId(),
//			source_id,
//			agent_id, IngestionType.ShuJuKuCaiJi.getCode());
		return databaseSettingRepository.initDatabase();
		
	}

	@Method(
		desc = "根据数据库脱敏任务ID查询数据库脱敏信息",
		logicStep =
			""
				+ "1、在数据库设置表(database_set)中，根据databaseId判断是否查询到数据，如果查询不到，抛异常给前端"
				+ "2、根据采集任务的ID,查询数据库采集信息,分类信息"
				+ "3: 根据任务ID信息查找表对应的存储层配置信息,这里的一次任务只能对应一个存储层,如果没有查询到结果集,则抛出异常信息")
	@Param(name = "databaseId", desc = "源系统数据库设置表主键", range = "不为空")
	@Return(desc = "贴源配置数据信息集合", range = "不能为空")
	public Result editorDatabase(long databaseId) {
		// 1、在数据库设置表(database_set)中，根据databaseId判断是否查询到数据，如果查询不到，抛异常给前端
		
		long countNum = databaseSettingRepository.countByAgentId();
//		long countNum = Dbo.queryNumber(
//			"SELECT count(1) "
//				+ " FROM "
//				+ Database_set.TableName
//				+ " das "
//				+ " JOIN "
//				+ Agent_info.TableName
//				+ " ai ON ai.agent_id = das.agent_id "
//				+ " WHERE das.database_id = ? AND ai.user_id = ? AND das.is_sendok = ?",
//			databaseId,
//			getUserId(), IsFlag.Shi.getCode())
//			.orElseThrow(() -> new BusinessException("SQL查询错误"));
		if (countNum != 1) {
			CheckParam.throwErrorMsg("根据用户ID(%s),未找到任务ID为(%s)的数据信息", getUserId(), databaseId);
		}
		//2、根据采集任务的ID,查询贴源配置信息,分类信息
		
		return databaseSettingRepository.findDatabaseByIngestionType();
//		return Dbo.queryResult(
//			"SELECT t1.*, t2.classify_id, t2.classify_num,t2.classify_name, t2.remark "
//				+ " FROM "
//				+ Database_set.TableName
//				+ " t1 "
//				+ " JOIN "
//				+ Collect_job_classify.TableName
//				+ " t2 ON "
//				+ " t1.classify_id = t2.classify_id  WHERE database_id = ? AND t1.is_sendok = ? AND t1.collect_type = ?",
//			databaseId, IsFlag.Shi.getCode(), CollectType.ShuJuKuCaiJi.getCode());
	}

	@Method(desc = "保存数据库采集配置信息", logicStep = ""
		+ "1: 校验实体每个必须字段的数据不能为空 "
		+ "2: 检查任务名称不能重复 "
		+ "3: 检查作业编号不能重复"
		+ "4: 返回此次任务的采集ID")
	@Param(name = "databaseSet", desc = "贴源数据的实体信息", range = "不可以为空", isBean = true)
	@Return(desc = "返回此次保存后生成的任务ID", range = "不可为空")
	public String saveDatabaseInfo(DatabaseSetting databaseSet) {
		//1: 校验实体每个必须字段的数据不能为空
		verifyDatabaseSetEntity(databaseSet);
		//2: 检查任务名称不能重复
		
		
		long taskCount = databaseSettingRepository.countByTaskName();
//		long val =
//			Dbo.queryNumber(
//				"SELECT COUNT(1) FROM " + Database_set.TableName + " WHERE task_name = ?",
//				databaseSet.getTask_name())
//				.orElseThrow(() -> new BusinessException("SQL查询错误"));
		
		
		
		if (taskCount != 0) {
			CheckParam.throwErrorMsg("任务名称(%s)重复，请重新定义任务名称", databaseSet.getTaskName());
		}
		//3: 检查作业编号不能重复
		if (StringUtil.isNotBlank(databaseSet.getDatabaseNumber())) {
			
			long dbNumberCount = databaseSettingRepository.countByDatabaseNumber();
//			val =
//				Dbo.queryNumber(
//					"SELECT COUNT(1) FROM " + Database_set.TableName + " WHERE database_number = ?",
//					databaseSet.getDatabase_number())
//					.orElseThrow(() -> new BusinessException("SQL查询错误"));
			if (dbNumberCount != 0) {
				CheckParam.throwErrorMsg("作业编号(%s)重复，请重新定义作业编号", databaseSet.getDatabaseNumber());
			}
		}
//		PrimayKeyGener pkg = new PrimayKeyGener();
		ObjectId obj =  new ObjectId();
		databaseSet.setDatabaseId(obj.toHexString());
		databaseSet.setIngestionType(IngestionType.ShuJuKuCaiJi.getCode());
		databaseSet.setDbAgent(IsFlag.Fou.getCode());
		databaseSet.setIsSendok(IsFlag.Fou.getCode());
		databaseSet.setCpOr(Constant.DEFAULT_TABLE_CLEAN_ORDER.toJSONString());
		
		databaseSettingRepository.save(databaseSet);
		
//		databaseSet.add(Dbo.db());
		// 4: 返回此次任务的采集ID
		return databaseSet.getDatabaseId();
	}

	@Method(desc = "保存更新的数据库采集配置信息", logicStep = ""
		+ "1: 校验实体每个必须字段的数据不能为空 "
		+ "2: 检查任务名称不能重复 "
		+ "3: 检查作业编号不能重复"
		+ "4: 更新此次任务"
		+ "5: 返回此次任务的采集ID")
	@Param(name = "databaseSet", desc = "贴源数据的实体信息", range = "不可以为空", isBean = true)
	@Return(desc = "返回此次保存后生成的任务ID", range = "不可为空")
	public String updateDatabaseInfo(DatabaseSetting databaseSet) {

		Validator.notNull(databaseSet.getDatabaseId(), "更新时未获取到主键ID信息");
		//1: 校验实体每个必须字段的数据不能为空
		verifyDatabaseSetEntity(databaseSet);
		//2: 检查任务名称不能重复
		
		
		long count = databaseSettingRepository.countByTaskAndDBId();
//		long val =
//			Dbo.queryNumber(
//				"SELECT COUNT(1) from " + Database_set.TableName + " WHERE task_name = ? AND database_id != ?",
//				databaseSet.getTask_name(), databaseSet.getDatabase_id())
//				.orElseThrow(() -> new BusinessException("SQL查询错误"));
		
		
		if (count != 0) {
			CheckParam.throwErrorMsg("任务名称(%s)重复，请重新定义任务名称", databaseSet.getTaskName());
		}
		// 3: 检查作业编号不能重复
		if (StringUtil.isNotBlank(databaseSet.getDatabaseNumber())) {
//			val =
//				Dbo.queryNumber(
//					"SELECT COUNT(1) from " + Database_set.TableName + " WHERE database_number = ? AND database_id != ?",
//					databaseSet.getDatabase_number(), databaseSet.getDatabase_id())
//					.orElseThrow(() -> new BusinessException("SQL查询错误"));
			
			count = databaseSettingRepository.countByDBNumAndDBId();
			if (count != 0) {
				CheckParam.throwErrorMsg("作业编号(%s)重复，请重新定义作业编号", databaseSet.getDatabase_number());
			}
		}
		//4: 更新此次任务
		try {
			databaseSettingRepository.save(databaseSet);
//			databaseSet.update(Dbo.db());
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		//5: 返回此次任务的采集ID
		return databaseSet.getDatabaseId();
	}


	private void verifyDatabaseSetEntity(DatabaseSetting databaseSet) {
		// 1、校验database_type不能为空，并且取值范围必须在DatabaseType代码项中
		Validator.notBlank(databaseSet.getDatabaseType(), "数据库类型不能为空");
		DatabaseType.ofEnumByCode(databaseSet.getDatabaseType());
		// 2、校验classify_id不能为空
		Validator.notNull(databaseSet.getClassifiedId(), "分类信息不能为空");
//		// 3、校验作业编号不为能空，并且长度不能超过10
//		Validator.notBlank(databaseSet.getDatabase_number(), "作业编号不为能空，并且长度不能超过10");
		// 4、校验数据库驱动不能为空
		Validator.notBlank(databaseSet.getDriver(), "数据库驱动不能为空");
//		// 5、校验数据库名称不能为空
		Validator.notBlank(databaseSet.getDatabaseName(), "数据库名称不能为空");
//		// 6、校验数据库IP不能为空
		Validator.notBlank(databaseSet.getDatabaseIp(), "数据库IP地址不能为空");
//		// 7、校验数据库端口号不能为空
		Validator.notBlank(databaseSet.getDatabasePort(), "数据库端口号不能为空");
		// 8、校验用户名不能为空
		Validator.notBlank(databaseSet.getUserName(), "数据库用户名不能为空");
		// 9、校验数据库密码不能为空
		Validator.notBlank(databaseSet.getPassword(), "数据库密码不能为空");
		// 10、校验JDBCURL不能为空
		Validator.notBlank(databaseSet.getJdbcUrl(), "数据库连接URL不能为空");
		// 11、校验agent_id不能为空
		Validator.notNull(databaseSet.getAgentId(), "必须关联Agent信息不能为空");
	}
}
