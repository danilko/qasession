package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="question")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="questionId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Question implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2217994381777064845L;

	@Column(name="qasession_id")
	private String qasessionId;

	@Id
	@Column(name = "question_id", unique = true, nullable = false)  
	private String questionId;
	
	@Column(name = "question_content") 
	private String questionContent;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
	@JoinColumn(name="question_id")
	private Answer answer;
	
	@Column(name = "question_status") 
	private String questionStatus;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="UTC")
	@Column(name = "create_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar createTimestamp;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:00", timezone="UTC")
	@Column(name = "update_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar updateTimestamp;

	@Column(name = "created_by") 
    private String createdBy;
	
	@Column(name = "updated_by") 
    private String updatedBy;
	
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

    
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getQuestionStatus() {
		return questionStatus;
	}  // String getQuestionStatus

	public void setQuestionStatus(String questionStatus) {
		this.questionStatus = questionStatus;
	}  // void setQuestionStatus
	
	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getQASessionId() {
		return qasessionId;
	}
	
	public void setQASessionId(String sessionId) {
		this.qasessionId = sessionId;
	}
}  // class Question
