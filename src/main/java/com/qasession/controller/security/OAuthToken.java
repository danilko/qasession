package com.qasession.controller.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OAuthToken 
{
	@JsonProperty("access_token")
	private String accessToken;
	
    @JsonProperty("expires_in")
    private String expiresIn;
    
    @JsonProperty("machine_id")
    private String machineId;
    
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

	public String getMachineId() {
		return machineId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

}
