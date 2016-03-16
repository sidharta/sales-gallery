package com.ciandt.techgallery.service.model.pipedrive.webhook;

import com.google.gson.annotations.SerializedName;

public class Deal {

	private Long id;
	private Long stageOrderNr;
	private String title;
	private String orgName;
	private Long userId;
	private String status;

	@SerializedName("2584f6868b4876c0a1784795075d865ed3be0abe")
	private Tower tower;
	
	@SerializedName("ad046fd66ed1e5a0c5052ccaa85b9909a3f9c3cd")
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getStageOrderNr() {
		return stageOrderNr;
	}

	public void setStageOrderNr(Long stageOrderNr) {
		this.stageOrderNr = stageOrderNr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Tower getTower() {
		return tower;
	}

	public void setTower(Tower tower) {
		this.tower = tower;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
