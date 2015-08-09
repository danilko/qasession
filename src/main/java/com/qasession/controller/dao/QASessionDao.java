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
 * @see QASession
 * QASessionDao Service class
 */

package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.QASession;

public interface QASessionDao 
{
	public List <QASession> getAllQASessions();
	public List <QASession> getQASessionByUserId(String pUserId);
	public QASession getQASessionById(String pSessionId) throws Exception;
    public QASession createQASession(QASession pSession, String pUserId) throws Exception;
    public void deleteQASessionById(String pQASessionId) throws Exception;
    public QASession updateQASession(QASession pSession) throws Exception;
}  // interface SessionDao 
