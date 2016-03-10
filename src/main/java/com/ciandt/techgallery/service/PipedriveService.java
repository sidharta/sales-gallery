package com.ciandt.techgallery.service;

import com.ciandt.techgallery.service.model.DealTO;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;


public interface PipedriveService {
	
	  /**
	   * Service for getting a list of pipidrive deal.
	   *
	   * @return deal of pipedrive
	   * @throws NotFoundException when entity is not found
	   * @throws InternalServerErrorException in case something goes wrong
	   */
	  DealTO getPipedriveDeal(String id) throws Exception;
	

}
