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

package com.datasphere.table.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


import com.huahui.datasphere.datasource.datastore.DataStoreModelGet;
import com.huahui.datasphere.datasource.datastore.DataStoreModelPost;
import com.huahui.datasphere.datasource.datastore.DataStoreModelPut;
import com.huahui.datasphere.datasource.exception.DataSrcException;
import com.huahui.datasphere.datasource.schema.DataSourceModelGet;
import com.huahui.datasphere.datasource.schema.DataSourceModelPost;
import com.huahui.datasphere.datasource.schema.DataSourceModelPut;
import org.springframework.web.multipart.MultipartFile;


public interface DataMarketService {

	
	/**
	 * 
	 * @return
	 * @throws DataSrcException
	 * @throws IOException
	 */
	public List<Map<String, Object>> getDataStoreLayerInDataMart() throws DataSrcException, IOException;
	
	
	
	/**
	 * Returns list of available datasources for the provided input
	 * @param user
	 * @param authorization
	 * @param namespace
	 * @param category
	 * @param dataSourcekey
	 * @param textSearch
	 * @return List
	 * @throws DataSrcException
	 * @throws IOException
	 */
	public List<String> getDataStoresList(String user,  String authorization,  String category, String dataSourcekey, String textSearch)
			throws DataSrcException, IOException;
	
	

	/**
	 * Returns boolean after deleting an existing datasource
	 * @param user
	 * @param datasourceKey
	 * @return boolean
	 * @throws DataSrcException
	 * @throws IOException
	 */
	public boolean deleteDataStoreDetail(String user, String datastoreId) throws DataSrcException, IOException;
//
//	/**
//	 * Returns datasource key after successfully onboarded a new datasource
//	 * @param user
//	 * @param authorization
//	 * @param dataSource
//	 * @return String
//	 * @throws DataSrcException
//	 * @throws IOException
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 */
	public String saveDataStoreDetail(String user, String authorization,
			DataStoreModelPost dataSource) throws DataSrcException, IOException, SQLException, ClassNotFoundException;

	/**
	 * Returns boolean after successfully updated an existing datasource
	 * @param user
	 * @param authorization
	 * @param datasourceKey
	 * @param dataSource
	 * @return boolean
	 * @throws DataSrcException
	 * @throws IOException
	 */
	public boolean updateDataStoreDetail(String user, String authorization,
			String datastoreId, DataStoreModelPut datastore) throws DataSrcException, IOException;

//	/**
//	 * Returns contents for an existing datasource
//	 * @param user
//	 * @param authorization
//	 * @param dataSourceKey
//	 * @param hdfsFilename
//	 * @return InputStream
//	 * @throws DataSrcException
//	 * @throws IOException
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 */	
//	public InputStream getDataSourceContents(String user, String authorization, String dataSourceKey,String hdfsFilename)
//			throws DataSrcException, IOException, SQLException, ClassNotFoundException;
//
	/**
	 * Returns the status of an existing datasource
	 * @param user
	 * @param authorization
	 * @param datasource
	 * @param dataSourceKey
	 * @param mode
	 * @return String
	 * @throws DataSrcException
	 * @throws IOException
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */	
	public String checkDataStoreDetails(String user, String authorization,
			DataStoreModelGet datastore, String datastoreId, String mode) throws DataSrcException, IOException, SQLException, ClassNotFoundException;
//	
//	/**
//	 * Returns the connection status of an existing datasource
//	 * @param user
//	 * @param authorization
//	 * @param dataSourceKey
//	 * @return boolean
//	 * @throws DataSrcException
//	 * @throws IOException
//	 */	
//	public boolean validateDataSourceConnection(String user, String authorization, 
//			String dataSourceKey) throws DataSrcException, IOException;
//
//	/**
//	 * Returns metadata for an existing datasource
//	 * @param user
//	 * @param authorization
//	 * @param dataSourceKey
//	 * @return
//	 * @throws DataSrcException
//	 * @throws IOException
//	 */	
//	public String getMetadataContents(String user, String authorization,
//			String dataSourceKey) throws DataSrcException, IOException;
//	
//	/**
//	 * Returns sample data for an existing datasource
//	 * @param user
//	 * @param authorization
//	 * @param dataSourceKey
//	 * @param hdfsFilename
//	 * @return InputStream
//	 * @throws DataSrcException
//	 * @throws IOException
//	 * @throws SQLException
// 	 * @throws ClassNotFoundException
//	 */	
//	public InputStream getDataSourceSamples(String user, String authorization, String dataSourceKey,String hdfsFilename)
//			throws DataSrcException, IOException, SQLException, ClassNotFoundException;
//
//	/**
//	 * Allows to upload Kerebros configuration files
//	 * @param user
//	 * @param bodyParts
//	 * @return List
//	 * @throws DataSrcException
//	 * @throws IOException
//	 */	
//	public List<String> kerberosFileUpload(String user,
//			MultipartFile[] bodyParts) throws DataSrcException, IOException;
//
//	/**
//	 * Allows to write back prediction results for an existing datasource
//	 * @param user
//	 * @param authorization
//	 * @param datasourceKey
//	 * @param hdfsFilename
//	 * @param data
//	 * @param contentType
//	 * @param includesHeader
//	 * @return boolean
//	 * @throws DataSrcException
//	 * @throws IOException
//	 */	
//	public boolean writebackPrediction(String user, String authorization, String datasourceKey, String hdfsFilename, 
//			String data, String contentType, String includesHeader) 
//			throws DataSrcException, IOException;
}

