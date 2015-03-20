package com.qasession.controller.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfo {
	@JsonProperty("uid")
	private String userId;

	@JsonProperty("access_token")
	private String accessToken;

	@JsonProperty("expires_in")
	private String expiresIn;

	@JsonProperty("email")
	private String email;

	@JsonProperty("facebook_profile_link")
	private String facebookProfileLink;

	@JsonProperty("facebook_profile_id")
	private String facebookProfileId;

	@JsonProperty("firstName")
	private String firstName;

	@JsonProperty("lastName")
	private String lastName;

	@JsonProperty("userRole")
	private String userRole;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getFacebookProfileLink() {
		return facebookProfileLink;
	}

	public void setFacebookProfileLink(String facebookProfileLink) {
		this.facebookProfileLink = facebookProfileLink;
	}

	public String getFacebookProfileId() {
		return facebookProfileId;
	}

	public void setFacebookProfileId(String facebookProfileId) {
		this.facebookProfileId = facebookProfileId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

}
