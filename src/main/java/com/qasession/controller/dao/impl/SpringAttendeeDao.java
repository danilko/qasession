package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringAttendeeDao implements AttendeeDao
{
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public List<Attendee> getAttendeeByKeyValue(String pKeyName,
			String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM Attendee attendee_object WHERE attendee_object." + pKeyName + " LIKE :pKeyValue";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pKeyValue", pKeyValue);

		List<?> lQueryList = lQuery.getResultList();
		
		// Create new list to store account
		List<Attendee> lLists = new ArrayList<Attendee>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof Attendee) {
				lLists.add((Attendee) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lLists;
	}  // List <Attendee> getAttendeeByKeyValue

	@Transactional(rollbackFor = Throwable.class)
	public Attendee createAttendee(Attendee pAttendee) throws Exception
	{
		pAttendee.setAttendeeId(RandomStringGenerator.generator(RandomStringGenerator.ID_LENTH));
		
		mEntityManager.persist(pAttendee);
		
		return pAttendee;
	}  // Attendee createAttendee

	@Transactional(rollbackFor = Throwable.class)
	public Attendee updateAttende(Attendee pAttendee) throws Exception
	{
		Attendee oldAttendee = getAttendeeBySessionIdUserId(pAttendee.getSession().getSessionId(), pAttendee.getUserId());
		
		oldAttendee.setSessionRole(pAttendee.getSessionRole());
		
		mEntityManager.persist(oldAttendee);
		
		return oldAttendee;
	}  // Attendee updateAttende

	@Transactional(rollbackFor = Throwable.class)
	public Attendee getAttendeeBySessionIdUserId(String pSessionId,
			String pUserId) throws Exception {
		
		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM Attendee attendee_object WHERE attendee_object.session.sessionId LIKE :pSessionId AND attendee_object.userId LIKE :pAttendeeEmail";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pSessionId", pSessionId);
		lQuery = lQuery.setParameter("pAttendeeEmail", pSessionId);
		
		List<?> lQueryList = lQuery.getResultList();
		
		// Create new list to store account
		List<Attendee> lList = new ArrayList<Attendee>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof Attendee) {
				lList.add((Attendee) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		if(lList.size() > 0)
		{
			return lList.get(0);
		}  // if
		
		return null;
	}  // Attendee getAttendeeBySessionIdAttendeeEmail

	@Transactional(rollbackFor = Throwable.class)
	public void deleteAttendeeByAttendeeId(String pAttendeeId) throws Exception {
		mEntityManager.remove(getAttendeeByKeyValue("attendeeId", pAttendeeId));
	}  // void deleteAttendeeBySessionIdUserId
}  // SpringAttendeeDao
