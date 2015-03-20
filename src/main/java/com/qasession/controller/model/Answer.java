package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

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
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name="ANSWER")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="answerId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033509333352468767L;
	
	@JsonBackReference(value="question-answer")
	@JoinColumn(name="QUESTION_ID")
	@OneToOne(orphanRemoval=true, fetch = FetchType.EAGER)
	private Question question;
	
	@Id
	@Column(name = "ANSWER_ID", unique = true, nullable = false) 
	private String answerId;
	
	@Column(name = "ANSWER_CONTENT") 
    private String answerContent;
	
	@JoinColumn(name="ATTENDEE_ID")
	@Column(name = "CREATED_BY") 
	@ManyToOne(fetch=FetchType.EAGER)
    private Attendee createdBy;

	@Column(name = "UPDATE_DATE", nullable = false) 
	@Temporal(TemporalType.DATE) 
	private Calendar updateDate;
	
    public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public String getAnswerId() {
		return answerId;
	}  // String getAnswerId

	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}  // void setAnswerId
	
	public Calendar getUpdateDate() {
		return updateDate;
	}  // getUpdateDate

	public void setUpdateDate(Calendar updateDate) {
		this.updateDate = updateDate;
	}  // void setUpdateDate

	public String getAnswerContent() {
		return answerContent;
	}  // String getAnswerContent

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}  // void setAnswerContent
	
    
	public Attendee getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Attendee createdBy) {
		this.createdBy = createdBy;
	}
}  // class Answer
