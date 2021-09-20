/*-
 * ===============LICENSE_START=======================================================
 * Acumos
 * ===================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property & Tech Mahindra. All rights reserved.
 * ===================================================================================
 * This Acumos software file is distributed by AT&T and Tech Mahindra
 * under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ===============LICENSE_END=========================================================
 */

package com.huahui.datasphere.table.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.huahui.datasphere.table.domain.ColumnStoreRelation;
import com.huahui.datasphere.table.domain.DMDataTable;

public interface BookTableRepository extends PagingAndSortingRepository<DMDataTable, String> {

	
	@Query(value = "SELECT * FROM DMDataTable dt join TableRelationStore trs on dt.id = trs.tableId where categoryId = :categoryId")
	List<Map<String, Object>> findStoreLayerByColId();
	
	@Query(value = "SELECT COUNT(1) FROM DatabaseSetting WHERE databaseId = :databaseId")
	Long countById();
	
	@Query(value = "SELECT ingestionType FROM DatabaseSetting WHERE databaseId = :databaseId")
	String findIngestionTypeById();

}
