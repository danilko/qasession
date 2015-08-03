package com.qasession.controller.dao;

import java.util.List;

import com.qasession.controller.model.UserTranslate;

public interface UserTranslateDao {
	public List <UserTranslate> getUserTranslatesByKeyValue(String pKeyName, String pKeyValue) throws Exception;
	public List <UserTranslate> getUserTranslatesByLoginUserIdTypeLoginUserType(String pLoginUserId, String pLoginUserType);
	public UserTranslate getUserTranslateById(String pUserId) throws Exception;
    public UserTranslate createUserTranslate(UserTranslate pUserTranslate) throws Exception;
    public void deleteUserTranslateById(String pUserId) throws Exception;
    public UserTranslate updateUserTranslate(UserTranslate pUserTranslate) throws Exception;
}  // interface UserTranslateDao
