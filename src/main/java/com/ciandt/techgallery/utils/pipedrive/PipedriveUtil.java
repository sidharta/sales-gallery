package com.ciandt.techgallery.utils.pipedrive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.ciandt.techgallery.service.model.pipedrive.webhook.Deal;
import com.ciandt.techgallery.service.model.pipedrive.webhook.Tower;
import com.ciandt.techgallery.service.model.pipedrive.webhook.WebhookResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class PipedriveUtil {

	public static final int STAGE_PROPOSAL = 6;
	public static final int STAGE_OPPORTUNITY = 5;
	public static final String PROPERTIES_FILE = "pipedrive.properties";
	
	@SuppressWarnings("serial")
	private static final Map<String, String> PRODUCT_MAP = new HashMap<String, String>(){{
		put("6", "Google Cloud");
		put("7", "Legacy Optimization");
		put("8", "Google Apps");
	}};

	public static Properties getPropertiesFile() throws IOException {
		Properties properties = new Properties();
		properties.load(PipedriveUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));

		return properties;
	}

	public static boolean checkWebhookCredentials(String[] values) throws IOException {

		Properties properties = getPropertiesFile();

		String username = properties.getProperty("webhook_username");
		String password = properties.getProperty("webhook_password");

		return Arrays.equals(values, new String[] { username, password });
	}

	public static WebhookResponse getJsonFromWebhook(String jsonString) {
		Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		return gson.fromJson(jsonString, WebhookResponse.class);
	}

	public static boolean shouldProcess(WebhookResponse webhookResponse) {
		Deal current = webhookResponse.getCurrent();
		Deal previous = webhookResponse.getPrevious();

		return isFromTower(current) && (isFromStage(current, previous) || hasPropertiesChanges(current, previous));
	}

	public static boolean hasPropertiesChanges(Deal current, Deal previous) {
		return !current.getOrgName().equals(previous.getOrgName()) || 
				!current.getTitle().equals(previous.getTitle()) || 
				!current.getStatus().equals(previous.getStatus()) ||
				!current.getTower().equals(previous.getTower()) ||
				!current.getProducts().equals(previous.getProducts());
	}

	public static boolean isFromStage(Deal current, Deal previous) {
		if (isFromSameStage(current, previous)) {
			return false;
		}

		return current.getStageOrderNr() == STAGE_OPPORTUNITY || current.getStageOrderNr() == STAGE_PROPOSAL;
	}

	public static boolean isFromSameStage(Deal current, Deal previous) {
		return current.getStageOrderNr().equals(previous.getStageOrderNr());
	}

	public static boolean isFromTower(Deal current) {
		return current.getTower().equals(Tower.ResourcesAndLogistics);
	}

	public static List<String> getProducts(String products) {
		List<String> productsList = new ArrayList<>();
		for (String productId : products.split(",")) {
			productsList.add(PRODUCT_MAP.get(productId));
		}
		
		return productsList;
	}

}
