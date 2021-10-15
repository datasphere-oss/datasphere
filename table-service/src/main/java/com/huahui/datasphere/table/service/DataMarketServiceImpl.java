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

package com.huahui.datasphere.table.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import com.huahui.datasphere.datasource.common.CmlpApplicationEnum;
import com.huahui.datasphere.datasource.common.DataSrcErrorList;
import com.huahui.datasphere.datasource.common.DataSrcRestError;
import com.huahui.datasphere.datasource.common.ErrorListEnum;
import com.huahui.datasphere.datasource.common.HelperTool;
import com.huahui.datasphere.datasource.common.KerberosConfigInfo;
import com.huahui.datasphere.datasource.connection.DbUtilitiesV2;
import com.huahui.datasphere.datasource.database.DataBaseModelGet;
import com.huahui.datasphere.datasource.datastore.DataStoreModelGet;
import com.huahui.datasphere.datasource.datastore.DataStoreModelPost;
import com.huahui.datasphere.datasource.datastore.DataStoreModelPut;
import com.huahui.datasphere.datasource.exception.DataSrcException;
import com.huahui.datasphere.datasource.model.CassandraConnectionModel;
import com.huahui.datasphere.datasource.model.FileConnectionModel;
import com.huahui.datasphere.datasource.model.JdbcConnectionModel;
import com.huahui.datasphere.datasource.model.JobSubmissionYarn;
import com.huahui.datasphere.datasource.model.KerberosLogin;
import com.huahui.datasphere.datasource.model.MongoDbConnectionModel;
import com.huahui.datasphere.datasource.model.MysqlConnectorModel;
import com.huahui.datasphere.datasource.model.SparkYarnTestModel;
import com.huahui.datasphere.datasource.schema.DataSourceModelGet;
import com.huahui.datasphere.datasource.schema.DataSourceModelPost;
import com.huahui.datasphere.datasource.schema.DataSourceModelPut;
import com.huahui.datasphere.datasource.utils.ApplicationUtilities;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DataMarketServiceImpl implements DataMarketService {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private DbUtilities dbUtilities;
//	
//	@Autowired
//	private JdbcDataSourceSvcImpl jdbcSvc;
//
//
//	
	@Autowired
	HelperTool helperTool;
	
	@Autowired
	ApplicationUtilities applicationUtilities;
	
	// 获得数据源的详情信息
	@Override
	public List<Map<String, Object>> getDataStoreLayerInDataMart() {
		return Dbo.queryList("select  dsl_name,count(dsl_name) from " + Data_store_layer.TableName + " t1  join " +
				DataStoreRelation.TableName + " t2 on t1.dsl_id = t2.dsl_id and t2.data_source = ? group by dsl_name",
			StoreLayerDataSource.DM.getCode());
	}
	
	// 获得数据源的详情信息
	@Override
	public List<String> getDataStoresList(String user, String authorization, String category,
			String datastoreId, String textSearch) throws DataSrcException, IOException {

		List<String> results = null;
		
		try {
			results = dbUtilities.getDataStoreDetails(user, category, datastoreId, textSearch, false, true, authorization);
		} catch (Exception exc) {
			log.info("getDataSourcesList, Unknown Exception : " + exc.getMessage());
			DataSrcRestError err = DataSrcErrorList.buildError(exc, null, CmlpApplicationEnum.DATASTORE);
			
			throw new DataSrcException("Exception occurred during GET.",
					Status.INTERNAL_SERVER_ERROR.getStatusCode(), err);
		}
		
		return results;
	}

	@Override
	public boolean deleteDataStoreDetail(String user, String datastoreId) throws DataSrcException, IOException {
		
		log.info("deleteDataStoreDetail()::loggd in user: " + user);
		log.info("deleteDataStoreDetail():: datastoreId: " + datastoreId);
		
		try {
			//validate and throw Exceptions
			validateDataStore(user, datastoreId);
			
			String deleteType = "";
			deleteType = helperTool.getEnv("dataStore_delete_type",
					helperTool.getComponentPropertyValue("dataStore_delete_type"));
			deleteType = deleteType != null ? deleteType : "softdelete";
			if (deleteType.equals("softdelete"))
				return dbUtilities.softDeleteDataStore(user, datastoreId);
			else
				return dbUtilities.deleteDataStore(user, datastoreId);
			
		} catch (DataSrcException cmlpExc) {
			throw cmlpExc;
		} catch (Exception exc) {
			log.info("deleteDataStoreDetail, Unknown Exception : " + exc.getMessage());
			DataSrcRestError err = DataSrcErrorList.buildError(exc, null, CmlpApplicationEnum.DATASTORE);
			
			throw new DataSrcException("Exception occurred during DELETE.",
					Status.INTERNAL_SERVER_ERROR.getStatusCode(), err);
		}
	}

	@Override
	public String saveDataStoreDetail(String user, String authorization, DataStoreModelPost datastorePost) throws DataSrcException, IOException, SQLException, ClassNotFoundException {
		log.info("saveDataBaseDetail, logged in user: " + user);
		
		DataStoreModelGet datastore = new DataStoreModelGet(datastorePost);
		datastore.setOwnedBy(user);
		
		datastore = applicationUtilities.validateInputRequest(datastore, "create");
		
		
		datastore.setOwnedBy(user);
//		database.setActive(true);// default status is true;
		
//		database(applicationUtilities.getName(dataSource.getNamespace(), dataSource.getCategory(), serverName, user));
		
		log.info("saveDataStoreDetail, id for new datastore detail: " + datastore.getDatastoreId());
		log.info("saveDataStoreDetail, name for new datastore detail: " + datastore.getDataStoreName());
		log.info("saveDataStoreDetail, type for new datastore detail: " + datastore.getDataStoreType());

		dbUtilities.insertDataStoreDetails(datastore);

		log.info("saveDataStoreDetail, Successfully created new datastore : " + datastore.getDatastoreId());

		
		return datastore.getDatastoreId();
	}

	@Override
	public boolean updateDataStoreDetail(String user, String authorization,
			String datastoreId, DataStoreModelPut dataStorePut) throws DataSrcException, IOException {
		
		log.info("updateDataStoreDetail, user: " + user);
		log.info("updateDataStoreDetail, datastoreId: " + datastoreId);
		
		try {
			//validate and throw Exceptions
			validateDataStore(user, datastoreId);

			DataStoreModelGet datastore = new DataStoreModelGet(dataStorePut);
			datastore.setDatastoreId(datastoreId);
			datastore.setOwnedBy(user);
	
			log.info("updateDataStoreDetail, logged in user: " + user);
	
			//Validate input parameters
			datastore = applicationUtilities.validateInputRequest(datastore, "update");
	
			log.info("updateDataStoreDetail, key for new datastore detail: " + datastore.getDatastoreId());
			log.info("updateDataStoreDetail, type for new datastore detail: " + datastore.getDataStoreType());
	
			// check null values in the parameters that are required for connection
			if (checkDataStoreDetails(user, authorization, datastore, datastoreId, "update")
					.equals("success")) {
	
				return dbUtilities.updateDataStore(user, datastoreId, datastore);
				
			}
			
			return false;
			
		} catch (DataSrcException cmlpExc) {
			throw cmlpExc;
		} catch (Exception exc) {
			log.info("updateDataStoreDetail, Unknown Exception : " + exc.getMessage());
			DataSrcRestError err = DataSrcErrorList.buildError(exc, null, CmlpApplicationEnum.DATASTORE);
			
			throw new DataSrcException("Exception occurred during PUT.",
					Status.INTERNAL_SERVER_ERROR.getStatusCode(), err);
		}
	}

//	@Override
//	public InputStream getDataSourceContents(String user, String authorization, String datasourceKey,
//			String hdfsFilename) throws DataSrcException, IOException, SQLException, ClassNotFoundException {
//		log.info("getDataSourcesContents, user: " + user);
//		log.info("getDataSourcesContents, datasourceKey: " + datasourceKey);
//		
//		//validate and throw Exceptions
//		validateDataSource(user, datasourceKey);
//		
//		ArrayList<String> dbDatasourceDetails = dbUtilities.getDataSourceDetails(user, null, null, datasourceKey, null, true, false, authorization);
//		
//		String namespace = null;
//		
//		JSONObject objJson = new JSONObject(dbDatasourceDetails.get(0));
//		
//		// calling different service method based on category of the database
//		if (objJson.getString("ownedBy").equals(user)) {
//			if (objJson.getString("category").equals("cassandra")) {
//				return cassandraSvc.getResults(user, authorization, namespace, datasourceKey);
//			} else if (objJson.getString("category").equals("mysql")) {
//				return mySqlSvc.getResults(user, authorization, namespace, datasourceKey);
//			} else if (objJson.getString("category").equals("mongo")) {
//				return mongoSvc.getResults(user, authorization, namespace, datasourceKey);
//			} else if (objJson.getString("category").equals("hive")) {
//				return hiveSvc.getResults(user, authorization, namespace, datasourceKey);
//			} else if (objJson.getString("category").equals("hdfs")) {
//				return hdpSvc.getResults(user, authorization, namespace, datasourceKey,hdfsFilename);
//			} else if (objJson.getString("category").equals("file")) {
//				return fileSvc.getResults(user, authorization, namespace, datasourceKey);
//			} else if (objJson.getString("category").equals("jdbc")) {
//				return jdbcSvc.getResults(user, authorization, namespace, datasourceKey);
//			} else if (objJson.getString("category").equals("hive batch")) {
//				return hiveBatchSvc.getResults(user, authorization, namespace, datasourceKey, objJson.getString("batchSize"));
//			} else if (objJson.getString("category").equals("hdfs batch")) {
//				return hdpBatchSvc.getResults(user, authorization, namespace, datasourceKey, objJson.getString("batchSize"));
//			}
//		} else {
//			String[] variables = {"Authorization"};
//			DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._1003, variables, null, CmlpApplicationEnum.DATASOURCE);
//			throw new DataSrcException(
//					"please check dataset key provided and user permission for this operation", Status.UNAUTHORIZED.getStatusCode(), err);
//		}
//		
//		//won't reach up to this point as datasource in db always have a valid category
//		return null;
//	}
//
	@Override
	public String checkDataStoreDetails(String user, String authorization,
			DataStoreModelGet datastore, String datastoreId, String mode) throws DataSrcException, IOException, SQLException, ClassNotFoundException {
		
		log.info("checkDataStoreDetails, user: " + user);
		log.info("checkDataStoreDetails, datastoreId: " + datastoreId);
		
		//validate and throw Exceptions
		if (!"create".equalsIgnoreCase(mode)) {
			validateDataStore(user, datastoreId);
		}
		
		String connectionStatus = "failed";
		String enAssociation = "";
		boolean isCreate = false;
		boolean isUpdate = false;

		
		if(datastore == null) {
		
			List<String> dbObjects = dbUtilities.getDataStoreDetails(user, null,  datastoreId, null, false, true, authorization);
			
			if(dbObjects != null && dbObjects.size() > 0) {
				datastore = applicationUtilities.getDataStoreModel(dbObjects.get(0));
			}
		}

		//validate input for the required connection parameters
//		applicationUtilities.validateConnectionParameters(database);
		
		if ("create".equalsIgnoreCase(mode))
			isCreate = true;
		else if ("update".equalsIgnoreCase(mode))
			isUpdate = true;
		
		// Check for Kerberos config during update
		if (isUpdate) {
			if (datastore.getDataStoreType().equals("stage")
					|| datastore.getDataStoreType().equals("dataware") || datastore.getDataStoreType().equals("datamart")
					|| datastore.getDataStoreType().equals("ods") ) {
				log.info("checkDataStoreDetails(), Mode is Update and Category is " + datastore.getDataStoreType());
				

				// 检查必要的属性
				log.info("checkDataStoreDetails(), Fetching datastore from db");
				List<String> dbDatastoreDetails = dbUtilities.getDataStoreDetails(user, null, 
						datastore.getDatastoreId(), null, false, true, authorization);

				log.info("checkDataStoreDetails(), Building datastoremodel...");
				DataStoreModelGet datastoreModel = applicationUtilities.getDataStoreModel(dbDatastoreDetails.get(0));

				

				return "success";
				
			}
		}
		
		return "failed";
		
	}
	
	
//
//	@Override
//	public boolean validateDataSourceConnection(String user, String authorization,
//			String dataSourceKey) throws DataSrcException, IOException {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public String getMetadataContents(String user, String authorization, String dataSourceKey)
//			throws DataSrcException, IOException {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public InputStream getDataSourceSamples(String user, String authorization, String datasourceKey,
//			String hdfsFilename) throws DataSrcException, IOException, SQLException, ClassNotFoundException {
//		
//		log.info("ENTER:getDataSourcesSamples");
//		log.info("getDataSourceSamples, user: " + user);
//		log.info("getDataSourceSamples, datasourceKey: " + datasourceKey);
//		
//		//validate and throw Exceptions
//		validateDataSource(user, datasourceKey);
//		
//		String namespace = null;
//
//		ArrayList<String> dbDetails = dbUtilities.getDataSourceDetails(user, null, null, datasourceKey, null, true,
//				false, authorization);
//		
//		JSONObject objJson = new JSONObject(dbDetails.get(0));
//		log.info("DataSourceServiceImpl::getDataSourcesSamples:objJson:\n" + objJson);
//
//		//EE-2904
//		if(objJson.has("readWriteDescriptor")) {
//			String readWriteFlag = objJson.getString("readWriteDescriptor");
//			
//			if("write".equals(readWriteFlag)) {
//				String[] variables = {"readWriteDescriptor"};
//				DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._0003, variables, null, CmlpApplicationEnum.DATASOURCE);
//				throw new DataSrcException(
//						"Sample Data cannot be fetched for 'write' type of ReadWrite flag.", Status.BAD_REQUEST.getStatusCode(), err);
//
//			}
//		}
//
//		// calling different service method based on category of the database
//		if (objJson.getString("ownedBy").equals(user)) {
//
//			if (objJson.getString("category").equals("mysql")) {
//
//				log.info("RETURN:DataSourceServiceImpl::getDataSourcesSamples:mysql");
//				return mySqlSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//
//			} else if (objJson.getString("category").equals("cassandra")) {
//
//				log.info("RETURN:DataSourceServiceImpl::getDataSourcesSamples:cassandra");
//				return cassandraSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//
//			} else if (objJson.getString("category").equals("file")) {
//
//				log.info("RETURN:DataSourceServiceImpl::getDataSourcesSamples:File");
//				return fileSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//
//			} else if (objJson.getString("category").equals("jdbc")) {
//
//				log.info("RETURN:DataSourceServiceImpl::getDataSourcesSamples:jdbc");
//				return jdbcSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//				
//			} else if (objJson.getString("category").equals("mongo")) {
//
//				log.info("RETURN:DataSourceServiceImpl::getDataSourcesSamples:mongo");
//				return mongoSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//
//			} else if (objJson.getString("category").equals("hive")) { // return
//
//				log.info("RETURN:DataSourceServiceImpl::getDataSourcesSamples:hive");
//				return hiveSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//
//			} else if (objJson.getString("category").equals("hdfs")) { // return
//
//				log.info("RETURN:DataSourceServiceImpl::hdfs");
//				return hdpSvc.getSampleResults(user, authorization, namespace, datasourceKey,hdfsFilename);
//
//			} else if (objJson.getString("category").equals("hdfs batch")) { // return
//
//				log.info("RETURN:DataSourceServiceImpl::hdfs batch");
//				return hdpBatchSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//
//			} else if (objJson.getString("category").equals("hive batch")) { // return
//
//				log.info("RETURN:DataSourceServiceImpl::hive batch");
//				return hiveBatchSvc.getSampleResults(user, authorization, namespace, datasourceKey);
//			}
//			
//		} else {
//			String[] variables = {"Authorization"};
//			DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._1003, variables, null, CmlpApplicationEnum.DATASOURCE);
//			throw new DataSrcException(
//					"please check dataset key provided and user permission for this operation", Status.UNAUTHORIZED.getStatusCode(), err);
//
//		}
//
//		return null;
//	}
//
//	@Override
//	public List<String> kerberosFileUpload(String user,
//			MultipartFile[] bodyParts) throws DataSrcException, IOException {
//		
//		log.info("kerberosFileUpload, logged in user: " + user);
//		List<String> uploadedFileNames = new ArrayList<String>();
//
//		if (bodyParts != null) {
//			// Make sure that there are two files to be uploaded
//			if (bodyParts.length != 2) {
//				String[] variables = {"uploadedFiles"};
//				DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._0005, variables, null, CmlpApplicationEnum.DATASOURCE);
//				throw new DataSrcException(
//						"Invalid Number of Attachments. Valid no. of attachemnst are - 2", Status.BAD_REQUEST.getStatusCode(), err);
//
//			} else { // Make sure that one file must be .conf
//				boolean isConfFile = false;
//				String attachmentfilename = null;
//				for (int i = 0; i < bodyParts.length; i++) {
//					attachmentfilename = bodyParts[i].getOriginalFilename();
//					if (attachmentfilename.endsWith(".conf")) {
//						isConfFile = true;
//						break;
//					}
//				}
//
//				if (!isConfFile) {
//					String[] variables = {".conf", ".keytab"};
//					DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._0006, variables, null, CmlpApplicationEnum.DATASOURCE);
//					throw new DataSrcException(
//							"Invalid format of Attachments. Valid formats are - .conf, .keytab", Status.BAD_REQUEST.getStatusCode(), err);
//
//				}
//			}
//
//			// Make sure that file directory exists
//			if (Files.notExists(Paths.get(helperTool.getEnv("kerberos_user_config_dir",
//					helperTool.getComponentPropertyValue("kerberos_user_config_dir"))))) { // ./kerberos
//				StringBuilder sb = new StringBuilder();
//				sb.append(System.getProperty("user.dir")).append(System.getProperty("file.separator"))
//						.append(helperTool.getEnv("kerberos_user_config_dir",
//								helperTool.getComponentPropertyValue("kerberos_user_config_dir")));
//				Files.createDirectories(Paths.get(sb.toString()));
//			}
//
//			MultipartFile attachment = null;
//			String attachmentfilename = null;
//			String fileNameKey = null;
//			log.info("kerberosFileupload, Filename to be uploaded: " + fileNameKey);
//
//			for (int i = 0; i < bodyParts.length; i++) {
//					attachment = bodyParts[i];
//					attachmentfilename = bodyParts[i].getOriginalFilename();
//				fileNameKey = applicationUtilities.getKerberosFileName(user, attachmentfilename);
//				if (attachmentfilename.endsWith(".conf")) { // Upload it as
//					// m09286.krb5.conf
//					StringBuilder sb = new StringBuilder();
//					sb.append(System.getProperty("user.dir")).append(System.getProperty("file.separator"))
//							.append(helperTool.getEnv("kerberos_user_config_dir",
//									helperTool.getComponentPropertyValue("kerberos_user_config_dir")))
//							.append(System.getProperty("file.separator")).append(fileNameKey).append(".krb5.conf");
//
//						
//						java.nio.file.Path path = FileSystems.getDefault().getPath(sb.toString());
//						Files.copy(attachment.getInputStream(), path);
//						
//
//					uploadedFileNames.add(fileNameKey + ".krb5.conf");
//
//				} else { // Upload the KeyTab with decoding
//					if (attachmentfilename.lastIndexOf(".") > 0)
//						attachmentfilename = attachmentfilename.substring(0, attachmentfilename.lastIndexOf("."));
//
//					StringBuilder sb = new StringBuilder();
//					sb.append(System.getProperty("user.dir")).append(System.getProperty("file.separator"))
//							.append(helperTool.getEnv("kerberos_user_config_dir",
//									helperTool.getComponentPropertyValue("kerberos_user_config_dir")))
//							.append(System.getProperty("file.separator")).append(fileNameKey).append(".keytab");
//
//						java.nio.file.Path path = FileSystems.getDefault().getPath(sb.toString());
//						Files.copy(attachment.getInputStream(), path);
//						//attachment.transferTo(new File(sb.toString()));
//					uploadedFileNames.add(fileNameKey + ".keytab");
//				}
//			}
//
//			log.info("kerberosFileupload, Files have been successfully uploaded.");
//		} else {
//			log.info("kerberosFileupload, No Files found to upload.");
//		}
//
//		return uploadedFileNames;
//	}
//

	private void validateDataStore(String user, String datastoreId) throws DataSrcException, IOException {
		boolean isValid = false;
		
		isValid = dbUtilities.isValidDatastore(user, datastoreId);
		
		if(!isValid) {
			if(dbUtilities.isDatastoreExists(datastoreId)) {
				//unauthorized access
				String[] variables = {"Authorization"};
				DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._1003, variables, null, CmlpApplicationEnum.DATASTORE);
				throw new DataSrcException(
						"please check datastore id provided and user permission for this operation", Status.UNAUTHORIZED.getStatusCode(), err);
				
			} else {
				//No Datasource in DB
				String[] variables = { "datastoreId"};
				DataSrcRestError err = DataSrcErrorList.buildError(ErrorListEnum._0003, variables, null, CmlpApplicationEnum.DATASTORE);
				
				throw new DataSrcException("Invalid data. No DataStore info. Please send valid datastoreId.",
						Status.NOT_FOUND.getStatusCode(), err);
			}
		}
	}

}
