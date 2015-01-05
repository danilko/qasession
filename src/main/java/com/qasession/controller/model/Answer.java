package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="ANSWER")
public class Answer implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033509333352468767L;
	
	@Column(name = "QUESTION_ID", unique = true, nullable = false) 
	private String questionId;
	
	@Column(name = "ANSWER_ID", unique = true, nullable = false) 
	private String answerId;
	
	@Column(name = "ANSWER_CONTENT") 
    private String content;
	
	@Column(name = "CREATED_BY") 
    private String createdBy;
    
	@Column(name = "UPDATE_DATE", nullable = false) 
	@Temporal(TemporalType.DATE) 
	private Calendar updateDate;
	
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
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

	public String getContent() {
		return content;
	}  // String getContent

	public void setContent(String content) {
		this.content = content;
	}  // void setContent
	
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

}  // class Answer
