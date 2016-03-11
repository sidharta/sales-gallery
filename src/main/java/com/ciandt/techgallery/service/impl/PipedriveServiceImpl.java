package com.ciandt.techgallery.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.ciandt.techgallery.service.PipedriveService;
import com.ciandt.techgallery.service.enums.ValidationMessageEnums;
import com.ciandt.techgallery.service.model.pipedrive.DealTO;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.server.spi.response.InternalServerErrorException;
import org.json.JSONObject;

public class PipedriveServiceImpl implements PipedriveService {

	private static PipedriveServiceImpl instance;

	private static String PIPEDRIVE_DEAL_URL_BASE = "https://api.pipedrive.com/v1/deals/";

	static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	static final JsonFactory JSON_FACTORY = new JacksonFactory();

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
	public DealTO getPipedriveDeal(String id) throws Exception {

		HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
			@Override
			public void initialize(HttpRequest request) {
				request.setParser(new JsonObjectParser(JSON_FACTORY));
			}
		});
		GenericUrl url = new GenericUrl(PIPEDRIVE_DEAL_URL_BASE + id + "?api_token=" + loadApikey());
		System.out.println(url.build());

		HttpRequest request = requestFactory.buildGetRequest(url);
		request.getHeaders().setContentType("application/json");

		String json = request.execute().parseAsString();
		System.out.print(json);

		return parseJsonToDeal(json);
	}

	private DealTO parseJsonToDeal(String json) {
		JSONObject dealObject = new JSONObject(json);
		
		JSONObject data = dealObject.getJSONObject("data");

		DealTO deal = new DealTO();
		deal.setName(data.getString("title"));
		deal.setStatus(data.getString("status"));
		
		JSONObject user_id = data.getJSONObject("user_id");
		deal.setOwner(user_id.getString("email"));
		
		JSONObject org_id = data.getJSONObject("org_id");
		deal.setClient(org_id.getString("name"));
		
		

		return deal;

	}

}
