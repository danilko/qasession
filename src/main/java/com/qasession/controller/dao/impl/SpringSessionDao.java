package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.model.Session;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringSessionDao implements SessionDao
{
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;
	
	@Transactional(rollbackFor = Throwable.class)
	public List<Session> getSessionsByKeyValue(String pKeyName, String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT session_object FROM Session session_object WHERE session_object." + pKeyName + " LIKE :pKeyValue";
		
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
		
		if(lList.size() > 0)
		{
			return lList.get(0);
		}  // if
		
		return null;
	}  // Session getSessionsById

	@Transactional(readOnly = false, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public Session createSession(Session pSession) 
	{
		Session newSession = new Session();

		newSession.setSessionId(RandomStringGenerator.generator(RandomStringGenerator.ID_LENTH));
		
		newSession.setSessionDescription(pSession.getSessionDescription());
		newSession.setSessionTopic(pSession.getSessionTopic());
		
		newSession.setSessionDescription(pSession.getSessionDescription());
		newSession.setUpdateDate(Calendar.getInstance());
		
		newSession.setSessionMaxQuestion(pSession.getSessionMaxQuestion());
	
		mEntityManager.createNativeQuery("INSERT INTO SESSION (SESSION_ID,SESSION_TOPIC,SESSION_DESCRIPTION,SESSION_STATUS,SESSION_MAX_QUESTION) VALUES ('" + pSession.getSessionId() + "','" + pSession.getSessionTopic() + "','" + pSession.getSessionDescription() + "','" + pSession.getSessionStatus() + "', " + pSession.getSessionMaxQuestion() +")").executeUpdate();;
		
		return newSession;
	}  // createSession

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public void deleteSessionById(String pSessionId) {
		mEntityManager.remove(getSessionById(pSessionId));
	}  // deleteSessionById

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public Session updateSession(Session pSession) 
	{
		Session oldSession = getSessionById(pSession.getSessionId());
		
		oldSession.setSessionDescription(pSession.getSessionDescription());
		oldSession.setSessionTopic(pSession.getSessionTopic());
		
		oldSession.setSessionDescription(pSession.getSessionDescription());
		oldSession.setUpdateDate(Calendar.getInstance());
		
		oldSession.setSessionStatus(pSession.getSessionStatus());
		oldSession.setSessionMaxQuestion(pSession.getSessionMaxQuestion());
		
		mEntityManager.createNativeQuery("UPDATE SESSION SET SESSION_TOPIC = '" + pSession.getSessionTopic() + "', SESSION_TOPIC'" + pSession.getSessionDescription() + "', SESSION_STATUS = '" + pSession.getSessionStatus() + "', SESSION_MAX_QUESTION = " + pSession.getSessionMaxQuestion() +" WHERE SESSION_ID = '" + pSession.getSessionId() + "')").executeUpdate();;
		
		return oldSession;
	}  // Session updateSession

}  // class SpringSessionDao
