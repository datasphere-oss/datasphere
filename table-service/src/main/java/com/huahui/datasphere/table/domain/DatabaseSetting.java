package com.huahui.datasphere.table.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Collections;

/**
 * 源系统数据库设置
 */
@Table(tableName = "database_set")
public class DatabaseSetting implements Serializable {
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "database_id", nullable = false, updatable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据库ID")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "数据库ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String databaseId;					
	
	@Column(name = "agent_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "代理ID")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "代理ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String agentId;
	
	
	
	@Column(name = "task_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库采集任务名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String taskName;
	
	
	@Column(name = "database_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String databaseName;
	
	
	@Column(name = "password", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库密码", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String password;
	
	@Column(name = "driver", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库驱动", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String driver;
	
	
	@Column(name = "database_type", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String databaseType;
	
	
	@Column(name = "user_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "用户名称", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String userName;
	
	
	@Column(name = "database_ip", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库服务器IP", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String databaseIp;
	
	
	@Column(name = "database_port", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库端口", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String databasePort;
	
	
	@Column(name = "host_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "主机名", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String hostName;
	
	
	@Column(name = "os", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "操作系统类型", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String os;
	
	
	@Column(name = "is_sendok", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否设置完成并发送成功")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "是否设置完成并发送成功", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String isSendok;
	
	
	@Column(name = "database_number", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库设置编号", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String databaseNumber;
	
	
	@Column(name = "dbAgent", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否DB文件数据采集")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "是否DB文件数据采集", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String dbAgent;
	
	
	@Column(name = "plane_url", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "DB文件数据字典位置", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String planeUrl;
	
	
	@Column(name = "database_separator", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据采用分隔符", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String databaseSeparator;
	
	
	@Column(name = "row_separator", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据行分隔符", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String rowSeparator;
	
	
	
	@Column(name = "classified_id", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "分类id")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "分类id", example = "12345678-abcd-90ab-cdef-1234567890ab")
	
	private String classifiedId;
	
	
	@Column(name = "cp_or", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "清洗顺序", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String cpOr;
	
	
	@Column(name = "jdbc_url", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库连接地址", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String jdbcUrl;
	
	
	
	@Column(name = "ingestion_type", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "数据库采集方式")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "数据库采集方式", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String ingestionType;
	
	
	@Column(name = "dsl_id", nullable = false, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "存储层配置ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String dataStoreLayerId;


	public String getDatabaseId() {
		return databaseId;
	}


	public void setDatabaseId(String databaseId) {
		this.databaseId = databaseId;
	}


	public String getAgentId() {
		return agentId;
	}


	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public String getDatabaseName() {
		return databaseName;
	}


	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getDriver() {
		return driver;
	}


	public void setDriver(String driver) {
		this.driver = driver;
	}


	public String getDatabaseType() {
		return databaseType;
	}


	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public String getDatabaseIp() {
		return databaseIp;
	}


	public void setDatabaseIp(String databaseIp) {
		this.databaseIp = databaseIp;
	}


	public String getDatabasePort() {
		return databasePort;
	}


	public void setDatabasePort(String databasePort) {
		this.databasePort = databasePort;
	}


	public String getHostName() {
		return hostName;
	}


	public void setHostName(String hostName) {
		this.hostName = hostName;
	}


	public String getOs() {
		return os;
	}


	public void setOs(String os) {
		this.os = os;
	}


	public String getIsSendok() {
		return isSendok;
	}


	public void setIsSendok(String isSendok) {
		this.isSendok = isSendok;
	}


	public String getDatabaseNumber() {
		return databaseNumber;
	}


	public void setDatabaseNumber(String databaseNumber) {
		this.databaseNumber = databaseNumber;
	}


	public String getDbAgent() {
		return dbAgent;
	}


	public void setDbAgent(String dbAgent) {
		this.dbAgent = dbAgent;
	}


	public String getPlaneUrl() {
		return planeUrl;
	}


	public void setPlaneUrl(String planeUrl) {
		this.planeUrl = planeUrl;
	}


	public String getDatabaseSeparator() {
		return databaseSeparator;
	}


	public void setDatabaseSeparator(String databaseSeparator) {
		this.databaseSeparator = databaseSeparator;
	}


	public String getRowSeparator() {
		return rowSeparator;
	}


	public void setRowSeparator(String rowSeparator) {
		this.rowSeparator = rowSeparator;
	}


	public String getClassifiedId() {
		return classifiedId;
	}


	public void setClassifiedId(String classifiedId) {
		this.classifiedId = classifiedId;
	}


	public String getCpOr() {
		return cpOr;
	}


	public void setCpOr(String cpOr) {
		this.cpOr = cpOr;
	}


	public String getJdbcUrl() {
		return jdbcUrl;
	}


	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}


	public String getIngestionType() {
		return ingestionType;
	}


	public void setIngestionType(String ingestionType) {
		this.ingestionType = ingestionType;
	}


	public String getDataStoreLayerId() {
		return dataStoreLayerId;
	}


	public void setDataStoreLayerId(String dataStoreLayerId) {
		this.dataStoreLayerId = dataStoreLayerId;
	}

	

}
