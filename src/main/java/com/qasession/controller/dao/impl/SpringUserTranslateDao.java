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
 * @see UserTranslateDao
 * SpringUserTranslateDo Service class
 */

package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.common.util.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.UserTranslate;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringUserTranslateDao implements UserTranslateDao {
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;

	@Transactional(rollbackFor = Throwable.class)
	public UserTranslate getUserTranslateByUserId(String pUserId) throws Exception {

		// Create query to find info
		String lBasedQuery = "SELECT user_translate_object FROM UserTranslate user_translate_object WHERE user_translate_object.userId = :userId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("userId", pUserId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<UserTranslate> lLists = new ArrayList<UserTranslate>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof UserTranslate) {
				lLists.add((UserTranslate) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		if (lLists.size() > 0)
		{
			return lLists.get(0);
		}  // if
		
		return null;
	}  // UserTranslate getUserTranslateByUserId

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public UserTranslate createUserTranslate(UserTranslate pUserTranslate)
			throws Exception {
		UserTranslate newUserTranslate = new UserTranslate();

		newUserTranslate.setUserId(RandomStringGenerator
				.generator(RandomStringGenerator.ID_LENTH));
		newUserTranslate.setFacebookUserId(pUserTranslate.getFacebookUserId());
		newUserTranslate.setName(pUserTranslate.getName());
		newUserTranslate
				.setLoginUserIdType(pUserTranslate.getLoginUserIdType());
		newUserTranslate.setUpdateTimestamp(Calendar.getInstance());
		newUserTranslate.setCreateTimestamp(Calendar.getInstance());

		mEntityManager
				.createNativeQuery(
						"INSERT INTO usertranslate (user_id,facebook_user_id,name, login_user_id_type, update_timestamp, create_timestamp) VALUES (?, ?, ?, ?, ?, ?)")
				.setParameter(1, newUserTranslate.getUserId())
				.setParameter(2, newUserTranslate.getFacebookUserId())
				.setParameter(3, newUserTranslate.getName())
				.setParameter(4, newUserTranslate.getLoginUserIdType())
				.setParameter(5, newUserTranslate.getUpdateTimestamp())
				.setParameter(6, newUserTranslate.getCreateTimestamp())
				.executeUpdate();

		return newUserTranslate;
	}

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public void deleteUserTranslateById(String pUserId) throws Exception {
		// TODO: Determine if deletion is necessary as it will impact multiple
		// session
	}

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public UserTranslate updateUserTranslate(UserTranslate pUserTranslate)
			throws Exception {
		UserTranslate oldUserTranslate = getUserTranslateByUserId(pUserTranslate
				.getUserId());
		oldUserTranslate.setFacebookUserId(pUserTranslate.getFacebookUserId());
		oldUserTranslate.setGoogleUserId(pUserTranslate.getGoogleUserId());
		oldUserTranslate.setTwitterUserId(pUserTranslate.getTwitterUserId());
		oldUserTranslate.setName(pUserTranslate.getName());
		oldUserTranslate
				.setLoginUserIdType(pUserTranslate.getLoginUserIdType());
		oldUserTranslate.setUpdateTimestamp(Calendar.getInstance());
		
		mEntityManager.persist(oldUserTranslate);

		return oldUserTranslate;
	}

	@Transactional(readOnly = true, rollbackFor = Throwable.class)
	public List<UserTranslate> getUserTranslatesByLoginUserIdTypeLoginUserType(
			String pLoginUserId, String pLoginUserType) {

		String lLoginSearchIDField = null;

		if (pLoginUserType.equalsIgnoreCase(UserInfo.LOGIN_TYPE_FACEBOOK)) {
			lLoginSearchIDField = "facebookUserId";
		}  // if
		else if (pLoginUserType.equalsIgnoreCase(UserInfo.LOGIN_TYPE_TWITTER)) {
			lLoginSearchIDField = "twitterUserId";
		}  // else if
		else if (pLoginUserType.equalsIgnoreCase(UserInfo.LOGIN_TYPE_GOOGLE)) {
			lLoginSearchIDField = "googleUserId";
		}  // else if

		// Create query to find info
		String lBasedQuery = "SELECT user_translate_object FROM UserTranslate user_translate_object WHERE user_translate_object."
				+ lLoginSearchIDField + " = :loginUserId";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("loginUserId", pLoginUserId);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<UserTranslate> lLists = new ArrayList<UserTranslate>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof UserTranslate) {
				lLists.add((UserTranslate) lObject);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lLists;
	} // List <UserTranslate> getUserTranslatesByLoginUserIdTypeLoginUserType

	@Transactional(readOnly = true, rollbackFor = Throwable.class)
	public List<UserTranslate> getAllUserTranslate() {

		// Create query to find info
		String lBasedQuery = "SELECT user_translate_object FROM UserTranslate user_translate_object";

		Query lQuery = mEntityManager.createQuery(lBasedQuery);

		List<?> lQueryList = lQuery.getResultList();

		// Create new list to store account
		List<UserTranslate> lLists = new ArrayList<UserTranslate>(0);

		// Check items in list and cast to account only if it is an instance
		// of
		// account object
		// Throw exception if there is an cast error
		for (Object lObject : lQueryList) {
			if (lObject instanceof UserTranslate) {
				UserTranslate lTempUserTranslate = (UserTranslate) lObject;
				UserTranslate lNewUserTranslate = new UserTranslate();
				
				lNewUserTranslate.setUserId(lTempUserTranslate.getUserId());
				lNewUserTranslate.setName(lTempUserTranslate.getName());
				lNewUserTranslate.setCreateTimestamp(lTempUserTranslate.getCreateTimestamp());
				lNewUserTranslate.setUpdateTimestamp(lTempUserTranslate.getUpdateTimestamp());

				lLists.add(lNewUserTranslate);
			} // if
			else {
				throw new ClassCastException();
			} // else
		} // for

		return lLists;
	} // List<UserTranslate> getAllUserTranslate


	@Transactional(readOnly = true, rollbackFor = Throwable.class)
	public UserInfo getUserInfo(HttpServletRequest pHttpServletRequest)
			throws Exception {
		UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
				.getAttribute(FacebookClient.getUserInfoSessionId());

		if (StringUtils.isEmpty(lUserInfo.getUserId())) {
			try {

				List<UserTranslate> lList = getUserTranslatesByLoginUserIdTypeLoginUserType(
						lUserInfo.getFacebookProfileId(), lUserInfo.getLoginType());

				UserTranslate lUserTranslate = null;

				if (lList.size() > 0) {
					lUserTranslate = lList.get(0);
				} // if

				if (lUserTranslate == null) {
					lUserTranslate = new UserTranslate();
					lUserTranslate.setFacebookUserId(lUserInfo
							.getFacebookProfileId());
					lUserTranslate.setLoginUserIdType(lUserInfo.getLoginType());
					lUserTranslate.setName(lUserInfo.getName());

					lUserTranslate = createUserTranslate(lUserTranslate);
				} // if
				else
				{
					lUserTranslate.setName(lUserInfo.getName());

					lUserTranslate = updateUserTranslate(lUserTranslate);
				}  // else

				lUserInfo.setUserId(lUserTranslate.getUserId());

				pHttpServletRequest.getSession().setAttribute(
						FacebookClient.getUserInfoSessionId(), lUserInfo);
			} // try
			catch (Exception pException) {

				throw pException;
			} // catch
		} // if
		return lUserInfo;
	} // UserInfo getUserInfo
}
