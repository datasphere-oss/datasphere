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

import com.datasphere.table.domain.DMCodeInfo;
import com.datasphere.table.domain.DMDataTable;

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
