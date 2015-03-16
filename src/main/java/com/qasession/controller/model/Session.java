package com.qasession.controller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="SESSION")
@JsonIdentityInfo(generator=com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Session implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5242246557731873497L;
	
	@Id
	@Column(name = "SESSION_ID", unique = true, nullable = false) 
	private String sessionId;
	
	@Column(name = "SESSION_TOPIC") 
	private String sessionTopic;
	
	@Column(name = "SESSION_DESCRIPTION") 
	private String sessionDescription;
	
	@JoinColumn(name="SESSION_ID")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
	private List <Question> questions = new ArrayList<Question>(0);
	
	@JoinColumn(name="SESSION_ID")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
	private List <Attendee> attendees = new ArrayList<Attendee>(0);;
	
	@Column(name = "SESSION_MAX_QUESTION") 
	private Integer sessionMaxQuestion;

	@Column(name = "SESSION_STATUS") 
	private String sessionStatus;
	
	@Column(name = "UPDATE_DATE", nullable = false)  
	@Temporal(TemporalType.DATE) 
	private Calendar updateDate;
	
	public String getSessionId()
	{
		return sessionId;
	}  // String getSessionId
	
	public String getSessionStatus() {
		return sessionStatus;
	}

	public void setSessionStatus(String sessionStatus) {
		this.sessionStatus = sessionStatus;
	}

	public Calendar getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}

	public void setSessionId(String pSessionId)
	{
		sessionId = pSessionId;
	}  // void getSessionId
	
	public String getSessionTopic()
	{
		return sessionTopic;
	}  // String getSessionTopic
	
	public void setSessionTopic(String pSessionTopic)
	{
		sessionTopic = pSessionTopic;
	}  // void getSessionTopic
	
	public String getSessionDescription()
	{
		return sessionDescription;
	}  // String getSessionDescription
	
	public void setSessionDescription(String pSessionDescription)
	{
		sessionDescription = pSessionDescription;
	}  // void getSessionDescription
	
	public List <Question> getQuestions()
	{
		return questions;
	}  // List <Question> getQuestions
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public List <Attendee> getAttendees()
	{
		return attendees;
	}  // List <Attendee> getAttendees
	
	public void setAttendees(List <Attendee> pASttendees)
	{
		attendees = pASttendees;
	}  // void setAttendees(List <Attendee> pASttendees)
	
	public Integer getSessionMaxQuestion() {
		return sessionMaxQuestion;
	}

	public void setSessionMaxQuestion(Integer sessionMaxQuestion) {
		this.sessionMaxQuestion = sessionMaxQuestion;
	}
}  // class Session
