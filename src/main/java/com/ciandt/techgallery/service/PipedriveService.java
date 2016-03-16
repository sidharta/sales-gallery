package com.ciandt.techgallery.service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import com.ciandt.techgallery.service.model.pipedrive.DealTO;
import com.ciandt.techgallery.service.model.pipedrive.webhook.Deal;
import com.google.api.server.spi.response.BadRequestException;
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
	  
	  void saveFromWebhook(Deal deal) throws BadRequestException, IOException, GeneralSecurityException;
	

}
