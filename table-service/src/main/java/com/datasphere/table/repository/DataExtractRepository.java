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

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.datasphere.table.domain.ColumnStoreRelation;
import com.datasphere.table.domain.DMDataTable;
import com.datasphere.table.domain.DataExtractDefination;

public interface DataExtractRepository extends PagingAndSortingRepository<DataExtractDefination, String> {

	
//	@Query(value = "SELECT * FROM DMDataTable dt join TableRelationStore trs on dt.id = trs.tableId where categoryId = :categoryId")
//	List<Map<String, Object>> findStoreLayerByColId();
//	
//	@Query(value = "SELECT COUNT(1) FROM DatabaseSetting WHERE databaseId = :databaseId")
//	Long countById();
//	
//	@Query(value = "SELECT ingestionType FROM DatabaseSetting WHERE databaseId = :databaseId")
//	String findIngestionTypeById();

}
