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
 * @see QASessionDao
 * SpringQASessionDao Service class
 */

package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.QASessionDao;
import com.qasession.controller.model.QASession;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringQASessionDao implements QASessionDao {
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public List<QASession> getAllQASessions() {
		// Create query to find info
		String lBasedQuery = "SELECT qasession_object FROM QASession qasession_object";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<QASession> lList = new ArrayList<QASession>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof QASession) {
				lList.add((QASession) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lList;
	}  // getAllQASessions

	@Transactional(rollbackFor = Throwable.class)
	public QASession getQASessionById(String pQASessionId) {
		// Create query to find info
		String lBasedQuery = "SELECT qasession_object FROM QASession qasession_object WHERE qasession_object.qasessionId = :qasessionId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("qasessionId", pQASessionId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<QASession> lList = new ArrayList<QASession>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof QASession) {
				lList.add((QASession) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		if (lList.size() > 0) {
			return lList.get(0);
		} // if

		return null;
	} // Session getSessionsById

	@Transactional(readOnly = false, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public QASession createQASession(QASession pQASession, String pUserId)
			throws Exception {
		pQASession.setQASessionId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));

		mEntityManager
				.createNativeQuery(
						"INSERT INTO qasession (qasession_id,qasession_topic,qasession_description,qasession_status,qasession_max_question,create_timestamp,update_timestamp,created_by,updated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
				.setParameter(1, pQASession.getQASessionId())
				.setParameter(2, pQASession.getQASessionTopic())
				.setParameter(3, pQASession.getQASessionDescription())
				.setParameter(4, pQASession.getQASessionStatus())
				.setParameter(5, pQASession.getQASessionMaxQuestion())
				.setParameter(6,
						Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
				.setParameter(7,
						Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
				.setParameter(8, pQASession.getCreatedBy())
				.setParameter(9, pQASession.getUpdatedBy()).executeUpdate();

		return getQASessionById(pQASession.getQASessionId());
	} // createSession

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public void deleteQASessionById(String pQASessionId) {

		mEntityManager
				.createNativeQuery(
						"DELETE FROM qasession WHERE qasession_id = ?")
				.setParameter(1, pQASessionId).executeUpdate();
		mEntityManager.flush();
	} // deleteSessionById

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public QASession updateQASession(QASession pQASession) {
		QASession oldQASession = getQASessionById(pQASession.getQASessionId());

		oldQASession.setQASessionDescription(pQASession
				.getQASessionDescription());
		oldQASession.setQASessionTopic(pQASession.getQASessionTopic());

		oldQASession.setCreatedBy(oldQASession.getCreatedBy());
		oldQASession.setUpdateTimestamp(Calendar.getInstance(TimeZone
				.getTimeZone("UTC")));

		oldQASession.setQASessionStatus(pQASession.getQASessionStatus());
		oldQASession.setQASessionMaxQuestion(pQASession
				.getQASessionMaxQuestion());

		mEntityManager.persist(oldQASession);

		return getQASessionById(oldQASession.getQASessionId());
	} // Session updateSession

	public List<QASession> getQASessionByUserId(String pUserId) {
		// Create query to find info
		String lBasedQuery = "SELECT qasession_object FROM QASession qasession_object, Attendee attendee_object WHERE qasession_object.qasessionId = attendee_object.qasessionId AND attendee_object.userId = :userId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("userId", pUserId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<QASession> lLists = new ArrayList<QASession>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof QASession) {
				lLists.add((QASession) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lLists;
	} // List<QASession> getQASessionByUserId

} // class SpringSessionDao
