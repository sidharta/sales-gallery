package com.ciandt.techgallery.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ciandt.techgallery.service.impl.PipedriveServiceImpl;
import com.ciandt.techgallery.service.model.pipedrive.webhook.WebhookResponse;
import com.ciandt.techgallery.utils.pipedrive.PipedriveUtil;
import com.google.api.client.util.Base64;
import com.google.api.server.spi.response.BadRequestException;

@SuppressWarnings("serial")
public class PipedriveWebhookServlet extends HttpServlet {

	private static final String UTF_8 = "UTF-8";
	private static final String BASIC = "Basic";
	private static final String AUTHORIZATION_HEADER = "Authorization";

	private static final Logger logger = Logger.getLogger(PipedriveWebhookServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		if (isValid(req)) {
			String jsonString = convertStreamToString(req.getInputStream());
			logger.info(jsonString);

			WebhookResponse webhookResponse = PipedriveUtil.getJsonFromWebhook(jsonString);

			if (PipedriveUtil.shouldProcess(webhookResponse)) {
				try {
					PipedriveServiceImpl.getInstance().saveFromWebhook(webhookResponse);
				} catch (BadRequestException e) {
					logger.severe("Bad request");
					logger.severe(e.getMessage());
					resp.sendError(500);
				} catch (GeneralSecurityException e) {
					logger.severe("General Security");
					logger.severe(e.getMessage());
					resp.sendError(500);
				}
			}
			resp.getWriter().append(jsonString);
		} else {
			resp.sendError(403);
		}
	}

	private boolean isValid(HttpServletRequest req) throws IOException {
		final String authorization = req.getHeader(AUTHORIZATION_HEADER);
		if (authorization != null && authorization.startsWith(BASIC)) {
			String base64Credentials = authorization.substring(BASIC.length()).trim();
			String credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName(UTF_8));
			final String[] values = credentials.split(":", 2);

			return PipedriveUtil.checkWebhookCredentials(values);
		}

		return false;
	}

	@SuppressWarnings("resource")
	static String convertStreamToString(InputStream is) {
		Scanner s = new Scanner(is, UTF_8).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}
