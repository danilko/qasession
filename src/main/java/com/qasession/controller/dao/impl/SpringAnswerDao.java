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
 * @see AnswerDao
 * SpringAnswerDao Service class
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
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.AnswerDao;
import com.qasession.controller.model.Answer;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringAnswerDao implements AnswerDao {
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public Answer createAnswer(Answer pAnswer) throws Exception {
		pAnswer.setAnswerId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));

		mEntityManager
				.createNativeQuery(
						"INSERT INTO answer (answer_id,answer_content,question_id,created_by,updated_by,create_timestamp,update_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)")
				.setParameter(1, pAnswer.getAnswerId())
				.setParameter(2, pAnswer.getAnswerContent())
				.setParameter(3, pAnswer.getQuestionId())
				.setParameter(4, pAnswer.getCreatedBy())
				.setParameter(5, pAnswer.getUpdatedBy())
				.setParameter(6, Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
				.setParameter(7, Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP).executeUpdate();

		return getAnswerById(pAnswer.getAnswerId());
	}

	@Transactional(rollbackFor = Throwable.class)
	public void deleteAnswerById(String pAnswerId) throws Exception {
		mEntityManager
				.createNativeQuery(
						"DELETE FROM answer WHERE answer_id = ?")
				.setParameter(1, pAnswerId).executeUpdate();
		mEntityManager.flush();
	} // void deleteAnswerById

	@Transactional(rollbackFor = Throwable.class)
	public Answer updateAnswerById(Answer pAnswer) throws Exception {
		Answer oldAnswer = getAnswerById(pAnswer.getAnswerId());
		oldAnswer.setCreatedBy(pAnswer.getCreatedBy());
		oldAnswer.setUpdatedBy(pAnswer.getUpdatedBy());
		oldAnswer.setAnswerContent(pAnswer.getAnswerContent());
		oldAnswer.setUpdateTimestamp(Calendar.getInstance(TimeZone.getTimeZone("UTC")));

		mEntityManager.persist(oldAnswer);

		return oldAnswer;
	} // Answer updateAttendeeById

	public Answer getAnswerById(String pAnswerId) throws Exception {
		// Create query to find session info
		String lBasedQuery = "SELECT answer_object FROM Answer answer_object WHERE answer_object.answerId = :answerId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("answerId", pAnswerId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<Answer> lLists = new ArrayList<Answer>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof Answer) {
				lLists.add((Answer) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		if(lLists.size() > 0)
		{
			return lLists.get(0);
		}

		return null;
	} // Answer getAnswerById

}
