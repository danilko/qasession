package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.Attendee;

public interface AttendeeDao 
{
	public List <Attendee> getAttendeeByKeyValue(String pKeyName, String pKeyValue) throws Exception;
	public Attendee createAttendee(Attendee pAttendee) throws Exception;
	public Attendee getAttendeeBySessionIdUserId(String pSessionId, String pUserId) throws Exception;
	public void deleteAttendeeByAttendeeId(String pAttendeeId) throws Exception;
	public Attendee updateAttende(Attendee pAttendee) throws Exception;
}  //  interface AttendeeDao 
