package com.ciandt.techgallery.service.enums;

/**
 * Enum for mapping Offers.
 * 
 * @author Fabio Viana
 *
 */
public enum OfferEnum {

  SM("Smart Canvas"), DSC("Digital Strategy Consulting"), LO("Legacy Optimization"),
  API("API-Extended Enterprise"), DI("Digital Implementation"), EA("Enterprise Agile"),
  BTAC("Business-Technology Agile Consulting"), IOT("IoT Experimentation"), GC("Google Cloud");

  private String message;

  private OfferEnum(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }
}