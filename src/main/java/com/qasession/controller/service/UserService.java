package com.qasession.controller.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Produces("application/json")
@Path("/user")
public class UserService {
	
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
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
}
