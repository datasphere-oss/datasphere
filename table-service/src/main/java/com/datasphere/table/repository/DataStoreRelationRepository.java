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
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.datasphere.table.domain.DataStoreLayer;
import com.datasphere.table.domain.DataStoreRelation;

public interface DataStoreRelationRepository extends PagingAndSortingRepository<DataStoreRelation, String> {
	
	
	@Query(value = "select isSuccessful from DataStoreRelation  where tableId = :tableId and dataSource = :dataSource")
	DataStoreRelation findSuccessByTableId(@Param("tableId") String tableId,@Param("dataSource") String dataSource);
	
	@Query(value = "select * from DataStoreRelation  where tableId = :tableId and dataSource = :dataSource")
	DataStoreRelation findByTableId(@Param("tableId") String tableId,@Param("dataSource") String dataSource);
	
	@Query(value = "UPDATE DataStoreRelation SET dslId = :dslId WHERE tableId = :tableId")
	Long updateDSLIdByTableId(@Param("dslId") String dslId,@Param("tableId") String tableId);
	
	
}
