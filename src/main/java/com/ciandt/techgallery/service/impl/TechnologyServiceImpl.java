package com.ciandt.techgallery.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;

import com.ciandt.techgallery.persistence.dao.StorageDAO;
import com.ciandt.techgallery.persistence.dao.TechnologyDAO;
import com.ciandt.techgallery.persistence.dao.impl.TechnologyDAOImpl;
import com.ciandt.techgallery.persistence.dao.storage.StorageDAOImpl;
import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.TechnologyService;
import com.ciandt.techgallery.service.UserServiceTG;
import com.ciandt.techgallery.service.enums.StatusEnums;
import com.ciandt.techgallery.service.enums.TechnologyOrderOptionEnum;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.Response;
import com.ciandt.techgallery.service.model.TechnologiesResponse;
import com.ciandt.techgallery.service.model.TechnologyFilter;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;

/**
 * Services for Technology Endpoint requests.
 *
 * @author felipers
 *
 */
public class TechnologyServiceImpl implements TechnologyService {

	/*
	 * Attributes --------------------------------------------
	 */
	private static TechnologyServiceImpl instance;

	/** tech gallery user service. */
	UserServiceTG userService = UserServiceTGImpl.getInstance();
	TechnologyDAO technologyDAO = TechnologyDAOImpl.getInstance();
	StorageDAO storageDAO = StorageDAOImpl.getInstance();

	/*
	 * Constructors --------------------------------------------
	 */
	private TechnologyServiceImpl() {
	}

	/**
	 * Singleton method for the service.
	 *
	 * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros
	 *         Moreira </a>
	 * @since 07/10/2015
	 *
	 * @return TechnologyServiceImpl instance.
	 */
	public static TechnologyServiceImpl getInstance() {
		if (instance == null) {
			instance = new TechnologyServiceImpl();
		}
		return instance;
	}

	/*
	 * Methods --------------------------------------------
	 */
	@Override
	public Technology addOrUpdateTechnology(Technology technology, User user)
			throws BadRequestException, IOException, GeneralSecurityException {

		Technology foundTechnology = validateInformations(technology);
		Boolean isUpdate = foundTechnology != null && foundTechnology.getId().equals(technology.getId())
				&& foundTechnology.getActive().equals(Boolean.TRUE);

		String imageLink = technology.getImage();
		if (technology.getImageContent() != null) {
			imageLink = storageDAO.insertImage(technology.convertNameToId(technology.getName()),
					new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(technology.getImageContent())));
		}

		fillTechnology(technology, user, imageLink, isUpdate);

		technologyDAO.add(technology);

		return technology;
	}

	/**
	 * Fill a few informations about the technology.
	 *
	 * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros
	 *         Moreira </a>
	 * @since 13/10/2015
	 *
	 * @param technology
	 *            to be converted.
	 * @param user
	 *            to get informations.
	 * @param imageLink
	 *            returned by the cloud storage.
	 *
	 */
	private void fillTechnology(Technology technology, User user, String imageLink, Boolean isUptate) {
		technology.setId(technology.convertNameToId(technology.getName()));
		if (user != null && user.getEmail() != null) {
			if (!isUptate) {
				technology.setAuthor(user.getEmail());
			}
			technology.setLastActivityUser(user.getEmail());
		}
		technology.setActive(Boolean.TRUE);

		if(!isUptate){
			technology.setCreationDate(new Date());
		}

		technology.setLastActivity(new Date());
		technology.setImage(imageLink);
		technology.initCounters();
	}

	/**
	 * Method to validade informations of the technology to be added.
	 *
	 * @author <a href="mailto:joaom@ciandt.com"> João Felipe de Medeiros
	 *         Moreira </a>
	 * @since 13/10/2015
	 *
	 * @param technology
	 *            to be validated.
	 *
	 * @throws BadRequestException
	 *             in case a request with problem were made.
	 */
	private Technology validateInformations(Technology technology) throws BadRequestException {
		if (StringUtils.isBlank(technology.getId())) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_ID_CANNOT_BLANK.message());
		} else if (StringUtils.isBlank(technology.getName())) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_NAME_CANNOT_BLANK.message());
		} else if (StringUtils.isBlank(technology.getClient())) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_CLIENT_CANNOT_BLANK.message());
		} else if (technology.getOffers() == null || technology.getOffers().isEmpty()) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_OFFER_CANNOT_BLANK.message());
		} else if (StringUtils.isBlank(technology.getShortDescription())) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_SHORT_DESCRIPTION_BLANK.message());
		} else if (StringUtils.isBlank(technology.getDescription())) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_DESCRIPTION_BLANK.message());
		}

		Technology dbTechnology = technologyDAO.findByName(technology.getName());
		if (dbTechnology != null && technology.getId() == null) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_NAME_ALREADY_USED.message());
		}
		if (technology.getId() != null && dbTechnology != null
				&& !dbTechnology.getName().equals(technology.getName())) {
			throw new BadRequestException(ValidationMessageEnums.TECHNOLOGY_NAME_CANNOT_CHANGE.message());
		}
		return dbTechnology;
	}

	/**
	 * GET for getting all technologies.
	 *
	 * @throws NotFoundException
	 *             .
	 */
	@Override
	public Response getTechnologies(User user)
			throws InternalServerErrorException, NotFoundException, BadRequestException {
		List<Technology> techEntities = technologyDAO.findAllActives();
		// if list is null, return a not found exception
		if (techEntities == null || techEntities.isEmpty()) {
			return new TechnologiesResponse();
		} else {
			verifyTechnologyFollowedByUser(user, techEntities);
			TechnologiesResponse response = new TechnologiesResponse();
			Technology.sortTechnologiesDefault(techEntities);
			response.setTechnologies(techEntities);
			return response;
		}
	}

	private List<Technology> setDateFilteredList(List<Technology> completeList, Date dateReference) {
		List<Technology> dateFilteredList = new ArrayList<>();
		for (Technology technology : completeList) {
			if (technology.getCreationDate().after(dateReference)
					|| technology.getCreationDate().equals(dateReference)) {
				dateFilteredList.add(technology);
			}
		}
		return dateFilteredList;
	}

	private Date setDateReference(Date currentDate, int toSubtract) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.MONTH, toSubtract);
		Date dateReference = cal.getTime();
		return dateReference;
	}

	@Override
	public Response findTechnologiesByFilter(TechnologyFilter techFilter, User user)
			throws InternalServerErrorException, NotFoundException, BadRequestException {
		validateUser(user);
		if (techFilter.getStatusIs() != null
				&& techFilter.getStatusIs().equals(StatusEnums.UNINFORMED.message())) {
			techFilter.setStatusIs("");
		}

		List<Technology> completeList = technologyDAO.findAllActives();
		completeList = filterByLastActivityDate(techFilter, completeList);

		List<Technology> filteredList = new ArrayList<>();
		if (StringUtils.isBlank(techFilter.getTitleContains()) && techFilter.getStatusIs() == null && techFilter.getOffersIs() == null && StringUtils.isBlank(techFilter.getTowerIs())) {
			filteredList.addAll(completeList);
		} else {
			verifyFilters(techFilter, completeList, filteredList);
		}

		if (filteredList.isEmpty()) {
			return new TechnologiesResponse();
		} else {
			if (techFilter.getOrderOptionIs() != null && !techFilter.getOrderOptionIs().isEmpty()) {
				filteredList = Technology.sortTechnologies(filteredList,
						TechnologyOrderOptionEnum.fromString(techFilter.getOrderOptionIs()));
			} else {
				Technology.sortTechnologiesDefault(filteredList);
			}
			TechnologiesResponse response = new TechnologiesResponse();
			response.setTechnologies(filteredList);
			return response;
		}
	}

	private List<Technology> filterByLastActivityDate(TechnologyFilter techFilter, List<Technology> completeList) {
		List<Technology> dateFilteredList = new ArrayList<>();
		if (techFilter.getDateFilter() != null) {
			Date currentDate = new Date();
			switch (techFilter.getDateFilter()) {
			case LAST_MONTH:
				Date lastOne = setDateReference(currentDate, -1);
				dateFilteredList = setDateFilteredList(completeList, lastOne);
				break;

			case LAST_6_MONTHS:
				Date lastSix = setDateReference(currentDate, -6);
				dateFilteredList = setDateFilteredList(completeList, lastSix);
				break;

			case LAST_12_MONTHS:
				Date lastTwelve = setDateReference(currentDate, -12);
				dateFilteredList = setDateFilteredList(completeList, lastTwelve);
				break;
			default:
				break;
			}
			completeList = dateFilteredList;
		}
		return completeList;
	}

	private void verifyTechnologyFollowedByUser(User user, List<Technology> filteredList)
			throws NotFoundException, BadRequestException, InternalServerErrorException {
		TechGalleryUser techUser = userService.getUserByGoogleId(user.getUserId());
		if (techUser.getFollowedTechnologyIds() != null && !techUser.getFollowedTechnologyIds().isEmpty()) {
			for (Technology technology : filteredList) {
				if (techUser.getFollowedTechnologyIds().contains(technology.getId())) {
					technology.setFollowedByUser(true);
				}
			}
		}
	}

	private void verifyFilters(TechnologyFilter techFilter, List<Technology> completeList,
			List<Technology> filteredList) {
		for (Technology technology : completeList) {
			if (verifyTitleAndShortDescriptionFilter(techFilter, technology)) {
				if (techFilter.getStatusIs() != null) {
					if (verifyStatusFilter(techFilter, technology)) {
						filteredList.add(technology);
					} else {
						continue;
					}
				} else if (techFilter.getOffersIs() != null) {
					if (verifyOfferFilter(techFilter, technology)) {
						filteredList.add(technology);
					} else {
						continue;
					}
				} else if (StringUtils.isNotBlank(techFilter.getTowerIs())){
					if (verifyTowerFilter(techFilter, technology)){
						filteredList.add(technology);
					}else{
						continue;
					}
				}
				
				else {
					filteredList.add(technology);
					continue;
				}
			} else if (verifyStatusFilter(techFilter, technology) && techFilter.getTitleContains() == null) {
				filteredList.add(technology);
				continue;
			}else if (verifyOfferFilter(techFilter, technology) && techFilter.getTitleContains() == null){
				filteredList.add(technology);
				continue;
			}else if (verifyTowerFilter(techFilter, technology) && techFilter.getTitleContains() == null){
				filteredList.add(technology);
				continue;
			}
		}
	}

	private boolean verifyStatusFilter(TechnologyFilter techFilter, Technology technology) {
		if (technology.getStatus() == null && techFilter.getStatusIs() == "") {
			return true;
		} else if (technology.getStatus() != null && techFilter.getStatusIs() != null
				&& (technology.getStatus().toLowerCase().equals(techFilter.getStatusIs().toLowerCase())
						|| techFilter.getStatusIs().toLowerCase()
								.equals(StatusEnums.ANY.message().toLowerCase()))) {
			return true;
		}
		return false;
	}
	
	private boolean verifyTowerFilter(TechnologyFilter techFilter, Technology technology) {
		if ((technology.getTower() == null || technology.getTower().isEmpty()) && (StringUtils.isBlank(techFilter.getTowerIs()))) {
			return true;
		} else if (technology.getTower() != null && StringUtils.isNotBlank(techFilter.getTowerIs())){
			if (technology.getTower().toLowerCase().equals(techFilter.getTowerIs().toLowerCase()) || 
					techFilter.getTowerIs().toLowerCase().equals(StatusEnums.ANY.message().toLowerCase())){
				return true;
			}
		}
		return false;
	}

	private boolean verifyOfferFilter(TechnologyFilter techFilter, Technology technology) {
		if ((technology.getOffers() == null || technology.getOffers().isEmpty()) && (techFilter.getOffersIs() == null || techFilter.getOffersIs().isEmpty())) {
			return true;
		} else if (technology.getOffers() != null && techFilter.getOffersIs() != null){
			for(String offer : technology.getOffers()){
				for(String offerIs : techFilter.getOffersIs()){
					if (offer.toLowerCase().equals(offerIs.toLowerCase()) || offerIs.toLowerCase().equals(StatusEnums.ANY.message().toLowerCase())){
						return true;
					}
				}
			}
			
		}
		return false;
	}

	private boolean verifyTitleAndShortDescriptionFilter(TechnologyFilter techFilter, Technology technology) {
		if (techFilter.getTitleContains() == null){
			return false;
		}
		if (checkTitle(techFilter, technology)
				|| checkCustomerName(techFilter, technology)
				|| checkDescription(techFilter, technology)
				|| checkShortDescription(techFilter, technology)
				|| checkTower(techFilter, technology)
				|| checkOffer(techFilter, technology)
				|| checkTechnologies(techFilter, technology)) {
			return true;
		}
		return false;
	}
	
	private boolean checkTower(TechnologyFilter techFilter, Technology technology){
		return !StringUtils.isEmpty(technology.getTower()) && technology.getTower().toLowerCase().contains(techFilter.getTitleContains().toLowerCase());
	}
		
	private boolean checkOffer(TechnologyFilter techFilter, Technology technology){
		if (technology.getOffers() == null)	 return false;		
		 for (String offer : technology.getOffers()){	 
			if (offer.toLowerCase().contains(techFilter.getTitleContains().toLowerCase())) {
				return true;
			 }
			 
		 }
		return false;
	}
	

	private boolean checkTechnologies(TechnologyFilter techFilter, Technology technology) {
		return technology.getTechnologies() != null && technology.getTechnologies().toLowerCase()
				.contains(techFilter.getTechnologiesContains().toString().toLowerCase());
	}

	private boolean checkCustomerName(TechnologyFilter techFilter, Technology technology) {
		return technology.getClient() != null && technology.getClient().toLowerCase()
				.contains(techFilter.getCustomerNameContains().toString().toLowerCase());
	}

	private boolean checkDescription(TechnologyFilter techFilter, Technology technology) {
		return technology.getDescription() != null && technology.getDescription().toLowerCase()
				.contains(techFilter.getShortDescriptionContains().toLowerCase());
	}

	private boolean checkShortDescription(TechnologyFilter techFilter, Technology technology) {
		return technology.getShortDescription() != null && technology.getShortDescription().toLowerCase()
				.contains(techFilter.getShortDescriptionContains().toLowerCase());
	}

	private boolean checkTitle(TechnologyFilter techFilter, Technology technology) {
		return technology.getName() != null && technology.getName().toLowerCase().contains(techFilter.getTitleContains().toLowerCase());
	}

	@Override
	public Technology getTechnologyById(String id, User user)
			throws NotFoundException, BadRequestException, InternalServerErrorException {
		Technology tech = technologyDAO.findByIdActive(id);
		if (tech == null) {
			throw new NotFoundException(ValidationMessageEnums.TECHNOLOGY_NOT_EXIST.message());
		} else {
			if (user != null) {
				TechGalleryUser techUser = userService.getUserByGoogleId(user.getUserId());
				if (techUser.getFollowedTechnologyIds() != null
						&& techUser.getFollowedTechnologyIds().contains(tech.getId())) {
					tech.setFollowedByUser(true);
				}
			}
			return tech;
		}
	}

	/**
	 * Validate the user logged in.
	 *
	 * @param user
	 *            info about user from google
	 * @throws InternalServerErrorException
	 *             in case something goes wrong
	 * @throws NotFoundException
	 *             in case the information are not founded
	 * @throws BadRequestException
	 *             in case a request with problem were made.
	 */
	private void validateUser(User user) throws BadRequestException, NotFoundException, InternalServerErrorException {

		if (user == null || user.getUserId() == null || user.getUserId().isEmpty()) {
			throw new BadRequestException(ValidationMessageEnums.USER_GOOGLE_ENDPOINT_NULL.message());
		}

		TechGalleryUser techUser = userService.getUserByGoogleId(user.getUserId());
		if (techUser == null) {
			throw new NotFoundException(ValidationMessageEnums.USER_NOT_EXIST.message());
		}
	}

	@Override
	public List<String> getOrderOptions(User user) {
		List<String> orderOptions = new ArrayList<String>();
		for (TechnologyOrderOptionEnum item : TechnologyOrderOptionEnum.values()) {
			orderOptions.add(item.option());
		}
		return orderOptions;
	}

	@Override
	public void addCommentariesCounter(Technology entity) {
		if (entity != null) {
			entity.addCommentariesCounter();
		}
		technologyDAO.update(entity);
	}

	@Override
	public void removeCommentariesCounter(Technology entity) {
		if (entity != null) {
			entity.removeCommentariesCounter();
		}
		technologyDAO.update(entity);

	}

	@Override
	public void updateEdorsedsCounter(Technology technology, Integer size) {
		technology.setEndorsersCounter(size);
		technologyDAO.update(technology);
	}

	@Override
	public void audit(String technologyId, User user)
			throws NotFoundException, BadRequestException, InternalServerErrorException {
		Technology technology = getTechnologyById(technologyId, user);
		technology.setLastActivity(new Date());
		technology.setLastActivityUser(user.getEmail());
		technologyDAO.update(technology);
	}

	@Override
	public Technology deleteTechnology(String technologyId, User user)
			throws InternalServerErrorException, BadRequestException, NotFoundException, OAuthRequestException {
		validateUser(user);
		Technology technology = technologyDAO.findById(technologyId);
		if (technology == null) {
			throw new NotFoundException(ValidationMessageEnums.NO_TECHNOLOGY_WAS_FOUND.message());
		}
		technology.setActive(Boolean.FALSE);
		technology.setLastActivity(new Date());
		technology.setLastActivityUser(user.getEmail());
		technologyDAO.update(technology);
		return technology;
	}
}
