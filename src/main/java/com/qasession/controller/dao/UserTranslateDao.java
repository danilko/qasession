package com.qasession.controller.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.qasession.controller.model.UserTranslate;
import com.qasession.controller.security.UserInfo;

public interface UserTranslateDao {
	public List <UserTranslate> getUserTranslatesByLoginUserIdTypeLoginUserType(String pLoginUserId, String pLoginUserType);
	public List <UserTranslate> getAllUserTranslate();
	public UserTranslate getUserTranslateById(String pUserId) throws Exception;
    public UserTranslate createUserTranslate(UserTranslate pUserTranslate) throws Exception;
    public void deleteUserTranslateById(String pUserId) throws Exception;
    public UserTranslate updateUserTranslate(UserTranslate pUserTranslate) throws Exception;
    public UserInfo getUserInfo(HttpServletRequest pHttpServletRequest) throws Exception;
}  // interface UserTranslateDao
