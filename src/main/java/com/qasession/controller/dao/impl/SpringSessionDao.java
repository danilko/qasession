package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.model.Session;
import com.qasession.controller.utility.RandomStringGenerator;

public class SpringSessionDao implements SessionDao
{
	@PersistenceContext
	private EntityManager mEntityManager;
	
	public List<Session> getSessionsByKeyValue(String pKeyName, String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT session_object FROM session session_object WHERE session_object." + pKeyName.toLowerCase() + " LIKE :pKeyValue";
		
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

	public Session getSessionById(String pSessionId) {
		// Create query to find info
		String lBasedQuery = "SELECT session_object FROM answer session_object WHERE session_object.session_id LIKE :pSessionId";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pSessionId", pSessionId);

		Object lObject = lQuery.getResultList().get(0);
		
		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		if (lObject instanceof Session) {
			return ((Session) lObject);
		} // if
		else {
				throw new ClassCastException();
		} // else
	}  // Session getSessionsById

	public Session createSession(Session pSession) 
	{
		Session newSession = new Session();

		newSession.setSessionId(RandomStringGenerator.generator(RandomStringGenerator.ID_LENTH));
		
		newSession.setSessionDescription(pSession.getSessionDescription());
		newSession.setSessionTopic(pSession.getSessionTopic());
		
		newSession.setSessionDescription(pSession.getSessionDescription());
		newSession.setUpdateDate(Calendar.getInstance());
		
		newSession.setSessionMaxQuestion(pSession.getSessionMaxQuestion());
		
		mEntityManager.persist(newSession);
		
		return pSession;
	}  // createSession

	public void deleteSessionById(String pSessionId) {
		mEntityManager.remove(getSessionById(pSessionId));
	}  // deleteSessionById

	public Session updateSession(Session pSession) 
	{
		Session oldSession = getSessionById(pSession.getSessionId());
		
		oldSession.setSessionDescription(pSession.getSessionDescription());
		oldSession.setSessionTopic(pSession.getSessionTopic());
		
		oldSession.setSessionDescription(pSession.getSessionDescription());
		oldSession.setUpdateDate(Calendar.getInstance());
		
		oldSession.setSessionStatus(pSession.getSessionStatus());
		oldSession.setSessionMaxQuestion(pSession.getSessionMaxQuestion());
		
		mEntityManager.persist(oldSession);
		
		return oldSession;
	}  // Session updateSession

}  // class SpringSessionDao
