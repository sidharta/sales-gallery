package com.ciandt.techgallery.service.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;

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
import com.ciandt.techgallery.service.model.TechModelTo;
import com.ciandt.techgallery.service.model.TechnologiesResponse;
import com.ciandt.techgallery.service.model.TechnologyFilter;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

		if (!isUptate) {
			technology.setCreationDate(new Date());
		}

		if (StringUtils.isNotBlank(technology.getPipedriveLink())) {
			technology.setPipedriveLink(StringUtils.replace(technology.getPipedriveLink(), "/deal/", "/deal/view/"));
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

		List<Technology> completeList = technologyDAO.findAllActives();
		List<Technology> filteredList = verifyFilters(techFilter, completeList);

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

	private Date findDateReference(TechnologyFilter techFilter) {
		Date currentDate = new Date();
		Date ref = new Date();

		if (techFilter.getDateFilter() != null) {
			switch (techFilter.getDateFilter()) {
				case LAST_MONTH:
					ref = setDateReference(currentDate, -1);
					break;

				case LAST_6_MONTHS:
					ref = setDateReference(currentDate, -6);
					break;

				case LAST_12_MONTHS:
					ref = setDateReference(currentDate, -12);
					break;
				default:
					break;
			}
		}

		return ref;
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

	private List<Technology> verifyFilters(final TechnologyFilter techFilter, List<Technology> completeList) {

		Predicate<Technology> filtraStatus = new Predicate<Technology>(){
			public boolean apply(Technology t){
				return isFilterNullOrAny(techFilter.getStatusIs())
								|| (t.getStatus() != null && t.getStatus().toLowerCase().equals(techFilter.getStatusIs().toLowerCase()));
			}
		};

		Predicate<Technology> filtraOffer = new Predicate<Technology>(){
			public boolean apply(Technology t){
				return techFilter.getOffersIs() == null
								|| Iterables.isEmpty(techFilter.getOffersIs())
								|| (t.getOffers() != null && techFilter.getOffersIs().retainAll(t.getOffers()));
			}
		};

		Predicate<Technology> filtraTower = new Predicate<Technology>(){
			public boolean apply(Technology t){
				return isFilterNullOrAny(techFilter.getTowerIs())
								|| (t.getTower() != null && t.getTower().toLowerCase().equals(techFilter.getTowerIs().toLowerCase()));
			}
		};

		Predicate<Technology> filtraTexto = new Predicate<Technology>(){
			public boolean apply(Technology t){
				return isFilterNullOrAny(techFilter.getTitleContains())
								|| (checkTitle(techFilter, t) || checkCustomerName(techFilter, t)
										|| checkDescription(techFilter, t) || checkShortDescription(techFilter, t)
										|| checkTower(techFilter, t) || checkOffer(techFilter, t)
										|| checkTechnologies(techFilter, t));
			}
		};

		final Date dateReference = findDateReference(techFilter);

		Predicate<Technology> filtraData = new Predicate<Technology>(){
			public boolean apply(Technology t){
				return techFilter.getDateFilter() == null
								|| (t.getCreationDate().after(dateReference) || t.getCreationDate().equals(dateReference));
			}
		};

		return Lists.newArrayList(Iterables.filter(completeList, Predicates.and(filtraStatus, filtraOffer, filtraTower, filtraTexto, filtraData)));
	}

	private boolean isFilterNullOrAny(String filter){
		return filter == null || filter.toLowerCase().equals(StatusEnums.ANY.message().toLowerCase());
	}

	private boolean checkTower(TechnologyFilter techFilter, Technology technology) {
		return !StringUtils.isEmpty(technology.getTower())
				&& technology.getTower().toLowerCase().contains(techFilter.getTitleContains().toLowerCase());
	}

	private boolean checkOffer(TechnologyFilter techFilter, Technology technology) {
		if (technology.getOffers() == null)
			return false;
		for (String offer : technology.getOffers()) {
			if (offer.toLowerCase().contains(techFilter.getTitleContains().toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private boolean checkTechnologies(TechnologyFilter techFilter, Technology technology) {
		if (technology.getTechnologies() == null)
			return false;
		for (String item : technology.getTechnologies()) {
			if (item.toLowerCase().contains(techFilter.getTitleContains().toLowerCase())) {
				return true;
			}
		}
		return false;
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
		return technology.getName() != null
				&& technology.getName().toLowerCase().contains(techFilter.getTitleContains().toLowerCase());
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

	@Override
	public List<String> findTechnologiesByName(String name, User user)
			throws InternalServerErrorException, NotFoundException, BadRequestException {

		InputStream resourceStream = TechnologyServiceImpl.class.getClassLoader()
				.getResourceAsStream("technologies.json");

		String json = convertStreamToString(resourceStream);
		Type listType = new TypeToken<List<TechModelTo>>() {}.getType();
		List<TechModelTo> techList = new Gson().fromJson(json, listType);
		List<String> resultList = new ArrayList<>();


		for (TechModelTo techModelTo : techList){
			resultList.add(techModelTo.getName());
		}

		Collections.sort(resultList);

		return resultList;
	}

	@SuppressWarnings("resource")
	private static String convertStreamToString(InputStream is) {
		Scanner scanner = new Scanner(is).useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}
}
