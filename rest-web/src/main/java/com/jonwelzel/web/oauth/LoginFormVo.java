package com.jonwelzel.web.oauth;

public class LoginFormVo {

	private String consumerApplicationName;

	private String consumerDomain;

	private String oauthToken;

	public String getConsumerApplicationName() {
		return consumerApplicationName;
	}

	public void setConsumerApplicationName(String consumerApplicationName) {
		this.consumerApplicationName = consumerApplicationName;
	}

	public String getConsumerDomain() {
		return consumerDomain;
	}

	public void setConsumerDomain(String consumerDomain) {
		this.consumerDomain = consumerDomain;
	}

	public String getOauthToken() {
		return oauthToken;
	}

	public void setOauthToken(String oauthToken) {
		this.oauthToken = oauthToken;
	}

}
