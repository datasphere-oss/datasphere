package com.huahui.datasphere.table.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModelProperty.AccessMode;

public class DMTableStorageInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
		
	@Id
	@Column(name = "id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "储存ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private Long id;
	
	
	
	@Column(name = "file_format", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "file_format cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(example = "文件格式:0-定长 1-非定长 2-CSV 3-SEQUENCEFILE 4-PARQUET 5-ORC")
	private String fileFormat;
	
	
	@Id
	@Column(name = "table_id", nullable = false, updatable = false, columnDefinition = "CHAR(36)")
	@NotNull(message = "table_id cannot be null")
	@Size(max = 36)
	@ApiModelProperty(required = true, value = "表ID", example = "12345678-abcd-90ab-cdef-1234567890ab")
	private Long tableId;
	
	
	@Column(name = "storage_type", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "file_format cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(example = "进数方式:2-追加 3-替换 4-全量拉链 5-增量拉链")
	private String storageType;
	
	
	@CreationTimestamp
	@Column(name = "storage_time", nullable = false, updatable = false, columnDefinition = "BIGINT")
	@NotNull(message = "storage_time cannot be null")
	// REST clients should not send this property
	@ApiModelProperty(accessMode = AccessMode.READ_ONLY, value = "存储期限(以天为单位)", example = "365")
	private Long storageTime;
	
	@Column(name = "is_zipper", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "file_format cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(value = "是否拉链存储:1-是 0-否", example = "")
	private String isZipper;
	
	
	@Column(name = "dss_name", columnDefinition = "VARCHAR(1024)")
	@NotNull(message = "dss_name cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(value = "是否拉链存储:1-是 0-否", example = "")
	
	private String DSSName;

	@Column(name = "is_prefix", columnDefinition = "CHAR(36)")
	@NotNull(message = "is_prefix cannot be null")
	@Size(max = 1024)
	@ApiModelProperty(value = "是否为前缀", example = "")
	private String isPrefix;
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getFileFormat() {
		return fileFormat;
	}


	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}


	public Long getTableId() {
		return tableId;
	}


	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}


	public String getStorageType() {
		return storageType;
	}


	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}


	public Long getStorageTime() {
		return storageTime;
	}


	public void setStorageTime(Long storageTime) {
		this.storageTime = storageTime;
	}


	public String getIsZipper() {
		return isZipper;
	}


	public void setIsZipper(String isZipper) {
		this.isZipper = isZipper;
	}


	public String getDSSName() {
		return DSSName;
	}


	public void setDSSName(String dSSName) {
		DSSName = dSSName;
	}
	

}
