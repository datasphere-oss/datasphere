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

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.huahui.datasphere.table.domain.DataExtractDefination;
import com.huahui.datasphere.table.domain.TableRelationStore;

public interface TableRelationStoreRepository extends PagingAndSortingRepository<TableRelationStore, String> {
	
	@Query(value = "DELETE FROM TableRelationStore  WHERE tableId in (SELECT storage_id FROM DMTableStorageInfo WHERE tableId in (SELECT tableId FROM TableInfo WHERE databaseId = :databaseId))")
	void deleteTableRelationStoreByTableId(String tableId);

}
