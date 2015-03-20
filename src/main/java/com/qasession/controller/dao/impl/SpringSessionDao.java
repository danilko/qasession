package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.model.Session;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringSessionDao implements SessionDao {
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public List<Session> getSessionsByKeyValue(String pKeyName, String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT session_object FROM Session session_object WHERE session_object."
				+ pKeyName + " LIKE :pKeyValue";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pKeyValue", pKeyValue);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<Session> lLists = new ArrayList<Session>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof Session) {
				lLists.add((Session) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lLists;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Session getSessionById(String pSessionId) {
		List<Session> lList = getSessionsByKeyValue("sessionId", pSessionId);

		if (lList.size() > 0) {
			return lList.get(0);
		} // if

		return null;
	} // Session getSessionsById

	@Transactional(readOnly = false, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public Session createSession(Session pSession, String pUserId)
			throws Exception {
		pSession.setSessionId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));

		mEntityManager
				.createNativeQuery(
						"INSERT INTO SESSION (SESSION_ID,SESSION_TOPIC,SESSION_DESCRIPTION,SESSION_STATUS,SESSION_MAX_QUESTION, UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?)")
				.setParameter(1, pSession.getSessionId())
				.setParameter(2, pSession.getSessionTopic())
				.setParameter(3, pSession.getSessionDescription())
				.setParameter(4, pSession.getSessionStatus())
				.setParameter(5, pSession.getSessionMaxQuestion())
				.setParameter(6, Calendar.getInstance(),
						TemporalType.TIMESTAMP).executeUpdate();

		return getSessionById(pSession.getSessionId());
	} // createSession

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public void deleteSessionById(String pSessionId) {
		mEntityManager
				.createQuery(
						"DELETE FROM Answer answer_object WHERE answer_object.question.session.sessionId = :sessionId")
				.setParameter("sessionId", pSessionId).executeUpdate();
		mEntityManager.flush();
		mEntityManager
				.createQuery(
						"DELETE FROM Question question_object WHERE question_object.session.sessionId = :sessionId")
				.setParameter("sessionId", pSessionId).executeUpdate();
		mEntityManager.flush();
		mEntityManager
				.createQuery(
						"DELETE FROM Attendee attendee_object WHERE attendee_object.session.sessionId = :sessionId")
				.setParameter("sessionId", pSessionId).executeUpdate();
		mEntityManager.flush();
		mEntityManager
				.createQuery(
						"DELETE FROM Session session_object WHERE session_object.sessionId = :sessionId")
				.setParameter("sessionId", pSessionId).executeUpdate();
	} // deleteSessionById

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public Session updateSession(Session pSession) {
		Session oldSession = getSessionById(pSession.getSessionId());

		oldSession.setSessionDescription(pSession.getSessionDescription());
		oldSession.setSessionTopic(pSession.getSessionTopic());

		oldSession.setSessionDescription(pSession.getSessionDescription());
		oldSession.setUpdateDate(Calendar.getInstance());

		oldSession.setSessionStatus(pSession.getSessionStatus());
		oldSession.setSessionMaxQuestion(pSession.getSessionMaxQuestion());

		mEntityManager.persist(oldSession);

		return getSessionById(oldSession.getSessionId());
	} // Session updateSession
} // class SpringSessionDao
