package com.ciandt.techgallery.service.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

import com.ciandt.techgallery.Constants;
import com.ciandt.techgallery.service.StatusService;
import com.ciandt.techgallery.service.impl.StatusServiceImpl;
import com.ciandt.techgallery.service.model.Response;

import java.util.List;

/**
 * Endpoint controller class for Statuss requests. Status are used only for
 * technologies.
 *
 * @author felipers
 *
 */
@Api(name = "rest", version = "v1", clientIds = {Constants.WEB_CLIENT_ID,
    Constants.API_EXPLORER_CLIENT_ID}, scopes = {Constants.EMAIL_SCOPE, Constants.PLUS_SCOPE,
    Constants.PLUS_STREAM_WRITE})
public class StatusEndpoint {

  private StatusService service = StatusServiceImpl.getInstance();

  @ApiMethod(name = "getStatus", path = "status", httpMethod = "get")
  public List<String> getStatus(User user) throws InternalServerErrorException,
      NotFoundException, BadRequestException {
    return service.getStatus(user);
  }

}
