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
 * @see QuestionDao
 * SpringQuestionDao Service class
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

import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.model.Question;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringQuestionDao implements QuestionDao {
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	public List<Question> getQuestionsBySessionIdUserId(String pSessionId,
			String pUserId) {
		// Create query to find info
		String lBasedQuery = "SELECT question_object FROM Question question_object WHERE question_object.qasessionId = :qasessionId AND question_object.createdBy = :userId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("qasessionId", pSessionId).setParameter(
				"userId", pUserId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<Question> lLists = new ArrayList<Question>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof Question) {
				lLists.add((Question) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lLists;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Question getQuestionById(String pQuestionId) {
		// Create query to find info
		String lBasedQuery = "SELECT question_object FROM Question question_object WHERE question_object.questionId = :questionId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("questionId", pQuestionId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<Question> lLists = new ArrayList<Question>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof Question) {
				lLists.add((Question) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		if (lLists.size() > 0) {
			return lLists.get(0);
		} // if

		return null;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Question createQuestion(Question pQuestion) {
		pQuestion.setQuestionId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));

		mEntityManager
				.createNativeQuery(
						"INSERT INTO question (question_id,question_content,qasession_id,created_by, updated_by ,question_status,create_timestamp,update_timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
				.setParameter(1, pQuestion.getQuestionId())
				.setParameter(2, pQuestion.getQuestionContent())
				.setParameter(3,
						pQuestion.getQASessionId())
				.setParameter(4,
						pQuestion.getCreatedBy())
				.setParameter(5,
						pQuestion.getUpdatedBy())
				.setParameter(6, pQuestion.getQuestionStatus())
				.setParameter(7, Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
				.setParameter(8, Calendar.getInstance(TimeZone.getTimeZone("UTC")),
						TemporalType.TIMESTAMP)
						.executeUpdate();

		return getQuestionById(pQuestion.getQuestionId());
	} // Question createQuestion

	@Transactional(rollbackFor = Throwable.class)
	public void deleteQuestionById(String pQuestionId) {
		mEntityManager
				.createNativeQuery(
						"DELETE FROM question WHERE question_id = ?")
				.setParameter(1, pQuestionId).executeUpdate();
		mEntityManager.flush();
	} // void deleteQuestionById

	@Transactional(rollbackFor = Throwable.class)
	public Question updateQuestion(Question pQuestion) {
		Question oldQuestion = getQuestionById(pQuestion.getQuestionId());
		oldQuestion.setQuestionStatus(pQuestion.getQuestionStatus());
		oldQuestion.setQuestionContent(pQuestion.getQuestionContent());
		
		oldQuestion.setQASessionId(pQuestion.getQASessionId());
		
		oldQuestion.setUpdatedBy(pQuestion.getUpdatedBy());
		oldQuestion.setCreatedBy(pQuestion.getCreatedBy());
		
		oldQuestion.setUpdateTimestamp(Calendar.getInstance(TimeZone.getTimeZone("UTC")));

		mEntityManager.persist(oldQuestion);

		return oldQuestion;
	} // Question updateQuestion

} // class SpringQuestionDao
