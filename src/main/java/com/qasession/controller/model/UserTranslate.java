package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="usertranslate")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="userId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTranslate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1212438446553704685L;

	@Id
	@Column(name = "user_id", unique = true, nullable = false) 
	private String userId;
	
	@Column(name = "facebook_user_id", unique = true, nullable = true)  
	private String facebookUserId;
	
	@Column(name = "twitter_user_id", unique = true, nullable = true)  
	private String twitterUserId;
	
	@Column(name = "google_user_id", unique = true, nullable = true)  
	private String googleUserId;
	
	@Column(name = "login_user_id_type", nullable = false)    
	private String loginUserIdType;
	
	@Column(name = "name")  
	private String name;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="UTC")
	@Column(name = "update_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar updateTimestamp;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="UTC")
	@Column(name = "create_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar createTimestamp;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFacebookUserId() {
		return facebookUserId;
	}

	public void setFacebookUserId(String facebookUserId) {
		this.facebookUserId = facebookUserId;
	}

	public String getGoogleUserId() {
		return googleUserId;
	}

	public void setGoogleUserId(String googleUserId) {
		this.googleUserId = googleUserId;
	}
	
	public String getTwitterUserId() {
		return twitterUserId;
	}

	public void setTwitterUserId(String twitterUserId) {
		this.twitterUserId = twitterUserId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getLoginUserIdType() {
		return loginUserIdType;
	}

	public void setLoginUserIdType(String loginUserIdType) {
		this.loginUserIdType = loginUserIdType;
	}
}
