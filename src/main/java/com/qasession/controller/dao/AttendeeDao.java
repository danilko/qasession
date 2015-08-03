package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.Attendee;

public interface AttendeeDao 
{
	public List <Attendee> getAttendeeByKeyValue(String pKeyName, String pKeyValue) throws Exception;
	public Attendee createAttendee(Attendee pAttendee) throws Exception;
	public Attendee getAttendeeByQASessionIdUserId(String pQAessionId, String pUserId) throws Exception;
	public void deleteAttendeeByQASessionIdUserId(String pQAessionId, String pUserId) throws Exception;
	public Attendee updateAttende(Attendee pAttendee) throws Exception;
}  //  interface AttendeeDao 
