package com.ciandt.techgallery.service;

import java.util.List;

import com.ciandt.techgallery.service.model.DealTO;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;


public interface PipedriveService {
	
	  /**
	   * Service for getting a list of pipidrive deal.
	   *
	   * @return deal of pipedrive
	   * @throws NotFoundException when entity is not found
	   * @throws InternalServerErrorException in case something goes wrong
	   */
	  DealTO getPipedriveDeal(String id, User user) throws Exception;
	  

	  /**
	   * Service for getting a list of possibles offers.
	   *
	   * @return List of offers
	   * @throws NotFoundException when entity is not found
	   * @throws InternalServerErrorException in case something goes wrong
	   */
	  List<String> getOffers(User user)
	      throws NotFoundException, BadRequestException, InternalServerErrorException;
	  
	  
	  /**
	   * Service for getting a list of possibles towers.
	   *
	   * @return List of towers
	   * @throws NotFoundException when entity is not found
	   * @throws InternalServerErrorException in case something goes wrong
	   */
	  List<String> getTowers(User user)
	      throws NotFoundException, BadRequestException, InternalServerErrorException;
	

}
