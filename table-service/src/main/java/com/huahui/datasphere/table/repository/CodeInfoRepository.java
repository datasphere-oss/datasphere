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

import com.huahui.datasphere.table.domain.DMCodeInfo;
import com.huahui.datasphere.table.domain.DMDataTable;

public interface CodeInfoRepository extends PagingAndSortingRepository<DMCodeInfo, String> {

	
	@Query(value = "SELECT * FROM DMCodeInfo where classifed in (select classified from DMCodeInfo group by classified)")
	DMCodeInfo findCodeInfo();
	
	@Query(value = "SELECT * FROM DMCodeInfo where classified = :classified")
	DMCodeInfo findCodeInfoByClassified(String classified);
	

	@Query(value = "SELECT count(*) FROM DMCodeInfo where classified = :classified and typeValue = :typeValue")	
	Integer countByValue();
	
	@Query(value = "SELECT count(*) FROM DMCodeInfo where classified = :classified")	
	Integer countByClassified(String classified);
	
	@Query(value = "DELETE FROM DMCodeInfo where classified = :classified")	
	void deleteByClassified(String classified);
	
	@Query(value = "DELETE FROM DMCodeInfo where group by classified")	
	List<String> findAllCodeInfosByClassified();
		
}
