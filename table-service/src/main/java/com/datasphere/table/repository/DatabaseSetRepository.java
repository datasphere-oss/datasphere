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

package com.datasphere.table.repository;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.datasphere.table.domain.Classified;
import com.datasphere.table.domain.ColumnStoreRelation;
import com.datasphere.table.domain.DMDataTable;
import com.datasphere.table.domain.DatabaseSetting;

public interface DatabaseSetRepository extends PagingAndSortingRepository<DatabaseSetting, String> {

	
	
	@Query(value = "SELECT t2.classify_num,t4.datasource_number FROM DatabaseSetting t1 JOIN IngrestJobClassified t2 ON t1.classify_id = t2.classify_id JOIN AgentInfo t3 ON t1.agent_id = t3.agent_id JOIN DataSource t4 ON t3.source_id = t4.source_id WHERE t1.database_id = :database_id")
	
	Classified findClassifiedByDatabaseId();
	
	
	@Query(value = "SELECT t1.*, t2.classify_id, t2.classify_num, t2.classify_name, t2.remark FROM DatabaseSetting t1  JOIN IngestionJobClassfied t2 ON t1.classify_id = t2.classify_id  JOIN  AgentInfo ai ON t1.agent_id = ai.agent_id WHERE  t1.is_sendok = :isSendok AND ai.agent_type = :AgentType AND ai.user_id = :userId AND ai.source_id = :sourceId AND ai.agent_id = :agentId AND t1.collect_type = :ingestionType")
	ResultSet initDatabase();
	
	@Query(value = "SELECT t1.*, t2.classify_id, t2.classify_num,t2.classify_name, t2.remark FROM  Database_set.TableName t1  JOIN  Collect_job_classify.TableName t2 ON  t1.classify_id = t2.classify_id  WHERE database_id = ? AND t1.is_sendok = ? AND t1.collect_type = ")
	ResultSet findDatabaseByIngestionType();

	
	@Query(value ="SELECT count(1) FROM DatabaseSetting das JOIN  AgentInfo ai ON ai.agent_id = das.agent_id  WHERE das.database_id = :databaseId AND ai.user_id = :userId AND das.is_sendok = :isSendok")
	long countByAgentId();
	
	
	@Query(value ="SELECT COUNT(1) FROM DatabaseSetting WHERE task_name = ?")
	long countByTaskName();
	
	
	@Query(value ="SELECT COUNT(1) FROM DatabaseSetting WHERE database_number = ?")
	long countByDatabaseNumber();
	
	@Query(value ="SELECT COUNT(1) from DatabaseSetting WHERE task_name = ? AND database_id != ?")
	long countByTaskAndDBId();
	
	
	@Query(value ="SELECT COUNT(1) from \" + Database_set.TableName + \" WHERE database_number = ? AND database_id != ?")
	long countByDBNumAndDBId();
	
	
	
}
