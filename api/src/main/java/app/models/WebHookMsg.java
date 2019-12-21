package app.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WebHookMsg {
	private final String url;

	@JsonCreator
	public WebHookMsg(@JsonProperty("url") String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
