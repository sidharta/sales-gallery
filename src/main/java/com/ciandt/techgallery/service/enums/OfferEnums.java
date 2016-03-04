package com.ciandt.techgallery.service.enums;

/**
 * Enum for mapping recommendations.
 *
 * @author Edgard Cardoso
 *
 */
public enum OfferEnums {

  API_EXTENDED_ENTERPRISE("API-Extended Enterprise"),
  BUSINESS_TECHNOLOGY_AGILE_CONSULTING("Business-Technology Agile Consulting"),
  DIGITAL_IMPLEMENTATION("Digital Implementation"),
  DIGITAL_STRATEGY_CONSULTING("Digital Strategy Consulting"),
  ENTERPRISE_AGILE("Enterprise Agile"),
  GOOGLE_CLOUD("Google Cloud"),
  IOT_EXPERIMENTATION("IoT Experimentation"),
  LEGACY_OPTIMIZATION("Legacy Optimization"),
  SMART_CANVAS("Smart Canvas");

  private String message;

  private OfferEnums(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }

}
