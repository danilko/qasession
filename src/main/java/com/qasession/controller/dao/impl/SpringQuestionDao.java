package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
		String lBasedQuery = "SELECT question_object FROM Question question_object WHERE question_object.session.sessionId = :sessionId AND question_object.createdBy.userTranslate.userId = :userId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("sessionId", pSessionId).setParameter(
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
						"INSERT INTO QUESTION (QUESTION_ID,QUESTION_CONTENT,SESSION_ID,CREATED_BY,QUESTION_STATUS,UPDATE_DATE) VALUES (?, ?, ?, ?, ?, ?)")
				.setParameter(1, pQuestion.getQuestionId())
				.setParameter(2, pQuestion.getQuestionContent())
				.setParameter(3,
						pQuestion.getSession().getSessionId())
				.setParameter(4,
						pQuestion.getCreatedBy().getAttendeeId())
				.setParameter(5, pQuestion.getQuestionStatus())
				.setParameter(6, Calendar.getInstance(),
						TemporalType.TIMESTAMP).executeUpdate();

		return getQuestionById(pQuestion.getQuestionId());
	} // Question createQuestion

	@Transactional(rollbackFor = Throwable.class)
	public void deleteQuestionById(String pQuestionId) {
		mEntityManager
				.createQuery(
						"DELETE FROM Answer answer_object WHERE answer_object.question.questionId = :questionId")
				.setParameter("questionId", pQuestionId).executeUpdate();
		mEntityManager.flush();
		mEntityManager
				.createQuery(
						"DELETE FROM Question question_object WHERE question_object.questionId = :questionId")
				.setParameter("questionId", pQuestionId).executeUpdate();
	} // void deleteQuestionById

	@Transactional(rollbackFor = Throwable.class)
	public Question updateQuestion(Question pQuestion) {
		Question oldQuestion = getQuestionById(pQuestion.getQuestionId());
		oldQuestion.setQuestionStatus(pQuestion.getQuestionStatus());
		oldQuestion.setQuestionContent(pQuestion.getQuestionContent());
		oldQuestion.setUpdateDate(Calendar.getInstance());

		mEntityManager.persist(oldQuestion);

		return oldQuestion;
	} // Question updateQuestion

} // class SpringQuestionDao
