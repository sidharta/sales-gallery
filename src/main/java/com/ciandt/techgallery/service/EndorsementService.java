package com.ciandt.techgallery.service;

import com.ciandt.techgallery.service.model.EndorsementResponse;
import com.ciandt.techgallery.service.model.Response;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

/**
 * Services for Endorsements.
 * 
 * @author felipers
 *
 */
public interface EndorsementService {

  /**
   * Service for adding a endorsement.
   * 
   * @param endorsement json with endorsement info.
   * @param user current user logged.
   * @return endorsement info or message error.
   * @throws InternalServerErrorException
   * @throws BadRequestException
   * @throws NotFoundException 
   */
  public Response addOrUpdateEndorsement(final EndorsementResponse endorsement, final User user)
      throws InternalServerErrorException, BadRequestException, NotFoundException;

  /**
   * Service for getting all endorsements.
   * 
   * @return endorsements info or message error.
   * @throws InternalServerErrorException
   * @throws NotFoundException
   */
  public Response getEndorsements() throws InternalServerErrorException, NotFoundException;

  /**
   * Service for getting a endorsement.
   * 
   * @param id entity id.
   * @return
   * @throws NotFoundException
   */
  public Response getEndorsement(final Long id) throws NotFoundException;
}
