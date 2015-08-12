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
 * Attendee model class
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
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel
@Entity
@Table(name="Attendee")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="attendeeId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attendee implements Serializable
{
	private static final long serialVersionUID = 223575325135709928L;

	@Id
	@Column(name = "attendee_id") 
	private String attendeeId;

	@Column(name="qasession_id")
	private String qasessionId;
	
	@Column(name = "user_id")
	private String userId;


	@Column(name = "qasession_role", nullable = false)  
	private String qasessionRole;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm", timezone="UTC")
	@Column(name = "update_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar updateTimestamp;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd,HH:mm", timezone="UTC")
	@Column(name = "create_timestamp", nullable = false)  
	@Temporal(TemporalType.TIMESTAMP) 
    private Calendar createTimestamp;
	
	@ApiModelProperty(value = "Attendee Id", required = false)
	public String getAttendeeId() {
		return attendeeId;
	}

	public void setAttendeeId(String attendeeId) {
		this.attendeeId = attendeeId;
	}
	
	@ApiModelProperty(value = "QASession Id", required = false)
	public String getQASessionId() {
		return qasessionId;
	}

	public void setQASessionId(String sessionId) {
		this.qasessionId = sessionId;
	}

	@ApiModelProperty(value = "Timestamp of creation", required = false)
	public Calendar getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(Calendar createTimestamp) {
		this.createTimestamp = createTimestamp;
	}
	@ApiModelProperty(value = "Attendee Role (ATTENDEE, HOST)", required = true)
	public String getQASessionRole() {
		return qasessionRole;
	}  // String getSessionRole

	public void setQASessionRole(String qasessionRole) {
		this.qasessionRole = qasessionRole;
	}  // void setSessionRole
	
	@ApiModelProperty(value = "QASession User Id of the attendee", required = true)
	public String getUserId() {
		return userId;
	}  // String getUserId

	public void setUserId(String userId) {
		this.userId = userId;
	}  // void setUserId

	@ApiModelProperty(value = "Timestamp of last modification", required = false)
	public Calendar getUpdateTimestamp() {
		return updateTimestamp;
	}  // Calendar getUpdateTimestamp

	public void setUpdateTimestamp(Calendar updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}  // setUpdateTimestamp
}  // class Attendee
