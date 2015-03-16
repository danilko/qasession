package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name="QUESTION")
@JsonIdentityInfo(generator=com.fasterxml.jackson.annotation.ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2217994381777064845L;

	@JsonBackReference(value="session-question")
	@JoinColumn(name="SESSION_ID")
	@ManyToOne(fetch = FetchType.EAGER)
	private Session session;

	@Id
	@Column(name = "QUESTION_ID", unique = true, nullable = false)  
	private String questionId;
	
	@Column(name = "QUESTION_CONTENT") 
	private String questionContent;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="QUESTION_ID")
	private Answer answer;
	
	@Column(name = "QUESTION_STATUS") 
	private String questionStatus;
	
	@JoinColumn(name="ATTENDEE_ID")
	@Column(name = "CREATED_BY") 
	@ManyToOne(fetch=FetchType.EAGER)
    private Attendee createdBy;
    
	@Column(name = "UPDATE_DATE", nullable = false)  
	@Temporal(TemporalType.DATE) 
    private Calendar updateDate;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

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

	public Attendee getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Attendee createdBy) {
		this.createdBy = createdBy;
	}

}  // class Question
