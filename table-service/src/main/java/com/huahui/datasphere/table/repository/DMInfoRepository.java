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

package com.huahui.datasphere.table.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.huahui.datasphere.table.domain.ColumnStoreRelation;
import com.huahui.datasphere.table.domain.DMBasicInfo;
import com.huahui.datasphere.table.domain.DMDataTable;

public interface DMInfoRepository extends PagingAndSortingRepository<DMBasicInfo, String> {

	
	@Query(value = "SELECT distinct t1.* from DMBasicInfo t1 left join sysUser t2 on t1.userId = t2.userId")
	List<Map<String, Object>> findDMInfo();
	
			
	@Query(value = "SELECT distinct t1.* from DMBasicInfo t1 left join sysUser t2 on t1.userId = t2.userId where depId = : depId")
	List<Map<String, Object>> findDMInfoByDepId(@Param("depId") String depId);
	
	
	@Query(value = "select name,count(name) from DataStoreLayer t1 join DataStoreRelation t2 on t1.id = t2.dslId and t2.dataSource = :dataSource group by name")
	List<Map<String, Object>> findALLDSLByDataSource(@Param("dataSource") String dataSource);
	
	
	@Query(value = "SELECT name,id FROM DMBasicInfo where userId = :userId order by id asc")
	List<DMBasicInfo> findBasicInfoByUserId(@Param("userId") String userId);
	
	@Query(value = "select count(*) from DMBasicInfo where id = :id")
	Long findCountById(@Param("id") String id);
	
	@Query(value = "select count(*) from DMBasicInfo where number = :number")
	Long findCountByNumber(@Param("number") String number);
	
	@Query(value = "select count(*) from DMBasicInfo where name = :name")
	Long findCountByName(@Param("name") String name);
	
	@Query(value = "select count(*) from DMBasicInfo where number = :number and id != :id")
	Long findCountByNumberAndId(@Param("number") String number,@Param("id") String id);
	
	@Query(value = "select count(*) from DMBasicInfo where name = :name and id != :id")
	Long findCountByNameAndId(@Param("name") String name,@Param("id") String id);
	
	@Query(value = "select name from DMBasicInfo where id != :id")
	DMBasicInfo findNameById(@Param("id") String id);
	
}
