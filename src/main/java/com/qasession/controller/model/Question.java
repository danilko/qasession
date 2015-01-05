package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="QUESTION")
public class Question implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2217994381777064845L;

	@Column(name = "SESSION_ID") 
	private String sessionId;
	
	@Id
	@Column(name = "QUESTION_ID", unique = true, nullable = false)  
	private String questionId;
	
	@Column(name = "QUESTION_CONTENT") 
	private String questionContent;
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="QUESTION_ID")
	private Answer answer;
	
	@Column(name = "QUESTION_STATUS") 
	private String questionStatus;
	
	@Column(name = "CREATED_BY") 
    private String createdBy;
    
	@Column(name = "UPDATE_DATE", nullable = false)  
	@Temporal(TemporalType.DATE) 
    private Calendar updateDate;
	
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

	public String getQuestionContent() {
		return questionContent;
	}  // String getQuestionContent

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}  // void setQuestionContent

	public Answer getAnswer() {
		return answer;
	}  // Answer getAnswer

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}  // void setAnswer

	public Calendar getUpdateDate() {
		return updateDate;
	}  // Date getLastModifiedDate

	public void setUpdateDate(Calendar calendar) {
		this.updateDate = calendar;
	}  // void setUpdateDate

	public String getQuestionStatus() {
		return questionStatus;
	}  // String getQuestionStatus

	public void setQuestionStatus(String questionStatus) {
		this.questionStatus = questionStatus;
	}  // void setQuestionStatus

	public String getCreatedBy() {
		return createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
}  // class Question
