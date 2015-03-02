package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.model.Question;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringQuestionDao implements QuestionDao
{
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public List<Question> getQuestionsByKeyValue(String pKeyName,
			String pKeyValue) {
		// Create query to find info
		String lBasedQuery = "SELECT question_object FROM Question question_object WHERE question_object." + pKeyName + " LIKE :pKeyValue";
		
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

	@Transactional(rollbackFor = Throwable.class)
	public Question getQuestionById(String pQuestionId) 
	{
		List<Question> lList = getQuestionsByKeyValue("questionId", pQuestionId);
		
		if(lList.size() > 0)
		{
			return lList.get(0);
		}  // if
		
		return null;
	}

	@Transactional(rollbackFor = Throwable.class)
	public Question createQuestion(Question pQuestion) 
	{
		Question newQuestion = new Question();
		
		newQuestion.setQuestionId(RandomStringGenerator.generator(RandomStringGenerator.ID_LENTH));
		
		newQuestion.setSession(pQuestion.getSession());
		
		newQuestion.setQuestionId(pQuestion.getQuestionStatus());
		
		newQuestion.setCreatedBy(pQuestion.getCreatedBy());
		
		newQuestion.setQuestionStatus(pQuestion.getQuestionStatus());
		newQuestion.setQuestionContent(pQuestion.getQuestionContent());
		newQuestion.setUpdateDate(Calendar.getInstance());
		
		mEntityManager.persist(newQuestion);
		
		return newQuestion;
	}  // Question createQuestion

	@Transactional(rollbackFor = Throwable.class)
	public void deleteQuestionById(String pQuestionId) 
	{
		mEntityManager.remove(getQuestionById(pQuestionId));
	}  // void deleteQuestionById

	@Transactional(rollbackFor = Throwable.class)
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
