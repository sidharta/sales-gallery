package com.ciandt.techgallery.service.enums;

/**
 * Enum for mapping statuses.
 *
 * @author Sidharta Noleto
 *
 */
public enum StatusEnums {

  ANY("Todos"),
  IN_PROPOSAL("open"),
  LOST("lost"),
  WON("won"),
  NOT_PROVIDED("Not provided");

  private String message;

  private StatusEnums(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }

}
