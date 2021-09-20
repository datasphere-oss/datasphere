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
import com.huahui.datasphere.table.domain.DMDataTable;
import com.huahui.datasphere.table.domain.DMDataTableField;
import com.huahui.datasphere.table.domain.DMTableColumn;

public interface DateTableFieldRepository extends PagingAndSortingRepository<DMDataTableField, String> {

	@Query(value = "select id, columnName, cnName, columnType, isPrimaryKey FROM DMTableColumn WHERE id = :id and endDate = :endDate")
	List<Map<String, Object>> findTableColumnById(@Param("id") String id,@Param("endDate") String endDate);
	
	@Query(value = "select * from DMDataTableField where dataTableId = :dataTableId AND endDate in (:maxDate,:initDate) order by field_seq")
	List<Map<String, Object>> findAllByEndDate(@Param("dataTableId") String dataTableId, @Param("maxDate") String maxDate,@Param("initDate") String initDate);

}
