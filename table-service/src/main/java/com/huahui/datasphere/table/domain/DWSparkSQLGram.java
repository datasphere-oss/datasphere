package com.huahui.datasphere.table.domain;
import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Collections;

/**
 * 数据加工spark语法提示
 */
@Table(name = "dw_sparksql_gram")
public class DWSparkSQLGram
{
	private static final long serialVersionUID = 321566870187324L;
	
	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return
	*/
	/** 数据加工spark语法提示 */

	
	@Id
	@Column(name = "esg_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "esg_id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "序号:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String esgId;
	
	@Column(name = "function_name", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "函数名称:")
	@Size(max = 1024)
	@ApiModelProperty(value = "函数名称:", example = "")
	private String functionName;
	
	@Column(name = "function_example", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "函数例子:")
	@Size(max = 1024)
	@ApiModelProperty(value = "函数例子:", example = "")
	private String functionExample;
	
	
	
	@Column(name = "function_desc", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "函数描述:")
	@Size(max = 1024)
	@ApiModelProperty(value = "函数例子:", example = "")
	private String desc;
	
	
	@Column(name = "is_available", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否可用(IsFlag):1-是<Shi> 0-否<Fou> ")
	@Size(max = 1024)
	@ApiModelProperty(value = "是否可用(IsFlag):1-是<Shi> 0-否<Fou> ", example = "")
	private String isAvailable;
	
	
	@Column(name = "is_udf", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否udf(IsFlag):1-是<Shi> 0-否<Fou> ")
	@Size(max = 1024)
	@ApiModelProperty(value = "是否udf(IsFlag):1-是<Shi> 0-否<Fou> ", example = "")
	private String isUDF;
	
	
	@Column(name = "remark", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "备注:", example = "")
	private String remark;
	
	
	@Column(name = "class_url", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "函数类路径:", example = "")
	private String classURL;
	
	@Column(name = "jar_url", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "函数类路径:", example = "")
	private String jarURL;
	
	
	@Column(name = "is_sparksql", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "是否同时使用sparksql(IsFlag):1-是<Shi> 0-否<Fou> ")
	@Size(max = 1024)
	@ApiModelProperty(value = "是否同时使用sparksql(IsFlag):1-是<Shi> 0-否<Fou> ", example = "")
	private String isSparkSQL;
	
	
	@Column(name = "hivedb_name", columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(value = "hive库名:", example = "")
	private String hivedbName;
	
	
	@Column(name = "function_classify", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "函数分类:")
	@Size(max = 1024)
	@ApiModelProperty(value = "函数分类:", example = "")
	
	private String functionClassify;


	public String getEsgId() {
		return esgId;
	}


	public void setEsgId(String esgId) {
		this.esgId = esgId;
	}


	public String getFunctionName() {
		return functionName;
	}


	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}


	public String getFunctionExample() {
		return functionExample;
	}


	public void setFunctionExample(String functionExample) {
		this.functionExample = functionExample;
	}


	public String getDesc() {
		return desc;
	}


	public void setDesc(String desc) {
		this.desc = desc;
	}


	public String getIsAvailable() {
		return isAvailable;
	}


	public void setIsAvailable(String isAvailable) {
		this.isAvailable = isAvailable;
	}


	public String getIsUDF() {
		return isUDF;
	}


	public void setIsUDF(String isUDF) {
		this.isUDF = isUDF;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getClassURL() {
		return classURL;
	}


	public void setClassURL(String classURL) {
		this.classURL = classURL;
	}


	public String getJarURL() {
		return jarURL;
	}


	public void setJarURL(String jarURL) {
		this.jarURL = jarURL;
	}


	public String getIsSparkSQL() {
		return isSparkSQL;
	}


	public void setIsSparkSQL(String isSparkSQL) {
		this.isSparkSQL = isSparkSQL;
	}


	public String getHivedbName() {
		return hivedbName;
	}


	public void setHivedbName(String hivedbName) {
		this.hivedbName = hivedbName;
	}


	public String getFunctionClassify() {
		return functionClassify;
	}


	public void setFunctionClassify(String functionClassify) {
		this.functionClassify = functionClassify;
	}

	
}
