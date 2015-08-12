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
 * QASession model class
 */

package com.qasession.controller.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name="Qasession")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="qasessionId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class QASession implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5242246557731873497L;
	
	@Id
	@Column(name = "qasession_id", unique = true, nullable = false) 
	private String qasessionId;
	
	@Column(name = "qasession_topic") 
	private String qasessionTopic;
	
	@Column(name = "qasession_description") 
	private String qasessionDescription;
	
	@JoinColumn(name="qasession_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
	private List <Question> questions = new ArrayList<Question>(0);
	
	@JoinColumn(name="qasession_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
	private List <Attendee> attendees = new ArrayList<Attendee>(0);;
	
	@Column(name = "qasession_max_question") 
	private Integer qasessionMaxQuestion;

	@Column(name = "qasession_status") 
	private String qasessionStatus;
	
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
	
	@ApiModelProperty(value = "QASession Id")
	public String getQASessionId()
	{
		return qasessionId;
	}  // String getSessionId
	
	public String getQASessionStatus() {
		return qasessionStatus;
	}

	@ApiModelProperty(value = "QASession Status", required = true)
	public void setQASessionStatus(String qasessionStatus) {
		this.qasessionStatus = qasessionStatus;
	}

	@ApiModelProperty(value = "Timestamp of last modification", required = false)
	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}  // Date getUpdateTimestamp

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}  // void setUpdateDate
    
	@ApiModelProperty(value = "User Id for the original author", required = false)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@ApiModelProperty(value = "User Id for the author of the last modification", required = false)
	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@ApiModelProperty(value = "Timestamp of creation", required = false)
	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	public void setQASessionId(String pQASessionId)
	{
		qasessionId = pQASessionId;
	}  // void getSessionId
	
	@ApiModelProperty(value = "QASession Topic", required = true)
	public String getQASessionTopic()
	{
		return qasessionTopic;
	}  // String getSessionTopic
	
	public void setQASessionTopic(String pQASessionTopic)
	{
		qasessionTopic = pQASessionTopic;
	}  // void getSessionTopic
	
	@ApiModelProperty(value = "QASession Description", required = true)
	public String getQASessionDescription()
	{
		return qasessionDescription;
	}  // String getSessionDescription
	
	public void setQASessionDescription(String pQASessionDescription)
	{
		qasessionDescription = pQASessionDescription;
	}  // void getSessionDescription
	
	@ApiModelProperty(value = "List of QASession questions", required = false)
	public List <Question> getQuestions()
	{
		return questions;
	}  // List <Question> getQuestions
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	@ApiModelProperty(value = "List of QASession attendees", required = false)
	public List <Attendee> getAttendees()
	{
		return attendees;
	}  // List <Attendee> getAttendees
	
	public void setAttendees(List <Attendee> pASttendees)
	{
		attendees = pASttendees;
	}  // void setAttendees(List <Attendee> pASttendees)
	
	@ApiModelProperty(value = "QASession maximum question", required = true)
	public Integer getQASessionMaxQuestion() {
		return qasessionMaxQuestion;
	}

	public void setQASessionMaxQuestion(Integer qasessionMaxQuestion) {
		this.qasessionMaxQuestion = qasessionMaxQuestion;
	}
}  // class Session
