package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.model.Question;
import com.qasession.controller.utility.RandomStringGenerator;

public class SpringQuestionDao implements QuestionDao
{
	@PersistenceContext
	private EntityManager mEntityManager;
	
	public List<Question> getQuestionsByKeyValue(String pKeyName,
			String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT question_object FROM question question_object WHERE question_object." + pKeyName.toLowerCase() + " LIKE :pKeyValue";
		
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

	public Question getQuestionById(String pQuestionId) 
	{
		// Create query to find info
		String lBasedQuery = "SELECT question_object FROM question question_object WHERE question_object.question_id LIKE :pQuestionId";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pQuestionId", pQuestionId);

		Object lObject = lQuery.getResultList().get(0);
		
		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		if (lObject instanceof Question) {
			return ((Question) lObject);
		} // if
		else {
				throw new ClassCastException();
		} // else
	}

	public Question createQuestion(Question pQuestion) 
	{
		Question newQuestion = new Question();
		
		newQuestion.setQuestionId(RandomStringGenerator.generator(RandomStringGenerator.ID_LENTH));
		
		newQuestion.setSessionId(pQuestion.getSessionId());
		
		newQuestion.setQuestionId(pQuestion.getQuestionStatus());
		
		newQuestion.setCreatedBy(pQuestion.getCreatedBy());
		
		newQuestion.setQuestionStatus(pQuestion.getQuestionStatus());
		newQuestion.setQuestionContent(pQuestion.getQuestionContent());
		newQuestion.setUpdateDate(Calendar.getInstance());
		
		mEntityManager.persist(newQuestion);
		
		return newQuestion;
	}  // Question createQuestion

	public void deleteQuestionById(String pQuestionId) 
	{
		mEntityManager.remove(getQuestionById(pQuestionId));
	}  // void deleteQuestionById

	public Question updateQuestion(Question pQuestion) 
	{
		Question oldQuestion = getQuestionById(pQuestion.getQuestionId());
		oldQuestion.setQuestionStatus(pQuestion.getQuestionStatus());
		oldQuestion.setQuestionContent(pQuestion.getQuestionContent());
		oldQuestion.setUpdateDate(Calendar.getInstance());
		
		mEntityManager.persist(oldQuestion);
		
		return oldQuestion;
	}  // Question updateQuestion

}
