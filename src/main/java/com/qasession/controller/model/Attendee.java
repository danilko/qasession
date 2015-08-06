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
@Table(name="attendee")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="attendeeId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendee implements Serializable
{
	private static final long serialVersionUID = 223575325135709928L;

	@Id
	@Column(name = "attendee_id") 
	private String attendeeId;

	@Column(name="qasession_id")
	private String qasessionId;
	
	@Column(name = "user_id")
	private String userId;


	@Column(name = "qasession_role", nullable = false)  
	private String qasessionRole;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="UTC")
	@Column(name = "update_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar updateTimestamp;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="UTC")
	@Column(name = "create_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar createTimestamp;
	
	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}
	
	public String getQASessionId() {
		return qasessionId;
	}

	public void setQASessionId(String sessionId) {
		this.qasessionId = sessionId;
	}

	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getQASessionRole() {
		return qasessionRole;
	}  // String getSessionRole

	public void setQASessionRole(String qasessionRole) {
		this.qasessionRole = qasessionRole;
	}  // void setSessionRole
	
	public String getUserId() {
		return userId;
	}  // String getUserId

	public void setUserId(String userId) {
		this.userId = userId;
	}  // void setUserId

	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}  // Calendar getUpdateTimestamp

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}  // setUpdateTimestamp
}  // class Attendee
