package com.qasession.controller.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.UserTranslate;
import com.qasession.controller.utility.RandomStringGenerator;

@Repository
public class SpringUserTranslateDao implements UserTranslateDao
{
	@PersistenceContext(unitName = "getEntityManagerFactoryBean")
	private EntityManager mEntityManager;
	
	@Transactional(rollbackFor = Throwable.class)
	public List<UserTranslate> getUserTranslatesByKeyValue(String pKeyName,
			String pKeyValue) throws Exception {
		
		// Create query to find info
		String lBasedQuery = "SELECT user_translate_object FROM UserTranslate user_translate_object WHERE user_translate_object." + pKeyName + " LIKE :pKeyValue";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pKeyValue", pKeyValue);

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
	}

	public UserTranslate getUserTranslateById(String pUserId) throws Exception 
	{
		List<UserTranslate> lList = getUserTranslatesByKeyValue("userId", pUserId);
		
		if(lList.size() > 0)
		{
			return lList.get(0);
		}  // if
		
		return null;
	}

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public UserTranslate createUserTranslate(UserTranslate pUserTranslate)
			throws Exception {
		UserTranslate newUserTranslate = new UserTranslate();

		newUserTranslate.setUserId(RandomStringGenerator.generator(RandomStringGenerator.ID_LENTH));
		newUserTranslate.setFacebookUserId(pUserTranslate.getFacebookUserId());
		newUserTranslate.setName(pUserTranslate.getName());
		newUserTranslate.setLoginUserIdType(pUserTranslate.getLoginUserIdType());
		
		mEntityManager.createNativeQuery("INSERT INTO usertranslate (user_id,facebook_user_id,name, login_user_id_type) VALUES (?, ?, ?, ?)")
		.setParameter(1, newUserTranslate.getUserId())
		.setParameter(2, newUserTranslate.getFacebookUserId())
		.setParameter(3, newUserTranslate.getName())
		.setParameter(4, newUserTranslate.getLoginUserIdType())
		.executeUpdate();
		
		return newUserTranslate;
	}
	
	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public void deleteUserTranslateById(String pUserId) throws Exception {
	//TODO: Determine if deletion is necessary as it will impact multiple session
	}

	@Transactional(readOnly = false, rollbackFor = Throwable.class)
	public UserTranslate updateUserTranslate(UserTranslate pUserTranslate)
			throws Exception {
		UserTranslate oldUserTranslate = getUserTranslateById(pUserTranslate.getUserId());
		oldUserTranslate.setFacebookUserId(pUserTranslate.getFacebookUserId());
		oldUserTranslate.setName(pUserTranslate.getName());
		oldUserTranslate.setLoginUserIdType(pUserTranslate.getLoginUserIdType());
		
		mEntityManager.persist(oldUserTranslate);
		
		return oldUserTranslate;
	}

	public List <UserTranslate> getUserTranslatesByLoginUserIdTypeLoginUserType(String pLoginUserId, String pLoginUserType)
	{

		String lLoginSearchIDField = null;
		
		if (pLoginUserType.equalsIgnoreCase("FACEBOOK"))
		{
			lLoginSearchIDField = "facebookUserId";
		}
		else if(pLoginUserType.equalsIgnoreCase("TWITTER"))
		{
			lLoginSearchIDField = "twitterUserId";
		}
			else if(pLoginUserType.equalsIgnoreCase("GOOGLE"))
		{
				lLoginSearchIDField = "googleUserId";
		}
		
		// Create query to find info
		String lBasedQuery = "SELECT user_translate_object FROM UserTranslate user_translate_object WHERE user_translate_object." + lLoginSearchIDField + " LIKE :pLoginUserId";
		
		Query lQuery = mEntityManager.createQuery(lBasedQuery);
		lQuery = lQuery.setParameter("pLoginUserId", pLoginUserId);

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
	}  // List <UserTranslate> getUserTranslatesByLoginUserIdTypeLoginUserType
}
