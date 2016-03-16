package com.ciandt.techgallery.service.enums;

/**
 * Enum for mapping recommendations.
 *
 * @author Edgard Cardoso
 *
 */
public enum OfferEnums {

	GOOGLE_APPS(63, "Google Apps"),
	GOOGLE_CLOUD(64, "Google Cloud"),
	GOOGLE_OTHERS(81, "Google: Others"),
	SMART_CANVAS_B2C(61,"Smart Canvas B2C"),
	SMART_CANVAS_B2E(61,"Smart Canvas B2E"),
	TABLEAU(65,"Tableau"),
	DIGITAL(106,"Digital"),
	EMETRIX(104,"eMetrix"),
	HYBRIS(105,"Hybris"),
	API_EXTENDED_ENTERPRISE(212,"API Extended Enterprise"),
	BUSINESS_TECHNOLOGY_AGILITY(213,"Business-Technology Agility"),
	DIGITAL_IMPLEMENTATION(214,"Digital Implementation"),
	DIGITAL_STRATEGY(215,"Digital Strategy"),
	ENTERPRISE_AGILE(216,"Enterprise Agile"),
	LEGACY_OPTIMIZATION(217,"Legacy Optimization"),
	IOT(218,"IoT"),
	RECURRING_LEGACY_OPTIMIZATION(229,"Recurring: Legacy Optimization"),
	RECURRING_ENTERPRISE_AGILE(230,"Recurring: Enterprise Agile"),
	RECURRING_SAAS(231,"Recurring: SAAS"),
	PROJECT_BASED_STRATEGY_INNOVATION(232, "Project-based: Strategy & Innovation");
	
	
	private int id;
	private String name;
	
	private OfferEnums(int id, String name){
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
	
	public static OfferEnums fromId(Integer id) {
	    if (id != null) {
	      for (OfferEnums offer : OfferEnums.values()) {
	        if (id.equals(offer.getId())) {
	          return offer;
	        }
	      }
	    }
	    return null;
	  }

}
