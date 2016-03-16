package com.ciandt.techgallery.service.model;

import java.util.List;

/**
 * 
 *
 * @author edgardc
 *
 */
public class DealTO {

	private String name;
	private String client;
	private String ownerEmail;
	private String ownerName;
	private String status;
	private List<String> offers;
	private String tower;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String owner) {
		this.ownerEmail = owner;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public List<String> getOffers() {
		return offers;
	}
	public void setOffers(List<String> offers) {
		this.offers = offers;
	}
	public String getTower() {
		return tower;
	}
	public void setTower(String tower) {
		this.tower = tower;
	}


	
	
	
}
