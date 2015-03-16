package com.qasession.controller.service;

import java.util.List;

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
import com.qasession.controller.model.Attendee;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")
public class AttendeeService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(AttendeeService.class);

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;

	@Resource(shareable = true, name = "getSessionDao")
	private SessionDao mSessionDao;

	@GET
	@Path("/attendee/{userId}")
	@ApiOperation(value = "Find attendee by user id", notes = "Returns all attendee record that this user id belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getAllAttendeeOccurance(@PathParam("userId") String pUserId) {
		try {
			List<Attendee> lAttendees = mAttendeeDao.getAttendeeByKeyValue(
					"userTranslate.userId", pUserId);

			if (lAttendees.size() > 0) {
				LOGGER.debug(lAttendees.get(0).getSession().getSessionId());
				LOGGER.debug(lAttendees.get(0).getSession().getSessionStatus());
				LOGGER.debug(lAttendees.get(0).getSession().getSessionTopic());
				LOGGER.debug(lAttendees.get(0).getSession()
						.getSessionDescription());
			} // if

			return Response.ok().entity(lAttendees).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance

	@PUT
	@Consumes("application/json")
	@Path("/session/{sessionId}/attendee")
	@ApiOperation(value = "Update an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionAttendee(
			@PathParam("sessionId") String pSessionId, Attendee pAttendee,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (lUserInfo.getUserRole().equals("ADMIN")
					&& (lAttendee == null || !lAttendee.getSessionRole()
							.equals("HOST"))) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			pAttendee.setSession(mSessionDao.getSessionById(pSessionId));

			return Response.ok().entity(mAttendeeDao.updateAttende(pAttendee))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session updateSessionAttendee

	@POST
	@Consumes("application/json")
	@Path("/session/{sessionId}/attendee/")
	@ApiOperation(value = "Create an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response createSessionAttendee(
			@PathParam("sessionId") String pSessionId, Attendee pAttendee,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (lUserInfo.getUserRole().equals("ADMIN")
					&& (lAttendee == null || !lAttendee.getSessionRole()
							.equals("HOST"))) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			pAttendee.setSession(mSessionDao.getSessionById(pSessionId));

			return Response.ok().entity(mAttendeeDao.createAttendee(pAttendee))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session createSessionAttendee

	@DELETE
	@Path("/session/{sessionId}/attendee/{userId}")
	@ApiOperation(value = "Delete an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			@PathParam("userId") String pUserId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (lUserInfo.getUserRole().equals("ADMIN")
					&& (lAttendee == null || !lAttendee.getSessionRole()
							.equals("HOST"))) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(pSessionId,
					pUserId);
			mAttendeeDao.deleteAttendeeByAttendeeId(lAttendee.getAttendeeId());

			return Response.ok().entity("{\"status\":\"success\"}").build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionAttendee
} // class AttendeeService
