package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.Answer;

public interface AnswerDao 
{
	public List <Answer> getAnswerByKeyValue(String pKeyName, String pKeyValue) throws Exception;
	public Answer createAnswer(Answer pAnswer) throws Exception;
	public Answer getAnswerById(String pAnswerId) throws Exception;
	public void deleteAnswerById(String pAnswerId) throws Exception;
	public Answer updateAnswerById(Answer pAnswer) throws Exception;
}  // interface AnswerDao
