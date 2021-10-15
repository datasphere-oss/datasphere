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

import com.datasphere.table.domain.DataStoreAddition;
import com.datasphere.table.domain.DataStoreLayer;

public interface DataStoreRepository extends PagingAndSortingRepository<DataStoreLayer, String> {
	
	
	
	
	@Query(value = "select t1.id, t1.storeType from DataStoreLayer t1 left join DataStoreRelation t2 on t1.id = t2.dslId  where t2.tableId = :tableId and t2.dataSource = :dataSource")
	Optional<DataStoreLayer> findDataStoreIdByTableId(@Param("tableId") String tableId,@Param("dataSource") String dataSource);
	
	@Query(value = "select * from DataStoreLayer  where name like %:name%")
	Iterator<DataStoreLayer> findAllByFuzzy(@Param("fuzzy") String name);
	
	@Query(value = "select dsla_storelayer from DataStoreAddition t1 left join ColumnStoreRelation t2 on t1.dslad_id = t2.dslad_id where t2.col_id = :colId and t2.data_source = :dataSource")
	Iterator<DataStoreAddition> findByColId(@Param("colId") String id,@Param("dataSource") String dataSource);
	

}
