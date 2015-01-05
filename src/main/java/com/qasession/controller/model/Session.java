package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="SESSION")
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
	
	@OneToMany(mappedBy="SESSION", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List <Question> questions;
	
	@OneToMany(mappedBy="SESSION", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List <Attendee> attendees;
	
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
		sessionTopic = pSessionDescription;
	}  // void getSessionDescription
	
	@JsonManagedReference
	public List <Question> getQuestions()
	{
		return questions;
	}  // List <Question> getQuestions
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	@JsonManagedReference
	public void setSessionHost(List <Question> pQuestions)
	{
		questions = pQuestions;
	}  // void setSessionHost(List <Question> pQuestions)
	
	@JsonManagedReference
	public List <Attendee> getAttendees()
	{
		return attendees;
	}  // List <Attendee> getAttendees
	
	@JsonManagedReference
	public void setAttendees(List <Attendee> pASttendees)
	{
		attendees = pASttendees;
	}  // void setSessionHost(List <Question> pQuestions)
	
	public Integer getSessionMaxQuestion() {
		return sessionMaxQuestion;
	}

	public void setSessionMaxQuestion(Integer sessionMaxQuestion) {
		this.sessionMaxQuestion = sessionMaxQuestion;
	}
}  // class Session
