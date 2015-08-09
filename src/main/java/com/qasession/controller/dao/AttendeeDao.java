package com.qasession.controller.dao;

import com.qasession.controller.model.Attendee;

public interface AttendeeDao 
{
	public Attendee createAttendee(Attendee pAttendee) throws Exception;
	public Attendee getAttendeeByQASessionIdUserId(String pQAessionId, String pUserId) throws Exception;
	public void deleteAttendeeByQASessionIdUserId(String pQAessionId, String pUserId) throws Exception;
	public Attendee updateAttende(Attendee pAttendee) throws Exception;
}  //  interface AttendeeDao 
