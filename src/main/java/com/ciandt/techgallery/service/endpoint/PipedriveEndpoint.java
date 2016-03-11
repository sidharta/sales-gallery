package com.ciandt.techgallery.service.endpoint;

import com.ciandt.techgallery.Constants;
import com.ciandt.techgallery.service.PipedriveService;
import com.ciandt.techgallery.service.impl.PipedriveServiceImpl;
import com.ciandt.techgallery.service.model.pipedrive.DealTO;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;

/**
 * Endpoint controller class for pipedrive requests. 
 *
 * @author Edgard Cardoso
 *
 */
@Api(name = "rest", version = "v1", clientIds = {Constants.WEB_CLIENT_ID,
    Constants.API_EXPLORER_CLIENT_ID}, scopes = {Constants.EMAIL_SCOPE, Constants.PLUS_SCOPE,
    Constants.PLUS_STREAM_WRITE})
public class PipedriveEndpoint {
	
	private PipedriveService service = PipedriveServiceImpl.getInstance();
	
	 @ApiMethod(name = "getPipedriveDeal", path = "pipedrive/deals/{id}", httpMethod = "GET")
	  public DealTO getPipedriveDeal(@Named("id") String id) throws Exception {
	    return service.getPipedriveDeal(id);
	  }

}
