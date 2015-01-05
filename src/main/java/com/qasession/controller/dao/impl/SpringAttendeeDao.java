package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.model.Attendee;

public class SpringAttendeeDao implements AttendeeDao
{
	@PersistenceContext
	private EntityManager mEntityManager;
	
	public List<Attendee> getAttendeeByKeyValue(String pKeyName,
			String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM attendee attendee_object WHERE attendee_object." + pKeyName.toLowerCase() + " LIKE :pKeyValue";
		
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

	public Attendee createAttendee(Attendee pAttendee) throws Exception
	{
		mEntityManager.persist(pAttendee);
		
		return pAttendee;
	}  // Attendee createAttendee

	public Attendee updateAttende(Attendee pAttendee) throws Exception
	{
		Attendee oldAttendee = getAttendeeBySessionIdAttendeeEmail(pAttendee.getSessionId(), pAttendee.getEmail());
		
		oldAttendee.setSessionRole(pAttendee.getSessionRole());
		
		mEntityManager.persist(oldAttendee);
		
		return oldAttendee;
	}  // Attendee updateAttende

	public Attendee getAttendeeBySessionIdAttendeeEmail(String pSessionId,
			String pAttendeeEmail) throws Exception {
		
		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM attendee attendee_object WHERE attendee_object.session_id LIKE :pSessionId AND attendee_object.attendee_email LIKE :pAttendeeEmail";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pSessionId", pSessionId);
		lQuery = lQuery.setParameter("pAttendeeEmail", pSessionId);
		
		Object lObject = lQuery.getResultList().get(0);
		
		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		if (lObject instanceof Attendee) {
			return ((Attendee) lObject);
		} // if
		else {
				throw new ClassCastException();
		} // else
	}  // Attendee getAttendeeBySessionIdAttendeeEmail

	public void deleteAttendeeBySessionIdAttendeeEmail(String pSessionId,
			String pAttendeeEmail) throws Exception {
		mEntityManager.remove(getAttendeeBySessionIdAttendeeEmail(pSessionId,pAttendeeEmail));
	}  // void deleteAttendeeBySessionIdAttendeeEmail
}  // SpringAttendeeDao
