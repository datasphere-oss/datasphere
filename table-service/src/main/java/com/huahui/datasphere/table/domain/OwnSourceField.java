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
 * 数据源表字段
 */
@Table(name = "own_source_field")
public class OwnSourceField 
{
	private static final long serialVersionUID = 321566870187324L;
	
	/**
	* 检查给定的名字，是否为主键中的字段
	* @param name String 检验是否为主键的名字
	* @return`as
	*/
	/** 数据源表字段 */
	
	
	@Id
	@Column(name = "own_field_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "字段id:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String ownFieldId;
	

	
	@Column(name = "field_name", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "字段名称")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "字段名称:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String fieldName;
	
	@Column(name = "field_type", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "字段类型:")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "字段类型:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String fieldType;
	
	
	@Column(name = "remark", nullable = true, columnDefinition = "VARCHAR(1024)")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "备注", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String remark;
	
	
	
	@Column(name = "own_dource_table_id", nullable = false, columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "已选数据源表id:")
	@Size(max = 1024)
	@ApiModelProperty(required = true, value = "已选数据源表id:", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private String own_dource_table_id;



	public String getOwnFieldId() {
		return ownFieldId;
	}



	public void setOwnFieldId(String ownFieldId) {
		this.ownFieldId = ownFieldId;
	}



	public String getFieldName() {
		return fieldName;
	}



	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}



	public String getFieldType() {
		return fieldType;
	}



	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getOwn_dource_table_id() {
		return own_dource_table_id;
	}



	public void setOwn_dource_table_id(String own_dource_table_id) {
		this.own_dource_table_id = own_dource_table_id;
	}

	
}
