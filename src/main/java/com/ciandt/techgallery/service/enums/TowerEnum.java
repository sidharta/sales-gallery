package com.ciandt.techgallery.service.enums;

import com.google.gson.annotations.SerializedName;

public enum TowerEnum {
	
	@SerializedName("108")
	PACIFIC(108,"Pacific"),
	
	@SerializedName("134")
	PRODUCT_AND_LICENCES(134,"Product and Licences"),
	@SerializedName("27")
	RJ(27,"RJ"),
	@SerializedName("26")
	TELECOM(26,"Telecom"),
	@SerializedName("40")
	INTERNATIONAL(40,"[International]"),
	@SerializedName("110")
	INTERNATIONAL_NORTH_ATLANTIC(110,"[International] North Atlantic"),
	@SerializedName("111")
	INTERNATIONAL_NORTHEST(111,"[International] Northeast"),
	@SerializedName("112")
	INTERNATIONAL_SOUTHEST(112,"[International] Southeast"),
	@SerializedName("113")
	INTERNATIONAL_WEST(113,"[International] West"),
	@SerializedName("29")
	LATAM_BAKING(29,"[Latam] Banking"),
	@SerializedName("25")
	LATAM_COMMERCE(25,"[Latam] Commerce"),
	@SerializedName("23")
	LATAM_CONSUMER_GOODS(23,"[Latam] Consumer Goods"),
	@SerializedName("137")
	LATAM_INDUSTRY(137, "[Latam] Industry"),
	@SerializedName("28")
	LATAM_INSURANCE(28,"[Latam] Insurance"),
	@SerializedName("136")
	LATAM_PAYMENT_METHODS(136,"[Latam] Payment Methods"),
	@SerializedName("135")
	LATAM_RESOURCES_LOGISTICS(135,"[Latam] Resources & Logistics"),
	@SerializedName("24")
	LATAM_SERVICES(24,"[Latam] Services"),
	@SerializedName("163")
	PACIFIC_ASEAN(163,"[Pacific] ASEAN"),
	@SerializedName("164")
	PACIFIC_JAPAN(164,"[Pacific] Japan"),
	@SerializedName("165")
	PACIFIC_US_EUROPE(165,"[Pacific] US/Europe"),
	@SerializedName("186")
	LATAM_CONSUMER_LOGISTICS(186,"[LATAM] Consumer & Logistics");
	
	private int id;
	private String name;
	
	private TowerEnum(int id, String name){
		this.id = id;
		this.name = name;
	}
	

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
