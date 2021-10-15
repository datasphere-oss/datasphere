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
import com.datasphere.table.domain.DMTableColumn;

public interface TableColumnRepository extends PagingAndSortingRepository<DMTableColumn, String> {

	@Query(value = "DELETE FROM  DMTableColumn WHERE tableId = :tableId")
	void deleteByTableId(String tableId);
	
	@Query(value = "select id, columnName, cnName, columnType, isPrimaryKey FROM DMTableColumn WHERE id = :id and endDate = :endDate")
	List<Map<String, Object>> findTableColumnById(@Param("id") String id,@Param("endDate") String endDate);

}
