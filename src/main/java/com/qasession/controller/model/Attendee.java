package com.qasession.controller.model;

import java.io.Serializable;

public class Attendee implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 223575325135709928L;

	private Session sessionId;
	
	private String email;

	private String role;
	
	
	public String getEmail() {
		return email;
	}  // String getEmail

	public void setEmail(String email) {
		this.email = email;
	}  // void setEmail

	public Session getSessionId() 
	{
		return sessionId;
	}  // getSessionId

	public void setSessionId(Session sessionId) {
		this.sessionId = sessionId;
	}  // setSessionId

	public String getRole() {
		return role;
	}  // String getRole

	public void setRole(String role) {
		this.role = role;
	}  // void setRole
}  // class Attendee
