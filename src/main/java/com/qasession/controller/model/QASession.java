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

@Entity
@Table(name="qasession")
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
	
	public String getQASessionId()
	{
		return qasessionId;
	}  // String getSessionId
	
	public String getQASessionStatus() {
		return qasessionStatus;
	}

	public void setQASessionStatus(String qasessionStatus) {
		this.qasessionStatus = qasessionStatus;
	}

	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}  // Date getUpdateTimestamp

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}  // void setUpdateDate
    
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
	public void setQASessionId(String pQASessionId)
	{
		qasessionId = pQASessionId;
	}  // void getSessionId
	
	public String getQASessionTopic()
	{
		return qasessionTopic;
	}  // String getSessionTopic
	
	public void setQASessionTopic(String pQASessionTopic)
	{
		qasessionTopic = pQASessionTopic;
	}  // void getSessionTopic
	
	public String getQASessionDescription()
	{
		return qasessionDescription;
	}  // String getSessionDescription
	
	public void setQASessionDescription(String pQASessionDescription)
	{
		qasessionDescription = pQASessionDescription;
	}  // void getSessionDescription
	
	public List <Question> getQuestions()
	{
		return questions;
	}  // List <Question> getQuestions
	
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	
	public List <Attendee> getAttendees()
	{
		return attendees;
	}  // List <Attendee> getAttendees
	
	public void setAttendees(List <Attendee> pASttendees)
	{
		attendees = pASttendees;
	}  // void setAttendees(List <Attendee> pASttendees)
	
	public Integer getQASessionMaxQuestion() {
		return qasessionMaxQuestion;
	}

	public void setQASessionMaxQuestion(Integer qasessionMaxQuestion) {
		this.qasessionMaxQuestion = qasessionMaxQuestion;
	}
}  // class Session
