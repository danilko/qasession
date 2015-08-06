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

	@Transactional(rollbackFor = Throwable.class)
	public List<Question> getQuestionsByKeyValue(String pKeyName,
			String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT question_object FROM Question question_object WHERE question_object."
				+ pKeyName + " LIKE :pKeyValue";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pKeyValue", pKeyValue);

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
		List<Question> lList = getQuestionsByKeyValue("questionId", pQuestionId);

		if (lList.size() > 0) {
			return lList.get(0);
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
