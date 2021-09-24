package com.huahui.datasphere.mdm.catalog.po;




public class CatalogResourcePO implements Comparable<CatalogResourcePO>{

    private Long catalogId;

    private Long resourceId;

    public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	private int depth;

    @Override
    public int compareTo(CatalogResourcePO crPO) {
        return depth - crPO.getDepth();
    }
}
