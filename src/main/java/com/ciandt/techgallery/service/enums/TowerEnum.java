package com.ciandt.techgallery.service.enums;

public enum TowerEnum {
	
	PACIFIC(108,"Pacific"),
	PRODUCT_AND_LICENCES(134,"Product and Licences"),
	RJ(27,"RJ"),
	TELECOM(26,"Telecom"),
	INTERNATIONAL(40,"[International]"),
	INTERNATIONAL_NORTH_ATLANTIC(110,"[International] North Atlantic"),
	INTERNATIONAL_NORTHEST(111,"[International] Northeast"),
	INTERNATIONAL_SOUTHEST(112,"[International] Southeast"),
	INTERNATIONAL_WEST(113,"[International] West"),
	LATAM_BAKING(29,"[Latam] Banking"),
	LATAM_COMMERCE(25,"[Latam] Commerce"),
	LATAM_CONSUMER_GOODS(23,"[Latam] Consumer Goods"),
	LATAM_INDUSTRY(137, "[Latam] Industry"),
	LATAM_INSURANCE(28,"[Latam] Insurance"),
	LATAM_PAYMENT_METHODS(136,"[Latam] Payment Methods"),
	LATAM_RESOURCES_LOGISTICS(135,"[Latam] Resources & Logistics"),
	LATAM_SERVICES(24,"[Latam] Services"),
	PACIFIC_ASEAN(163,"[Pacific] ASEAN"),
	PACIFIC_JAPAN(164,"[Pacific] Japan"),
	PACIFIC_US_EUROPE(165,"[Pacific] US/Europe"),
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
