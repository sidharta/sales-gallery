package com.ciandt.techgallery.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ciandt.techgallery.persistence.model.TechGalleryUser;
import com.ciandt.techgallery.persistence.model.Technology;
import com.ciandt.techgallery.service.PipedriveService;
import com.ciandt.techgallery.service.TechnologyService;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.pipedrive.DealFieldTO;
import com.ciandt.techgallery.service.model.pipedrive.DealTO;
import com.ciandt.techgallery.service.model.pipedrive.webhook.Deal;
import com.ciandt.techgallery.service.model.pipedrive.webhook.WebhookResponse;
import com.ciandt.techgallery.utils.TechGalleryUtil;
import com.ciandt.techgallery.utils.pipedrive.PipedriveUtil;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
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
	private static String PIPEDRIVE_DEAL_FIELD_URL_BASE = "https://api.pipedrive.com/v1/dealFields/";
	private static String PIPEDRIVE_DEAL_URL_PREFIX = "https://citsoftware.pipedrive.com/deal/view/";
	private static String PIPEDRIVE_PRODUCT_KEY = "e16df82dc89790231a2169c6ee3d4b79a2230036";
	private static String PIPEDRIVE_TOWER_KEY = "7b4743f02e683528a10ec09ed249726b5adcf6ad";

	private static String PIPEDRIVE_DEAL_FIELD_TOWER_ID = "12459";
	private static String PIPEDRIVE_DEAL_FIELD_PRODUCT_ID = "12461";

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
	public DealTO getPipedriveDeal(String id, User user)
			throws IOException, NotFoundException, InternalServerErrorException, BadRequestException {
		validateUser(user);

		return getPipedriveDeal(id);
	}

	private DealTO getPipedriveDeal(String id)
			throws InternalServerErrorException, IOException, NotFoundException, BadRequestException {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
		GenericUrl url = new GenericUrl(PIPEDRIVE_DEAL_URL_BASE + id + "?api_token=" + loadApikey());
		HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		try {
			HttpResponse response = request.execute();
			String json = response.parseAsString();
			return parseJsonToDeal(json);
		} catch (HttpResponseException e) {
			throw new NotFoundException(ValidationMessageEnums.PIPEDRIVE_DEAL_NOT_FOUND.message());
		}
	}

	private DealTO parseJsonToDeal(String json)
			throws NotFoundException, BadRequestException, InternalServerErrorException, IOException {
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
		
		try {
			String offerIds = data.getString(PIPEDRIVE_PRODUCT_KEY);
			if (StringUtils.isNotBlank(offerIds)) {
				deal.setOffers(getOfferNames(Arrays.asList(offerIds.split(","))));
			}
		} catch (JSONException e) {
			// Product is null
		}

		try {
			String towerId = data.getString(PIPEDRIVE_TOWER_KEY);
			if (StringUtils.isNotBlank(towerId)) {
				deal.setTower(getTowerName(towerId));
			}
		} catch (JSONException e) {
			// Tower is null
		}

		return deal;
	}

	private List<String> dealFieldToList(HashMap<String, DealFieldTO> map)
			throws NotFoundException, BadRequestException, InternalServerErrorException, IOException {

		List<DealFieldTO> list = new ArrayList<DealFieldTO>(map.values());
		List<String> result = new ArrayList<>();
		for (DealFieldTO dealFieldTO : list) {
			result.add(dealFieldTO.getLabel());
		}
		return result;
	}

	@Override
	public void saveFromWebhook(WebhookResponse webhookResponse)
			throws BadRequestException, IOException, GeneralSecurityException {
		try {
			DealTO dealTO = getPipedriveDeal(webhookResponse.getCurrent().getId().toString());
			Technology technology = technologyService.getTechnologyByPipedriveId(webhookResponse.getCurrent().getId());

			if (technology == null) {
				technology = new Technology();
				technology.setDescription(dealTO.getName());
				technology.setShortDescription(dealTO.getName());
				technology.setId(TechGalleryUtil.slugify(dealTO.getName()));
				technology.setIdPipedrive(webhookResponse.getCurrent().getId());
			}

			technology.setName(dealTO.getName());
			technology.setClient(dealTO.getClient());
			technology.setStatus(dealTO.getStatus());
			technology.setOffers(dealTO.getOffers());
			technology.setTower(dealTO.getTower());
			technology.setOwnerEmail(dealTO.getOwnerEmail());
			technology.setOwnerName(dealTO.getOwnerName());
			technology.setPipedriveLink(PIPEDRIVE_DEAL_URL_PREFIX + webhookResponse.getCurrent().getId());

			technologyService.addOrUpdateTechnology(technology, null);
		} catch (InternalServerErrorException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}

	private HashMap<String, DealFieldTO> getOffers()
			throws NotFoundException, BadRequestException, InternalServerErrorException, IOException {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
		GenericUrl url = new GenericUrl(
				PIPEDRIVE_DEAL_FIELD_URL_BASE + PIPEDRIVE_DEAL_FIELD_PRODUCT_ID + "?api_token=" + loadApikey());
		HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		try {
			HttpResponse response = request.execute();
			String json = response.parseAsString();
			return parseJsonToDealField(json);
		} catch (HttpResponseException e) {
			throw new NotFoundException(ValidationMessageEnums.PIPEDRIVE_DEAL_NOT_FOUND.message());
		}
	}

	@Override
	public List<String> getOffers(User user)
			throws NotFoundException, BadRequestException, InternalServerErrorException, IOException {
		validateUser(user);
		return this.dealFieldToList(getOffers());
	}

	private HashMap<String, DealFieldTO> parseJsonToDealField(String json) {

		HashMap<String, DealFieldTO> items = new HashMap<>();
		JSONObject dealObject = new JSONObject(json);

		JSONObject data = dealObject.getJSONObject("data");

		JSONArray options = data.getJSONArray("options");

		for (int i = 0; i < options.length(); i++) {
			JSONObject obj = options.getJSONObject(i);
			String label = obj.getString("label");
			String id = Integer.toString(obj.getInt("id"));
			if (StringUtils.isNotBlank(label) && StringUtils.isNotBlank(id)) {
				DealFieldTO dealFieldTO = new DealFieldTO();
				dealFieldTO.setId(id);
				dealFieldTO.setLabel(label);
				items.put(id, dealFieldTO);
			}
		}
		return items;
	}

	private String getTowerName(String id) throws InternalServerErrorException, NotFoundException, IOException {
		HashMap<String, DealFieldTO> items = this.getTowers();
		DealFieldTO dealFieldTO = items.get(id);
		if (dealFieldTO != null) {
			return dealFieldTO.getLabel();
		}
		return "";
	}

	private List<String> getOfferNames(List<String> ids)
			throws NotFoundException, BadRequestException, InternalServerErrorException, IOException {
		HashMap<String, DealFieldTO> items = this.getOffers();

		List<String> result = new LinkedList<>();
		for (final String id : ids) {
			DealFieldTO dealFieldTO = items.get(id);
			if (dealFieldTO != null) {
				result.add(dealFieldTO.getLabel());
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
			throws BadRequestException, NotFoundException, InternalServerErrorException, IOException {
		validateUser(user);
		return this.dealFieldToList(this.getTowers());
	}

	private HashMap<String, DealFieldTO> getTowers()
			throws InternalServerErrorException, IOException, NotFoundException {
		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
		GenericUrl url = new GenericUrl(
				PIPEDRIVE_DEAL_FIELD_URL_BASE + PIPEDRIVE_DEAL_FIELD_TOWER_ID + "?api_token=" + loadApikey());
		HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		try {
			HttpResponse response = request.execute();
			String json = response.parseAsString();
			return parseJsonToDealField(json);
		} catch (HttpResponseException e) {
			throw new NotFoundException(ValidationMessageEnums.PIPEDRIVE_DEAL_NOT_FOUND.message());
		}
	}

}
