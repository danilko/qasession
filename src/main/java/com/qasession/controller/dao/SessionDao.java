package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.Session;

public interface SessionDao 
{
	public List <Session> getSessionsByKeyValue(String pKeyName, String pKeyValue) throws Exception;
	public Session getSessionById(String pSessionId) throws Exception;
    public Session createSession(Session pSession) throws Exception;
    public void deleteSessionById(String pSessionId) throws Exception;
    public Session updateSession(Session pSession) throws Exception;
}  // interface SessionDao 
