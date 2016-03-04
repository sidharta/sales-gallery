package com.ciandt.techgallery.service;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import java.util.List;

/**
 * Services for Offer.
 * 
 * @author Edgard Cardoso
 *
 */
public interface OfferService {

  /**
   * Service for getting a list of possibles offers.
   *
   * @return List of offers
   * @throws NotFoundException when entity is not found
   * @throws InternalServerErrorException in case something goes wrong
   */
  List<String> getOffers(User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException;

}
