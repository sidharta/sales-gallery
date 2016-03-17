package com.ciandt.techgallery.service.model.pipedrive.webhook;

import com.ciandt.techgallery.service.enums.TowerEnum;
import com.google.gson.annotations.SerializedName;

public class Deal {

	private Long id;
	private Long stageOrderNr;
	private String title;
	private String orgName;
	private Long userId;
	private String status;

	@SerializedName("7b4743f02e683528a10ec09ed249726b5adcf6ad")
	private TowerEnum tower;
	
	@SerializedName("e16df82dc89790231a2169c6ee3d4b79a2230036")
	private String products;

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

	public TowerEnum getTower() {
		return tower;
	}

	public void setTower(TowerEnum tower) {
		this.tower = tower;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProducts() {
		return products;
	}

	public void setProducts(String products) {
		this.products = products;
	}
}
