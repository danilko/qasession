package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.Question;

public interface QuestionDao {
	public Question getQuestionById(String pQuestionId) throws Exception;
	public List <Question> getQuestionsBySessionIdUserId(String pSessionId, String pQuestionId) throws Exception;
    public Question createQuestion(Question pQuestion) throws Exception;
    public void deleteQuestionById(String pQuestionId) throws Exception;
    public Question updateQuestion(Question pQuestion) throws Exception;
}  // interface QuestionDao
