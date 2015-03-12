package com.qasession.controller.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="USERTRANSLATE")
@JsonIdentityInfo(generator=com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserTranslate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1212438446553704685L;

	@Id
	@Column(name = "USER_ID", unique = true, nullable = false) 
	private String userId;
	
	@Column(name = "FB_USER_ID", unique = true, nullable = false)  
	private String fbUserId;
	
	@Column(name = "LAST_NAME")  
	private String firstName;
	
	@Column(name = "FIRST_NAME")  
	private String lastName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFbUserId() {
		return fbUserId;
	}

	public void setFbUserId(String fbUserId) {
		this.fbUserId = fbUserId;
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
	
	
}
