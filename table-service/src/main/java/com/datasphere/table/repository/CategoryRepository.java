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

import com.datasphere.table.domain.DMCategory;

public interface CategoryRepository extends PagingAndSortingRepository<DMCategory, String> {

	@Query(value = "SELECT * FROM DMCategory where dataMartId = :dataMartId")
	List<Map<String, Object>> findDMLCategoryByDataMartId(@Param("dataMartId") String dataMartId);
	
	
	@Query(value = "select count(*) from DMCategory where id = :id")
	Long findCountById(@Param("id") String id);
	
	@Query(value = "update DMCategory  set name = :name, number = :number, parentId = :parentId , desc = :desc  where id = :id")
	void updateCategoryByNameAndNumber(@Param("name") String name,@Param("number") String number,@Param("parentId") String parentId,@Param("desc") String desc,@Param("id") String id);

	@Query(value = "update DMCategory set name = :name  where id = :id ")
	void updateNameById(@Param("name") String name, @Param("id") String id);
	
	
	@Query(value = "select count(*) from DMCategory where parentId = :id")
	Long findCountByParentId(@Param("id") String id);

	@Query(value = "select id from DMCategory where dataMartId = :dataMartId")
	List<String> findIdByMartId(@Param("dataMartId") String dataMartId);
	
	@Query(value = "select id from DMCategory where name = :name and dataMartId = :dataMartId")
	List<String> findIdByName(@Param("name") String name, @Param("dataMartId") String dataMartId);
	
	
	@Query(value = "select parentId from DMCategory where id = :id")
	DMCategory findParentIdById(@Param("id") String id);
	
	@Query(value = "select parentId from DMCategory where dataMartId = :dataMartId")
	DMCategory findMartNameByMartId(@Param("dataMartId") String dataMartId);
	
	@Query(value = "select id, name from DMCategory where parentId = :parentId and dataMartId = :dataMartId")
	List<DMCategory> findNameByParentIdAndMartId(@Param("parentId") String parentId, @Param("dataMartId") String dataMartId);
	
	@Query(value = "select count(*) from DMCategory where name = :name and dataMartId = :dataMartId")
	Boolean findCountByNameAndMartId(@Param("id") String id, @Param("dataMartId") String dataMartId);

	@Query(value = "select count(*) from DMCategory where number = :number and dataMartId = :dataMartId")
	Boolean findCountByNumberAndMartId(@Param("number") String number, @Param("dataMartId") String dataMartId);
	
}
