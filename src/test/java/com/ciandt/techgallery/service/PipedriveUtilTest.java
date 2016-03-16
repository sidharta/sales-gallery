package com.ciandt.techgallery.service;

import org.junit.Assert;
import org.junit.Test;

import com.ciandt.techgallery.service.model.pipedrive.webhook.Deal;
import com.ciandt.techgallery.utils.pipedrive.PipedriveUtil;

public class PipedriveUtilTest {

	@Test
	public void processFromStage() {
		Deal current = new Deal();
		Deal previous = new Deal();
		
		current.setStageOrderNr(5L);
		previous.setStageOrderNr(4L);
		
		Assert.assertTrue(PipedriveUtil.isFromStage(current, previous));
		
		previous.setStageOrderNr(5L);
		Assert.assertFalse(PipedriveUtil.isFromStage(current, previous));
		
		current.setStageOrderNr(2L);
		Assert.assertFalse(PipedriveUtil.isFromStage(current, previous));
	}	
}
