package com.ciandt.techgallery.service.model.pipedrive.webhook;

public class WebhookResponse {

	private Deal current;
	private Deal previous;

	public Deal getCurrent() {
		return current;
	}

	public void setCurrent(Deal current) {
		this.current = current;
	}

	public Deal getPrevious() {
		return previous;
	}

	public void setPrevious(Deal previous) {
		this.previous = previous;
	}
}
