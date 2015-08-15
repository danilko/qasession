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
 * Attendee Service class
 */

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
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Api(value = "Attendee Service", description = "manage attendees")
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
	@ApiOperation(value = "Update an attendee by attendee ID within a session ID", response = Attendee.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Update an attendee by attendee ID operation success"),
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionAttendee(
			@PathParam("qasessionId") String pQASessionId,
			@PathParam("userId") String pUserId, Attendee pAttendee,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

			Attendee lTargetAttendee = mAttendeeDao
					.getAttendeeByQASessionIdUserId(pQASessionId, pUserId);

			QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);

			if (lUserInfo.getUserRole().equals("ADMIN")
					|| (lAttendee != null
							&& lQASession.getQASessionStatus()
									.equalsIgnoreCase("OPEN") && lAttendee
							.getQASessionRole().equals("HOST"))) {

				if (!lQASession.getCreatedBy().equalsIgnoreCase(pUserId)) {
					lTargetAttendee.setQASessionRole(pAttendee
							.getQASessionRole());

					if(pAttendee.getQASessionRole().equalsIgnoreCase("HOST"))
					{
						pAttendee.setQASessionRole("HOST");
					}  // if
					else
					{
						pAttendee.setQASessionRole("ATTENDEE");
					}  // else
					
					return Response.ok()
							.entity(mAttendeeDao.updateAttende(pAttendee))
							.build();
				} else {
					Response.status(Response.Status.FORBIDDEN)
							.entity("{\"status\":\"forbidden\", \"message\":\"Original Host of the session cannot be modified/deleted\"}")
							.build();
				} // else
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
	@ApiOperation(value = "Create an attendee within a session ID", response = Attendee.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Create an attendee within a session ID operation success"),
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

			if (lUserInfo.getUserRole().equals("ADMIN")
					|| (lAttendee != null
							&& lQASession.getQASessionStatus()
									.equalsIgnoreCase("OPEN") && lAttendee
							.getQASessionRole().equals("HOST"))) {

				if (!lQASession.getCreatedBy().equalsIgnoreCase(pAttendee.getUserId()) && mAttendeeDao.getAttendeeByQASessionIdUserId(pQASessionId,
						pAttendee.getUserId()) == null) {
					pAttendee.setQASessionId(pQASessionId);
					
					if(pAttendee.getQASessionRole().equalsIgnoreCase("HOST"))
					{
						pAttendee.setQASessionRole("HOST");
					}  // if
					else
					{
						pAttendee.setQASessionRole("ATTENDEE");
					}  // else
					
					return Response.ok()
							.entity(mAttendeeDao.createAttendee(pAttendee))
							.build();
				} // if
				else {
					Response.status(Response.Status.FORBIDDEN)
							.entity("{\"status\":\"forbidden\", \"message\":\"Original Host of the session cannot be modified/deleted\"}")
							.build();
				} // else
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
	@ApiOperation(value = "Delete an attendee by attendee Id", response = Attendee.class)
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Delete an attendee by attendee Id operation success"),
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

			if (lUserInfo.getUserRole().equals("ADMIN")
					|| (lAttendee != null
							&& lQASession.getQASessionStatus()
									.equalsIgnoreCase("OPEN") && lAttendee
							.getQASessionRole().equals("HOST"))) {
				if (!lQASession.getCreatedBy().equalsIgnoreCase(pUserId)) {
					mAttendeeDao.deleteAttendeeByQASessionIdUserId(
							pQASessionId, pUserId);

					return Response.ok().entity("{\"status\":\"success\"}")
							.build();
				} // if
				else {
					Response.status(Response.Status.FORBIDDEN)
							.entity("{\"status\":\"forbidden\", \"message\":\"Original Host of the session cannot be modified/deleted\"}")
							.build();
				} // else

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
