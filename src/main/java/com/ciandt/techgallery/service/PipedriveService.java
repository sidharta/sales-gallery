package com.ciandt.techgallery.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import com.ciandt.techgallery.service.model.pipedrive.DealTO;
import com.ciandt.techgallery.service.model.pipedrive.webhook.Deal;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

public interface PipedriveService {

	/**
	 * Service for getting a list of pipidrive deal.
	 *
	 * @return deal of pipedrive
	 * @throws NotFoundException
	 *             when entity is not found
	 * @throws InternalServerErrorException
	 *             in case something goes wrong
	 */
	DealTO getPipedriveDeal(String id, User user) throws Exception;

	/**
	 * Persist deal from webhook.
	 * 
	 * @param deal
	 * @throws BadRequestException
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	void saveFromWebhook(Deal deal) throws BadRequestException, IOException, GeneralSecurityException;

	/**
	 * Service for getting a list of possibles offers.
	 *
	 * @return List of offers
	 * @throws NotFoundException
	 *             when entity is not found
	 * @throws InternalServerErrorException
	 *             in case something goes wrong
	 */
	List<String> getOffers(User user) throws NotFoundException, BadRequestException, InternalServerErrorException;

	/**
	 * Service for getting a list of possibles towers.
	 *
	 * @return List of towers
	 * @throws NotFoundException
	 *             when entity is not found
	 * @throws InternalServerErrorException
	 *             in case something goes wrong
	 */
	List<String> getTowers(User user) throws NotFoundException, BadRequestException, InternalServerErrorException;
}
