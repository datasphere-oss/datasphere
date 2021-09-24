package com.huahui.datasphere.mdm.catalog.po;


import java.util.Date;

/**
 * 资源目录节点分类表
 * @Author: Wangbin
 * @Date: 2018/5/23
 */

public class CatalogNodePO {

    /*
     * 目录ID使用 32位
     * */
    private Long id;

    /*
     * 父节点 id
     * */
    private Long parentId;

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentFullCode() {
		return parentFullCode;
	}

	public void setParentFullCode(String parentFullCode) {
		this.parentFullCode = parentFullCode;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceEncode() {
		return resourceEncode;
	}

	public void setResourceEncode(String resourceEncode) {
		this.resourceEncode = resourceEncode;
	}

	public int getDept() {
		return dept;
	}

	public void setDept(int dept) {
		this.dept = dept;
	}

	public Long getRentId() {
		return rentId;
	}

	public void setRentId(Long rentId) {
		this.rentId = rentId;
	}

	public Long getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(Long resourceCount) {
		this.resourceCount = resourceCount;
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

	/*父节点ID 全称*/
    private String parentFullCode;

    /*节点资源名称*/
    private String resourceName;

    /*节点资源编码，为不同长度数据，类、项目、目、细目：长度数字分别长度为：1位、2位、3位、不定长度*/
    private String resourceEncode;

    /*节点所在层级深度，分为类，项目，目，细目*/
    private int dept;

    /*租户ID，实现组合隔离*/
    private Long rentId;

    /*目录分类节点资源个数统计*/
    private Long resourceCount;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date modifyTime;

    public CatalogNodePO(){
        super();
    }

    public CatalogNodePO(String name, String code, Long rentId, String user){
        this.parentId = 0L;
        this.parentFullCode = "0";
        this.resourceName = name;
        this.resourceEncode = code;
        this.dept = 1;
        this.rentId = rentId;
        this.creator = user;
        this.modifier = user;
        this.createTime = new Date();
        this.modifyTime = new Date();
    }
}
