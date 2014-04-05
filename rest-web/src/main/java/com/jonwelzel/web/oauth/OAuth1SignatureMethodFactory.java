package com.jonwelzel.web.oauth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class OAuth1SignatureMethodFactory {

	@Inject
	private HmaSha1Method hmaSha1;

	@Inject
	private RsaSha1Method rsaSha1;

	@Inject
	private PlaintextMethod plaintext;

	public OAuth1SignatureMethod getMethod(String method) {
		switch (method) {
		case HmaSha1Method.NAME:
			return hmaSha1;
		case RsaSha1Method.NAME:
			return rsaSha1;
		case PlaintextMethod.NAME:
			return plaintext;
		default:
			break;
		}
		return null;
	}

}
