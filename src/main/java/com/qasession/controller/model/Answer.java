/**
 * 
 * The MIT License (MIT)
 * 
 * Copyright (c) Kai-Ting (Danil) Ko
 * 
 * Permission is hereby granted, free of charge, 
 * to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including 
 * without limitation the rights to use, copy, modify, 
 * merge, publish, distribute, sublicense, and/or sell 
 * copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the 
 * following conditions:
 * 
 * The above copyright notice and this permission notice 
 * shall be included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY 
 * OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
 * TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE 
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 */

/**
 * 
 * @author Kai - Ting (Danil) Ko
 * Answer model class
 */

package com.qasession.controller.model;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name="answer")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="answerId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Answer implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2033509333352468767L;
	
	@Id
	@Column(name = "answer_id", unique = true, nullable = false) 
	private String answerId;
	
	@Column(name = "question_id") 
	private String questionId;
	
	@Column(name = "answer_content") 
    private String answerContent;
	
	@Column(name = "created_by") 
    private String createdBy;
	
	@Column(name = "updated_by") 
    private String updatedBy;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm", timezone="UTC")
	@Column(name = "update_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar updateTimestamp;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm", timezone="UTC")
	@Column(name = "create_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar createTimestamp;
	
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
	
	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}  // Date getUpdateTimestamp

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}  // void setUpdateDate

	public String getAnswerContent() {
		return answerContent;
	}  // String getAnswerContent

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}  // void setAnswerContent
	
    
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
}  // class Answer
