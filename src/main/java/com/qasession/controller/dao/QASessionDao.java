package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.QASession;

public interface QASessionDao 
{
	public List <QASession> getQASessionsByKeyValue(String pKeyName, String pKeyValue) throws Exception;
	public List <QASession> getQASessionByUserId(String pUserId);
	public QASession getQASessionById(String pSessionId) throws Exception;
    public QASession createQASession(QASession pSession, String pUserId) throws Exception;
    public void deleteQASessionById(String pQASessionId) throws Exception;
    public QASession updateQASession(QASession pSession) throws Exception;
}  // interface SessionDao 
