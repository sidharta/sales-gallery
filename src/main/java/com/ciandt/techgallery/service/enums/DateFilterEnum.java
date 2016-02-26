package com.ciandt.techgallery.service.enums;

/**
 * Enum for mapping Date Filters.
 *
 * @author Thulio Ribeiro
 *
 */
public enum DateFilterEnum {

  LAST_MONTH("Último mês"), LAST_6_MONTHS("Últimos seis meses"), LAST_12_MONTHS("Últimos doze meses");

  private String message;

  private DateFilterEnum(String message) {
    this.message = message;
  }

  public String message() {
    return message;
  }

}
