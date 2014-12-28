package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Date;

public class Answer implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033509333352468767L;
	
	private String answerId;
    private String content;
	
    private Attendee author;
    
    private Date lastModifiedDate;
	
    public String getAnswerId() {
		return answerId;
	}  // String getAnswerId

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}  // void setAnswerId
	
	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}  // getLastModifiedDate

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}  // void setLastModifiedDate

	public String getContent() {
		return content;
	}  // String getContent

	public void setContent(String content) {
		this.content = content;
	}  // void setContent

	public Attendee getAuthor() {
		return author;
	}  // Attendee getAuthor

	public void setAuthor(Attendee author) {
		this.author = author;
	}  // void setAuthor
}  // class Answer
