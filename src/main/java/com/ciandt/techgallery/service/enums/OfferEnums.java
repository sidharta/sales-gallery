package com.ciandt.techgallery.service.enums;

/**
 * Enum for mapping recommendations.
 *
 * @author Edgard Cardoso
 *
 */
public enum OfferEnums {

  SMART_CANVAS("Smart Canvas"),
  DIGITAL_STRATEGY_CONSULTING("Digital Strategy Consulting"),
  LEGACY_OPTIMIZATION("Legacy Optimization"),
  API_EXTENDED_ENTERPRISE("API-Extended Enterprise"),
  DIGITAL_IMPLEMENTATION("Digital Implementation"),
  BUSINESS_TECHNOLOGY_AGILE_CONSULTING("Business-Technology Agile Consulting"),
  IOT_EXPERIMENTATION("IoT Experimentation"),
  GOOGLE_CLOUD("Google Cloud"),
  ENTERPRISE_AGILE("Enterprise Agile");


  private String message;

  private OfferEnums(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }

}
