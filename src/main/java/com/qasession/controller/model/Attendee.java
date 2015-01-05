package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="ATTENDEE")
public class Attendee implements Serializable
{
	private static final long serialVersionUID = 223575325135709928L;

	@Column(name = "SESSION_ID") 
	private String sessionId;
	
	@Column(name = "ATTENDEE_EMAIL") 
	private String email;
	
	@Column(name = "SESSION_ROLE") 
	private String sessionRole;
	
	@Column(name = "UPDATE_DATE", nullable = false) 
	@Temporal(TemporalType.DATE) 
	private Calendar updateDate;
	
	public String getEmail() {
		return email;
	}  // String getEmail

	public void setEmail(String email) {
		this.email = email;
	}  // void setEmail

	public String getSessionId() 
	{
		return sessionId;
	}  // getSessionId

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}  // setSessionId

	public String getSessionRole() {
		return sessionRole;
	}  // String getSessionRole

	public void setSessionRole(String sessionRole) {
		this.sessionRole = sessionRole;
	}  // void setSessionRole
}  // class Attendee
