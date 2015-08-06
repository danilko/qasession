package com.qasession.controller.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.UserTranslate;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Produces("application/json")
@Path("/user")
public class UserService {
	private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	
	@Resource(shareable=true, name="getUserTranslateDao")
	private UserTranslateDao mUserTranslateDao;
	
	@GET
	@ApiOperation(value = "Get current token info", notes = "Return user info for this user")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response getUserTranslate(@Context HttpServletRequest pHttpServletRequest ) {
		try {
			UserInfo lUserInfo = (UserInfo)pHttpServletRequest.getSession().getAttribute(FacebookClient.getUserInfoSessionId());
			
			List <UserTranslate> lList = mUserTranslateDao.getUserTranslatesByLoginUserIdTypeLoginUserType(lUserInfo.getFacebookProfileId(), "FACEBOOK");
			
			
			UserTranslate lUserTranslate = null;
			
			if(lList.size() > 0)
			{
				lUserTranslate = lList.get(0);
			}  // if
			
			if(lUserTranslate == null)
			{
				lUserTranslate = new UserTranslate();
				lUserTranslate.setFacebookUserId(lUserInfo.getFacebookProfileId());
				lUserTranslate.setLoginUserIdType("FACEBOOK");
				lUserTranslate.setName(lUserInfo.getName());
				
				lUserTranslate = mUserTranslateDao.createUserTranslate(lUserTranslate);
			}  // if
			else if(lUserInfo.getUserId() == null)
			{
				lUserTranslate.setName(lUserInfo.getName());
				
				lUserTranslate = mUserTranslateDao.updateUserTranslate(lUserTranslate);
			}  // else if
			
			lUserInfo.setUserId(lUserTranslate.getUserId());
			
			pHttpServletRequest.getSession().setAttribute(FacebookClient.getUserInfoSessionId(), lUserInfo);
			
			return Response
					.ok()
					.entity(lUserInfo).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error performing the request", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
	
	@Path("/_all")
	@GET
	@ApiOperation(value = "Get current token info", notes = "Return user info for this user")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response getAllUserTranslate()
	{
		try{
			return Response
					.ok()
					.entity(mUserTranslateDao.getAllUserTranslate()).build();
	} // try
	catch (Exception pExeception) {
		LOGGER.error("Error performing the request", pExeception);
		return Response.serverError().build();
	} // catch
	}
	
	@Path("/token")
	@GET
	@ApiOperation(value = "Get current token info", notes = "Returns all attendee record that this session belong")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response getCurrentUserInfo(@Context HttpServletRequest pHttpServletRequest ) {
		try {
			return Response
					.ok()
					.entity(pHttpServletRequest.getSession().getAttribute(FacebookClient.getUserInfoSessionId())).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
	
	@POST
	@Path("/logout")
	@ApiOperation(value = "Expire all tokens and cookies", notes = "Expire all tokens and cookies")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All sessions deleted"), @ApiResponse(code = 503, message = "Internal Server Error") })
	public Response logout(@Context HttpServletRequest pHttpServletRequest ) {
		try {
			UserInfo lUserInfo = (UserInfo)pHttpServletRequest.getSession().getAttribute(FacebookClient.getUserInfoSessionId());
			
			HttpSession lSession = pHttpServletRequest.getSession();

			lSession.setAttribute(FacebookClient.getUserInfoSessionId(), null);
			lSession.setAttribute(FacebookClient.getFacebookSessionCodeId(lUserInfo.getFacebookProfileId()), null);
			lSession.setAttribute(FacebookClient.getFacebookSessionAccessTokenId(lUserInfo.getFacebookProfileId()), null);
			lSession.setAttribute(FacebookClient.getFacebookSessioUserId(lUserInfo.getFacebookProfileId()), null);
			
			return Response.ok().build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
}  // class UserTranslateService
