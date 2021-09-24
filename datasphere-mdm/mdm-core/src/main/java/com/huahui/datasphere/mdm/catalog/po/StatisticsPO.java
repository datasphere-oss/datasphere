package com.huahui.datasphere.mdm.catalog.po;


/**
 * 数据统计情况，包含月份数和数据量
 */
public class StatisticsPO {

    /*表示月份数*/
    private String month;

    public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Long getRegCount() {
		return regCount;
	}

	public void setRegCount(Long regCount) {
		this.regCount = regCount;
	}

	public Long getPubCount() {
		return pubCount;
	}

	public void setPubCount(Long pubCount) {
		this.pubCount = pubCount;
	}

	public Long getSubCount() {
		return subCount;
	}

	public void setSubCount(Long subCount) {
		this.subCount = subCount;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}

	/*
	 * 表示统计数值大小
	 * 
	 * */
    private Long regCount;

    private Long pubCount;

    private Long subCount;

    /*
     * 租户ID,用于用于隔离
     * 
     * */
    private Long rentId;

}
