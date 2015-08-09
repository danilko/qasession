package com.qasession.controller.service;


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
	@ApiOperation(value = "Get current user info", response = UserTranslate.class, notes = "Return user info for this user")
	@ApiResponses(value = { @ApiResponse(code = 403, message = "Not authorized") })
	public Response getUserTranslate(@Context HttpServletRequest pHttpServletRequest ) {
		try {
            return Response
					.ok()
					.entity(mUserTranslateDao.getUserInfo(pHttpServletRequest)).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error performing the request", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
	
	@Path("/_all")
	@GET
	@ApiOperation(value = "Get current token info", response = UserTranslate.class, responseContainer = "List", notes = "Return user info for this user")
	@ApiResponses(value = {@ApiResponse(code = 200, message = "Return All User Translate"), @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
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
	
	@POST
	@Path("/logout")
	@ApiOperation(value = "Expire all tokens and cookies", notes = "Expire all tokens and cookies")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All sessions deleted"), @ApiResponse(code = 503, message = "Internal Server Error") })
	public Response logout(@Context HttpServletRequest pHttpServletRequest ) {
		try {
			HttpSession lSession = pHttpServletRequest.getSession();

			lSession.invalidate();
			
			return Response.ok().build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
}  // class UserTranslateService
