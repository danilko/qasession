package com.qasession.controller.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
import com.qasession.controller.dao.QASessionDao;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.model.QASession;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")
@Path("/qasession/{qasessionId}/attendee")
public class AttendeeService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(AttendeeService.class);

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;

	@Resource(shareable = true, name = "getQASessionDao")
	private QASessionDao mQASessionDao;
	
	@PUT
	@Consumes("application/json")
	@Path("/{userId}")
	@ApiOperation(value = "Update an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionAttendee(
			@PathParam("qasessionId") String pQASessionId, @PathParam("userId") String pUserId, Attendee pAttendee,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

			Attendee lTargetAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(pQASessionId, pUserId);
			
            QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);
			
			if (lUserInfo.getUserRole().equals("ADMIN") || 
					(lAttendee != null && lQASession.getQASessionStatus().equalsIgnoreCase("OPEN") && lAttendee.getQASessionRole().equals("HOST"))) {
				
				lTargetAttendee.setQASessionRole(pAttendee.getQASessionRole());
				
				return Response.ok().entity(mAttendeeDao.updateAttende(pAttendee))
						.build();
			} // if
			
			return Response
					.status(Response.Status.FORBIDDEN)
					.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
					.build();

		} // try
		catch (Exception pExeception) {
            LOGGER.error("Error in response", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session updateSessionAttendee

	@POST
	@Consumes("application/json")
	@Path("/")
	@ApiOperation(value = "Create an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response createSessionAttendee(
			@PathParam("qasessionId") String pQASessionId, Attendee pAttendee,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

            QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);
			
			if (lUserInfo.getUserRole().equals("ADMIN") || 
					(lAttendee != null && lQASession.getQASessionStatus().equalsIgnoreCase("OPEN") && lAttendee.getQASessionRole().equals("HOST"))) {
				pAttendee.setQASessionId(pQASessionId);

				return Response.ok().entity(mAttendeeDao.createAttendee(pAttendee))
						.build();
			} // if
			return Response
					.status(Response.Status.FORBIDDEN)
					.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
					.build();

		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error in response", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session createSessionAttendee

	@DELETE
	@Path("/{userId}")
	@ApiOperation(value = "Delete an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionAttendee(
			@PathParam("qasessionId") String pQASessionId,
			@PathParam("userId") String pUserId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

            QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);
			
			if (lUserInfo.getUserRole().equals("ADMIN") || 
					(lAttendee != null && lQASession.getQASessionStatus().equalsIgnoreCase("OPEN") && lAttendee.getQASessionRole().equals("HOST"))) {
				mAttendeeDao.deleteAttendeeByQASessionIdUserId(pQASessionId,
						pUserId);

				return Response.ok().entity("{\"status\":\"success\"}").build();

			} // if
			
			return Response
					.status(Response.Status.FORBIDDEN)
					.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error in response", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionAttendee
} // class AttendeeService
