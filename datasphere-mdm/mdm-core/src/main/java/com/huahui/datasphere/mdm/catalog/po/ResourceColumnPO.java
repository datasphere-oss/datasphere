package com.huahui.datasphere.mdm.catalog.po;


import java.util.Date;

/**
 * 数据资源数据项信息表
 * @Author: Wangbin
 * @Date: 2018/5/23
 */

public class ResourceColumnPO {

    /*
     * 主键
     * */
    private Long id;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public String getColSeqNum() {
		return colSeqNum;
	}

	public void setColSeqNum(String colSeqNum) {
		this.colSeqNum = colSeqNum;
	}

	public String getTableColCode() {
		return tableColCode;
	}

	public void setTableColCode(String tableColCode) {
		this.tableColCode = tableColCode;
	}

	public String getTableColType() {
		return tableColType;
	}

	public void setTableColType(String tableColType) {
		this.tableColType = tableColType;
	}

	public Boolean getRequiredFlag() {
		return requiredFlag;
	}

	public void setRequiredFlag(Boolean requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

	public Boolean getUniqueFlag() {
		return uniqueFlag;
	}

	public void setUniqueFlag(Boolean uniqueFlag) {
		this.uniqueFlag = uniqueFlag;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/*引用资源标识符id*/
    private Long resourceId;

    /*列名称*/
    private String colName;

    /*数据类型：字符型 C、数值型 N、货币型 Y、日期型 D、日期时间型 T、
    逻辑型 L、备注型 M、通用型 G、双精度型 B、整型 I、浮点型 F*/
    private String colType;

    /*细项顺序码：001-999*/
    private String colSeqNum;

    /*绑定数据库表的列名*/
    private String tableColCode;

    /*绑定数据库表的列类型*/
    private String tableColType;

    /*交换时是否必选：0否，1是*/
    private Boolean requiredFlag;

    /*是否主键: 0否，1是*/
    private Boolean uniqueFlag;

    /*日期类型格式*/
    private String dateFormat;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

}
