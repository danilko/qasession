package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Date;

public class Question implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2217994381777064845L;

	private String sessionId;
	
	private String questionId;
	private String content;
	private Answer answer;
	
	private String questionStatus;
	
    private Attendee author;
    
    private Date lastModifiedDate;
	
	public String getSessionId() {
		return sessionId;
	}  // String getSessionId

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}  // void setSessionId

	public String getQuestionId() {
		return questionId;
	}  // String getQuestionId

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}  // void setQuestionId

	public String getContent() {
		return content;
	}  // String getContent

	public void setContent(String content) {
		this.content = content;
	}  // void setContent

	public Answer getAnswer() {
		return answer;
	}  // Answer getAnswer

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}  // void setAnswer

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}  // Date getLastModifiedDate

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}  // void setLastModifiedDate

	public Attendee getAuthor() {
		return author;
	}  // Attendee getAuthor

	public void setAuthor(Attendee author) {
		this.author = author;
	}  // void setAuthor

	public String getQuestionStatus() {
		return questionStatus;
	}  // String getQuestionStatus

	public void setQuestionStatus(String questionStatus) {
		this.questionStatus = questionStatus;
	}  // void setQuestionStatus
}  // class Question
