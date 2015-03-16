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

import com.qasession.controller.dao.AnswerDao;
import com.qasession.controller.model.Answer;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringAnswerDao implements AnswerDao {
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public List<Answer> getAnswerByKeyValue(String pKeyName, String pKeyValue)
			throws Exception {
		// Create query to find session info
		String lBasedQuery = "SELECT answer_object FROM Answer answer_object WHERE answer_object."
				+ pKeyName + " LIKE :pKeyValue";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pKeyValue", pKeyValue);

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

		return lLists;
	} // List<Answer> getAnswerByKeyValue

	@Transactional(rollbackFor = Throwable.class)
	public Answer createAnswer(Answer pAnswer) throws Exception {
		pAnswer.setAnswerId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));

		mEntityManager
				.createNativeQuery(
						"INSERT INTO ANSWER (ANSWER_ID,ANSWER_CONTENT,QUESTION_ID,CREATED_BY,UPDATE_DATE) VALUES (:answerId, :answerContent, :questionId, :createBy, :updateDate)")
				.setParameter("answerId", pAnswer.getAnswerId())
				.setParameter("answerContent", pAnswer.getAnswerContent())
				.setParameter("createBy",
						pAnswer.getCreatedBy().getAttendeeId())
				.setParameter("updateDate", Calendar.getInstance(),
						TemporalType.TIMESTAMP).executeUpdate();

		return getAnswerById(pAnswer.getAnswerId());
	}

	@Transactional(rollbackFor = Throwable.class)
	public void deleteAnswerById(String pAnswerId) throws Exception {
		mEntityManager
				.createQuery(
						"DELETE FROM Answer answer_object WHERE answer_object.answerId = :answerId")
				.setParameter("answerId", pAnswerId).executeUpdate();
	} // void deleteAnswerById

	@Transactional(rollbackFor = Throwable.class)
	public Answer updateAnswerById(Answer pAnswer) throws Exception {
		Answer oldAnswer = getAnswerById(pAnswer.getAnswerId());
		oldAnswer.setCreatedBy(pAnswer.getCreatedBy());
		oldAnswer.setAnswerContent(pAnswer.getAnswerContent());
		oldAnswer.setUpdateDate(Calendar.getInstance());

		mEntityManager.persist(oldAnswer);

		return oldAnswer;
	} // Answer updateAttendeeById

	public Answer getAnswerById(String pAnswerId) throws Exception {
		List<Answer> lList = getAnswerByKeyValue("answerId", pAnswerId);

		if (lList.size() > 0) {
			return lList.get(0);
		} // if

		return null;
	} // Answer getAnswerById

}
