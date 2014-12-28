package com.qasession.controller.model;

import java.io.Serializable;
import java.util.List;

public class Session implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5242246557731873497L;
	
	private String sessionId;
	
	private String sessionTopic;
	
	private String sessionDescription;
	
	private List <Question> questions;
	private List <Attendee> attendees;
	
	public String getSessionId()
	{
		return sessionId;
	}  // String getSessionId
	
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
	
	public List <Question> getQuestions()
	{
		return questions;
	}  // List <Question> getQuestions
	
	public void setSessionHost(List <Question> pQuestions)
	{
		questions = pQuestions;
	}  // void setSessionHost(List <Question> pQuestions)
	
	public List <Attendee> getAttendees()
	{
		return attendees;
	}  // List <Attendee> getAttendees
	
	public void setAttendees(List <Attendee> pASttendees)
	{
		attendees = pASttendees;
	}  // void setSessionHost(List <Question> pQuestions)
}  // class Session
