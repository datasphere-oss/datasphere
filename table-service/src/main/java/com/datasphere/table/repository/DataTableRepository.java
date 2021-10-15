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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.datasphere.table.domain.ColumnStoreRelation;
import com.datasphere.table.domain.DMDataTable;

public interface DataTableRepository extends PagingAndSortingRepository<DMDataTable, String> {

	@Query(value = "SELECT * FROM DMDataTable dt join TableRelationStore trs on dt.id = trs.tableId where categoryId = :categoryId")
	List<Map<String, Object>> findDMLTableByCategoryId(@Param("dataMartId") String dataMartId);
	
	
//	"SELECT * FROM ColumnRelationStore crs JOIN " + Data_store_layer_added.TableName + " dsl_add ON dcol_rs" + ".dslad_id=dsl_add.dslad_id " +
//	" WHERE col_id=?", dqTableColumn.getField_id()
	
	
	@Query(value = "SELECT * FROM DMDataTable dt join TableRelationStore trs on dt.id = trs.tableId where categoryId = :categoryId")
	List<Map<String, Object>> findStoreLayerByCategoryId(@Param("categoryId") String categoryId);
	
	@Query(value = "select count(1) from DMDataTable WHERE categoryId = :categoryId")
	Long countByCategoryId(@Param("categoryId") String categoryId);
	
	@Query(value = "select count(*) from DMDataTable WHERE enName = :enName")
	Long countByEnName(@Param("enName") String enName);
	
	@Query(value = "select count(*) from DMDataTable WHERE enName = :enName and id != :id")
	Long countByEnNameAndId(@Param("enName") String enName, @Param("id") String id);
	
	@Query(value = "select distinct t1.enName from DMDataTable t1 left join DMBasicInfo t2 on t1.dataMartId = t2.id left join SysUser t3 on t2.userId = t3.userId  where t3.depId = :depId")
	Iterator<DMDataTable> findAllByUser(@Param("depId") String depId);
	
	@Query(value = "select etlDate from DMDataTable  where id = :id")
	DMDataTable findETLDateById(@Param("id") String id);
	
	@Query(value = "select datatable_id from DMDataTable where enName in (select enName from DMDataTable where id = :id) and id != :id order by createDate, createTime")
	Iterator<DMDataTable> findTableIdById(@Param("id") String id);
	
	@Query(value = "select * from DMDataTable where enName = :enName")
	List<DMDataTable> findByEnName(@Param("enName") String enName);
	
	@Query(value = "select * from DMDataTable where enName = :enName and id = :id")
	List<DMDataTable> findByEnNameAndId(@Param("enName") String enName,@Param("id") String id);
	
	@Query(value = "select * from DMDataTable where remark = :remark")
	List<DMDataTable> findAllByRemark(@Param("remark") String remark);
	
	
}
