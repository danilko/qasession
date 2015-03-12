package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="ATTENDEE")
@JsonIdentityInfo(generator=com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendee implements Serializable
{
	private static final long serialVersionUID = 223575325135709928L;

	@Id
	@Column(name = "ATTENDEE_ID") 
	private String attendeeId;

	@JoinColumn(name="SESSION_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private Session session;
	
	@JoinColumn(name = "USER_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private UserTranslate userTranslate;
	
	@Column(name = "SESSION_ROLE", nullable = false)  
	private String sessionRole;
	
	@Column(name = "UPDATE_DATE", nullable = false) 
	@Temporal(TemporalType.DATE) 
	private Calendar updateDate;
	
	
	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}
	
	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}

	public UserTranslate getUserTranslate() {
		return userTranslate;
	}

	public void setUserTranslate(UserTranslate userTranslate) {
		this.userTranslate = userTranslate;
	}

	public String getSessionRole() {
		return sessionRole;
	}  // String getSessionRole

	public void setSessionRole(String sessionRole) {
		this.sessionRole = sessionRole;
	}  // void setSessionRole
}  // class Attendee
