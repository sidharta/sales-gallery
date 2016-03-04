package com.ciandt.techgallery.service.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.Constants;
import com.ciandt.techgallery.service.OfferService;
import com.ciandt.techgallery.service.impl.OfferServiceImpl;
import com.ciandt.techgallery.service.model.Response;

import java.util.List;

/**
 * Endpoint controller class for Offer requests. Recommendations are used only for
 * technologies.
 *
 * @author Edgard Cardoso
 *
 */
@Api(name = "rest", version = "v1", clientIds = {Constants.WEB_CLIENT_ID,
    Constants.API_EXPLORER_CLIENT_ID}, scopes = {Constants.EMAIL_SCOPE, Constants.PLUS_SCOPE,
    Constants.PLUS_STREAM_WRITE})
public class OfferEndpoint {

  private OfferService service = OfferServiceImpl.getInstance();


  @ApiMethod(name = "getOffers", path = "offer", httpMethod = "get")
  public List<String> getOffers(User user) throws InternalServerErrorException,
      NotFoundException, BadRequestException {
    return service.getOffers(user);
  }

}
