package com.qasession.controller.service;

import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.model.Session;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Produces("application/json")
@Path("/session")
public class SessionService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(SessionService.class);

	@Resource(shareable = true, name = "getSessionDao")
	private SessionDao mSessionDao;

	@Resource(shareable = true, name = "getUserTranslateDao")
	private UserTranslateDao mUserTranslateDao;

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;

	@GET
	@Path("/{sessionId}")
	@ApiOperation(value = "Find session by session ID", notes = "Return the session record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getSessionById(@PathParam("sessionId") String pSessionId) {
		try {
			Session lSession = mSessionDao.getSessionById(pSessionId);

			if (lSession != null) {
				return Response.ok().entity(lSession).build();
			} else {
				return Response.status(404).build();
			}

		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getSessionById

	@POST
	@Consumes("application/json")
	@Path("/")
	@ApiOperation(value = "Create a session", notes = "Create a session")
	@ApiResponses(value = { @ApiResponse(code = 403, message = "Not authorized") })
	public Response createSession(Session pSession,
			@Context HttpServletRequest pHttpServletRequest) {
		try {
			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Session newSession = mSessionDao.createSession(pSession,
					lUserInfo.getUserId());

			Attendee lAttendee = new Attendee();
			lAttendee.setSession(newSession);
			lAttendee.setUpdateDate(Calendar.getInstance());
			lAttendee.setSessionRole("HOST");
			lAttendee.setUserTranslate(mUserTranslateDao
					.getUserTranslateById(lUserInfo.getUserId()));

			mAttendeeDao.createAttendee(lAttendee);

			return Response
					.ok()
					.entity(mSessionDao.getSessionById(newSession
							.getSessionId())).build();

		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().entity(pExeception).build();
		} // catch
	} // Session createSession

	@PUT
	@Consumes("application/json")
	@Path("/{sessionId}")
	@ApiOperation(value = "Update a session", notes = "Update the session record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionById(
			@PathParam("sessionId") String pSessionId, Session pSession,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (!lUserInfo.getUserRole().equals("ADMIN")
					&& (lAttendee == null || !lAttendee.getSessionRole()
							.equals("HOST"))) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			pSession.setSessionId(pSessionId);
			
			return Response.ok().entity(mSessionDao.updateSession(pSession))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionById

	@DELETE
	@Path("/{sessionId}")
	@ApiOperation(value = "Delete a session by session id", notes = "Delete a session by session id")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionById(
			@PathParam("sessionId") String pSessionId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (!lUserInfo.getUserRole().equals("ADMIN")
					&& (lAttendee == null || !lAttendee.getSessionRole()
							.equals("HOST"))) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			mSessionDao.deleteSessionById(pSessionId);
			return Response.ok().entity("{\"status\":\"ok\"}").build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session updateSessionById
} // class SessionService
