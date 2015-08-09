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
 * @see AttendeeDao
 * SpringAttendeeDao Service class
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
	public Attendee createAttendee(Attendee pAttendee) throws Exception {
		pAttendee.setAttendeeId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));
		
		LOGGER.debug("create attendee" + pAttendee.getAttendeeId());
		
		mEntityManager
				.createNativeQuery(
						"INSERT INTO attendee (attendee_id, qasession_id, user_id,qasession_role,create_timestamp,update_timestamp) VALUES (?, ?, ?, ?, ?, ?)")
				.setParameter(1, pAttendee.getAttendeeId())
				.setParameter(2,
						pAttendee.getQASessionId())
				.setParameter(3,
						pAttendee.getUserId())
				.setParameter(4, pAttendee.getQASessionRole())
				.setParameter(5, Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
				.setParameter(6, Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
						.executeUpdate();

		return getAttendeeByQASessionIdUserId(pAttendee.getQASessionId(), pAttendee.getUserId());
	} // Attendee createAttendee

	@Transactional(rollbackFor = Throwable.class)
	public Attendee updateAttende(Attendee pAttendee) throws Exception {
		Attendee oldAttendee = getAttendeeByQASessionIdUserId(pAttendee
				.getQASessionId(), pAttendee
				.getUserId());

		oldAttendee.setQASessionRole(pAttendee.getQASessionRole());

		mEntityManager.persist(oldAttendee);

		return getAttendeeByQASessionIdUserId(oldAttendee
				.getQASessionId(), oldAttendee.getUserId());
	} // Attendee updateAttende

	@Transactional(rollbackFor = Throwable.class)
	public Attendee getAttendeeByQASessionIdUserId(String pQASessionId,
			String pUserId) throws Exception {

		// Create query to find info
		String lBasedQuery = "SELECT attendee_object FROM Attendee attendee_object WHERE attendee_object.qasessionId LIKE :qasessionId AND attendee_object.userId LIKE :userId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("qasessionId", pQASessionId);
		lQuery = lQuery.setParameter("userId", pUserId);

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
	public void deleteAttendeeByQASessionIdUserId(String pQASessionId,
			String pUserId) throws Exception {
		mEntityManager
				.createNativeQuery(
						"DELETE FROM attendee WHERE user_id = ? AND qasession_id = ?")
				.setParameter(1, pUserId)
				.setParameter(2, pQASessionId)
				.executeUpdate();
		mEntityManager.flush();
	} // void deleteAttendeeBySessionIdUserId
} // SpringAttendeeDao
