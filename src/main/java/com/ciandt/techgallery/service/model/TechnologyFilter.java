package com.ciandt.techgallery.service.model;

import com.ciandt.techgallery.service.enums.DateFilterEnum;
import com.ciandt.techgallery.service.enums.OfferEnums;

/**
 * Response with all technology entities.
 *
 * @author Thulio Ribeiro
 *
 */
public class TechnologyFilter implements Response {

  /** string for search in title. */
  private String titleContains;

  /** string for search in shortDescription. */
  private String shortDescriptionContains;

  /** string for search in status. */
  private String statusIs;

  /** string for order option. */
  private String orderOptionIs;

  /** string for order option. */
  private DateFilterEnum dateFilter;

  /** string for search in customerName. */
  private String customerNameContains;

  /** string for search in offer. */
  private String offerIs;

  /** string for search in technologies. */
  private String technologiesContains;

  public String getCustomerNameContains() {
    return customerNameContains;
  }
  public void setCustomerNameContains(String value) {
    this.customerNameContains = value;
  }

  public String getOfferIs() {
    return offerIs;
  }

  public void setOfferIs(String value) {
    this.offerIs = value;
  }

  public String getTechnologiesContains() {
    return technologiesContains;
  }
  public void setTechnologiesContains(String value) {
    this.technologiesContains = value;
  }

  public String getTitleContains() {
    return titleContains;
  }

  public String getStatusIs() {
    return statusIs;
  }

  public void setStatusIs(String statusIs) {
    this.statusIs = statusIs;
  }

  public void setTitleContains(String titleContains) {
    this.titleContains = titleContains;
  }

  public String getShortDescriptionContains() {
    return shortDescriptionContains;
  }

  public DateFilterEnum getDateFilter() {
    return dateFilter;
  }

  public void setDateFilter(DateFilterEnum dateFilter) {
    this.dateFilter = dateFilter;
  }

  public void setShortDescriptionContains(String shortDescriptionContains) {
    this.shortDescriptionContains = shortDescriptionContains;
  }

  public String getOrderOptionIs() {
    return orderOptionIs;
  }

  public void setOrderOptionIs(String orderOptionIs) {
    this.orderOptionIs = orderOptionIs;
  }

  public TechnologyFilter() {
  }

  /**
   * Construtor for TechnologyFilter.
   *
   * @param titleContains
   *          part of the technology's title
   * @param shortDescriptionContains
   *          titleContains part of the technology's short description
   * @param statusIs
   *          technology's Ci&T status
   * @param orderOptionIs
   *          for sort the result
   */
  public TechnologyFilter(String titleContains, String shortDescriptionContains, String statusIs,
      Integer dateFilter, String orderOptionIs, String customerNameContains, String offerIs, String technologiesContains) {
    this.titleContains = titleContains;
    this.shortDescriptionContains = shortDescriptionContains;
    this.statusIs = statusIs;
    this.orderOptionIs = orderOptionIs;
    if (dateFilter != null && (dateFilter >= 0 && dateFilter <= 2)) {
      this.dateFilter = DateFilterEnum.values()[dateFilter];
    }
    this.customerNameContains = customerNameContains;
    this.offerIs = offerIs;
    this.technologiesContains = technologiesContains;
  }
}
