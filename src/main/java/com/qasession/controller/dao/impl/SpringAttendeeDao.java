package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringAttendeeDao implements AttendeeDao {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(SpringAttendeeDao.class);
	
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public List<Attendee> getAttendeeByKeyValue(String pKeyName,
			String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM Attendee attendee_object WHERE attendee_object."
				+ pKeyName + " LIKE :pKeyValue";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pKeyValue", pKeyValue);

		List<?> lQueryList = lQuery.getResultList();

		LOGGER.debug("FIND ATTENDEE SIZE: " + lQueryList.size());
		
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
	} // List <Attendee> getAttendeeByKeyValue

	@Transactional(rollbackFor = Throwable.class)
	public Attendee createAttendee(Attendee pAttendee) throws Exception {
		pAttendee.setAttendeeId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));

		mEntityManager
				.createNativeQuery(
						"INSERT INTO ATTENDEE (ATTENDEE_ID,SESSION_ID,USER_ID,SESSION_ROLE,UPDATE_DATE) VALUES (?, ?, ?, ?, ?)")
				.setParameter(1, pAttendee.getAttendeeId())
				.setParameter(2,
						pAttendee.getSession().getSessionId())
				.setParameter(3,
						pAttendee.getUserTranslate().getUserId())
				.setParameter(4, pAttendee.getSessionRole())
				.setParameter(5, Calendar.getInstance(),
						TemporalType.TIMESTAMP).executeUpdate();

		return getAttendeeBySessionIdUserId(pAttendee.getSession()
				.getSessionId(), pAttendee.getUserTranslate().getUserId());
	} // Attendee createAttendee

	@Transactional(rollbackFor = Throwable.class)
	public Attendee updateAttende(Attendee pAttendee) throws Exception {
		Attendee oldAttendee = getAttendeeBySessionIdUserId(pAttendee
				.getSession().getSessionId(), pAttendee.getUserTranslate()
				.getUserId());

		oldAttendee.setSessionRole(pAttendee.getSessionRole());

		mEntityManager.persist(oldAttendee);

		return getAttendeeBySessionIdUserId(oldAttendee.getSession()
				.getSessionId(), oldAttendee.getUserTranslate().getUserId());
	} // Attendee updateAttende

	@Transactional(rollbackFor = Throwable.class)
	public Attendee getAttendeeBySessionIdUserId(String pSessionId,
			String pUserId) throws Exception {

		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM Attendee attendee_object WHERE attendee_object.session.sessionId LIKE :pSessionId AND attendee_object.userTranslate.userId LIKE :pUserId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pSessionId", pSessionId);
		lQuery = lQuery.setParameter("pUserId", pSessionId);

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

		if (lList.size() > 0) {
			return lList.get(0);
		} // if

		return null;
	} // Attendee getAttendeeBySessionIdAttendeeEmail

	@Transactional(rollbackFor = Throwable.class)
	public void deleteAttendeeByAttendeeId(String pAttendeeId) throws Exception {
		mEntityManager
				.createQuery(
						"DELETE FROM Answer answer_object WHERE answer_object.question.createBy.attendeeId = :attendeeId")
				.setParameter("attendeeId", pAttendeeId).executeUpdate();

		mEntityManager
				.createQuery(
						"DELETE FROM Question question_object WHERE question_object.createBy.attendeeId = :attendeeId")
				.setParameter("attendeeId", pAttendeeId).executeUpdate();

		mEntityManager
				.createQuery(
						"DELETE FROM Attendee attendee_object WHERE attendee_object.attendeeId = :attendeeId")
				.setParameter("attendeeId", pAttendeeId).executeUpdate();
	} // void deleteAttendeeBySessionIdUserId
} // SpringAttendeeDao
