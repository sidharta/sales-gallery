package com.ciandt.techgallery.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.json.JSONObject;

import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.PipedriveService;
import com.ciandt.techgallery.service.TechnologyService;
import com.ciandt.techgallery.service.enums.OfferEnums;
import com.ciandt.techgallery.service.enums.TowerEnum;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.pipedrive.DealTO;
import com.ciandt.techgallery.service.model.pipedrive.webhook.Deal;
import com.ciandt.techgallery.utils.TechGalleryUtil;
import com.ciandt.techgallery.utils.pipedrive.PipedriveUtil;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;

public class PipedriveServiceImpl implements PipedriveService {

	private static PipedriveServiceImpl instance;

	private static String PIPEDRIVE_DEAL_URL_BASE = "https://api.pipedrive.com/v1/deals/";
	private static String PIPEDRIVE_PRODUCT_KEY = "e16df82dc89790231a2169c6ee3d4b79a2230036";
	private static String PIPEDRIVE_TOWER_KEY = "7b4743f02e683528a10ec09ed249726b5adcf6ad";

	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();
	
	private TechnologyService technologyService = TechnologyServiceImpl.getInstance();

	private PipedriveServiceImpl() {
	}

	/**
	 * Singleton method for the service.
	 *
	 * @return OfferServiceImpl instance.
	 */
	public static PipedriveServiceImpl getInstance() {
		if (instance == null) {
			instance = new PipedriveServiceImpl();
		}
		return instance;
	}

	private String loadApikey() throws InternalServerErrorException {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("pipedrive.properties");
			Properties props = new Properties();
			props.load(is);
			return props.getProperty("api_key");
		} catch (IOException e) {
			throw new InternalServerErrorException(ValidationMessageEnums.PIPEDRIVE_API_KEY_NOT_FOUND.message());
		}
	}

	@Override
	public DealTO getPipedriveDeal(String id, User user) throws Exception {
		validateUser(user);

		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
		GenericUrl url = new GenericUrl(PIPEDRIVE_DEAL_URL_BASE + id + "?api_token=" + loadApikey());
		HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		String json = request.execute().parseAsString();
		return parseJsonToDeal(json);
	}

	private DealTO parseJsonToDeal(String json) {
		JSONObject dealObject = new JSONObject(json);

		JSONObject data = dealObject.getJSONObject("data");

		DealTO deal = new DealTO();
		deal.setName(data.getString("title"));
		deal.setStatus(data.getString("status"));

		JSONObject user_id = data.getJSONObject("user_id");
		deal.setOwnerEmail(user_id.getString("email"));
		deal.setOwnerName(user_id.getString("name"));

		JSONObject org_id = data.getJSONObject("org_id");
		deal.setClient(org_id.getString("name"));

		String offerIds = data.getString(PIPEDRIVE_PRODUCT_KEY);
		List<String> offerItems = Arrays.asList(offerIds.split(","));
		deal.setOffers(getOfferNames(offerItems));
		
		String towerId = data.getString(PIPEDRIVE_TOWER_KEY);
		deal.setTower(getTowerName(towerId));

		return deal;
	}

	@Override
	public void saveFromWebhook(Deal deal) throws BadRequestException, IOException, GeneralSecurityException {
		Technology technology = new Technology();
		technology.setId(TechGalleryUtil.slugify(deal.getTitle()));
		technology.setName(deal.getTitle());
		technology.setClient(deal.getOrgName());
		technology.setStatus(deal.getStatus());
		technology.setOffers(PipedriveUtil.getProducts(deal.getProducts()));
		technology.setShortDescription(deal.getTitle());
		technology.setDescription(deal.getTitle());
		technology.setTower(deal.getTower().getName());
		
		technologyService.addOrUpdateTechnology(technology, null);
	}
	
	@Override
	public List<String> getOffers(User user)
			throws NotFoundException, BadRequestException, InternalServerErrorException {
		validateUser(user);
		final List<OfferEnums> enumValues = Arrays.asList(OfferEnums.values());
		final List<String> offers = new ArrayList<>();
		for (final OfferEnums enumEntry : enumValues) {
			offers.add(enumEntry.getName());
		}
		return offers;
	}

	
	private String getTowerName(String id){
		final List<TowerEnum> enumValues = Arrays.asList(TowerEnum.values());
		for(final TowerEnum enumEntry :enumValues){
			if (enumEntry.getId() == Integer.valueOf(id)) {
				return enumEntry.getName();
			}
		}
		return "";
	}
	
	private List<String> getOfferNames(List<String> ids) {
		List<String> result = new LinkedList<>();
		final List<OfferEnums> enumValues = Arrays.asList(OfferEnums.values());
		for (final OfferEnums enumEntry : enumValues) {
			for (final String id : ids) {
				if (enumEntry.getId() == Integer.valueOf(id)) {
					result.add(enumEntry.getName());
				}
			}
		}
		return result;
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

		final TechGalleryUser techUser = UserServiceTGImpl.getInstance().getUserByGoogleId(user.getUserId());
		if (techUser == null) {
			throw new NotFoundException(ValidationMessageEnums.USER_NOT_EXIST.message());
		}
	}

	@Override
	public List<String> getTowers(User user)
			throws NotFoundException, BadRequestException, InternalServerErrorException {
		validateUser(user);
		final List<TowerEnum> enumValues = Arrays.asList(TowerEnum.values());
		final List<String> towers = new ArrayList<>();
		for (final TowerEnum enumEntry : enumValues) {
			towers.add(enumEntry.getName());
		}
		return towers;
	}

}
