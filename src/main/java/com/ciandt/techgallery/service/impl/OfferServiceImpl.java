package com.ciandt.techgallery.service.impl;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.service.impl.OfferServiceImpl;
import com.ciandt.techgallery.service.UserServiceTG;
import com.ciandt.techgallery.service.OfferService;
import com.ciandt.techgallery.service.enums.OfferEnums;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Services for Oferr Endpoint requests.
 *
 * @author Edgard Cardoso
 *
 */
public class OfferServiceImpl implements OfferService {

  UserServiceTG userService = UserServiceTGImpl.getInstance();
  private static OfferServiceImpl instance;

  private OfferServiceImpl() {}

  /**
   * Singleton method for the service.
   *
   * @return OfferServiceImpl instance.
   */
  public static OfferServiceImpl getInstance() {
    if (instance == null) {
      instance = new OfferServiceImpl();
    }
    return instance;
  }

  @Override
  public List<String> getOffers(User user)
      throws NotFoundException, BadRequestException, InternalServerErrorException {
    validateUser(user);
    final List<OfferEnums> enumValues = Arrays.asList(OfferEnums.values());
    final List<String> offers = new ArrayList<>();
    for (final OfferEnums enumEntry : enumValues) {
      offers.add(enumEntry.message());
    }
    return offers;
  }

  /**
   * Validate the user logged in.
   *
   * @param user info about user from google
   * @throws InternalServerErrorException in case something goes wrong
   * @throws NotFoundException in case the information are not founded
   * @throws BadRequestException in case a request with problem were made.
   */
  private void validateUser(User user)
      throws BadRequestException, NotFoundException, InternalServerErrorException {

    if (user == null || user.getUserId() == null || user.getUserId().isEmpty()) {
      throw new BadRequestException(ValidationMessageEnums.USER_GOOGLE_ENDPOINT_NULL.message());
    }

    final TechGalleryUser techUser = userService.getUserByGoogleId(user.getUserId());
    if (techUser == null) {
      throw new NotFoundException(ValidationMessageEnums.USER_NOT_EXIST.message());
    }
  }

}
