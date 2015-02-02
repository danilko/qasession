package com.qasession.controller.security;

public class FacebookClient {
	private static final String OAUTH_API_KEY = System.getenv("OAUTH_API_KEY");
	private static final String OAUTH_API_CLIENT_SECRET = System
			.getenv("OAUTH_API_CLIENT_SECRET");
	private static final String OAUTH_API_CLIENT_ID = System
			.getenv("OAUTH_API_CLIENT_ID");
	private static final String OAUTH_API_SCOPES = System
			.getenv("OAUTH_API_SCOPES");

	private static final String OAUTH_AUTH_URI = System
			.getenv("OAUTH_AUTH_URI");
	private static final String OAUTH_REDUCT_URI = System
			.getenv("OAUTH_REDUCT_URI");

	public static String getOauthAPIKey() {
		return OAUTH_API_KEY;
	}

	public static String getOauthAPISecret() {
		return OAUTH_API_CLIENT_SECRET;
	}

	public static String getLogInRedirectionURI() {
		return OAUTH_AUTH_URI + "/authorize?clientId=" + OAUTH_API_CLIENT_ID
				+ "&display=page&redirect_uri=" + OAUTH_REDUCT_URI + "&scope="
				+ OAUTH_API_SCOPES;
	}

	public static String getAuthURI(String pAuthCode) {
		return OAUTH_AUTH_URI + "/access_token?clientId=" + OAUTH_API_CLIENT_ID
				+ "&client_secret=" + OAUTH_API_CLIENT_SECRET
				+ "&redirect_uri=" + OAUTH_REDUCT_URI + "&code=" + pAuthCode;
	}
}
