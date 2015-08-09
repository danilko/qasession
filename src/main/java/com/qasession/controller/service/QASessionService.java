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
 * QASession Service class
 */

package com.qasession.controller.service;

import java.util.Calendar;
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
import com.qasession.controller.dao.QASessionDao;
import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.model.QASession;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Produces("application/json")
@Path("/qasession")
public class QASessionService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(QASessionService.class);

	@Resource(shareable = true, name = "getQASessionDao")
	private QASessionDao mQASessionDao;

	@Resource(shareable = true, name = "getUserTranslateDao")
	private UserTranslateDao mUserTranslateDao;

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;

	@GET
	@Path("/")
	@ApiOperation(value = "Get all qasessions belong to current user", response = QASession.class, responseContainer = "List", notes = "Return the session record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "List of qasessions belong to the user"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getSession(@Context HttpServletRequest pHttpServletRequest) {
		try {
			UserInfo lUserInfo = mUserTranslateDao
					.getUserInfo(pHttpServletRequest);

			List<QASession> lQASession = mQASessionDao
					.getQASessionByUserId(lUserInfo.getUserId());

			if (lQASession != null) {
				return Response.ok().entity(lQASession).build();
			} else {
				return Response.status(404).build();
			}

		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error in response", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session getSessionById

	@GET
	@Path("/{qasessionId}")
	@ApiOperation(value = "Find session by session ID", response = QASession.class, notes = "Return the session record that this session belong")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Session find and return the session object"),
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getSessionById(@PathParam("qasessionId") String pQASessionId) {
		try {
			QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);

			if (lQASession != null) {
				return Response.ok().entity(lQASession).build();
			} else {
				return Response.status(Response.Status.NOT_FOUND).build();
			}

		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error in response", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session getSessionById

	@POST
	@Consumes("application/json")
	@Path("/")
	@ApiOperation(value = "Create a session", response = QASession.class,  notes = "Create a session")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operation Success"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response createSession(QASession pSession,
			@Context HttpServletRequest pHttpServletRequest) {
		try {
			UserInfo lUserInfo = mUserTranslateDao
					.getUserInfo(pHttpServletRequest);

			pSession.setCreatedBy(lUserInfo.getUserId());
			pSession.setUpdatedBy(lUserInfo.getUserId());
			
			QASession newSession = mQASessionDao.createQASession(pSession,
					lUserInfo.getUserId());

			
			Attendee lAttendee = new Attendee();
			lAttendee.setQASessionId(newSession.getQASessionId());
			lAttendee.setUpdateTimestamp(Calendar.getInstance());
			lAttendee.setQASessionRole("HOST");
			lAttendee.setUserId(lUserInfo.getUserId());

			mAttendeeDao.createAttendee(lAttendee);

			return Response
					.ok()
					.entity(mQASessionDao.getQASessionById(newSession
							.getQASessionId())).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error in response", pExeception);
			return Response.serverError().entity(pExeception).build();
		} // catch
	} // Session createSession

	@PUT
	@Consumes("application/json")
	@Path("/{qasessionId}")
	@ApiOperation(value = "Update a session", response = QASession.class, notes = "Update the session record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operation Success"),
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionById(
			@PathParam("qasessionId") String pQASessionId,
			QASession pQASession,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = mUserTranslateDao
					.getUserInfo(pHttpServletRequest);

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

			QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);

			if (lQASession == null) {
				return Response.status(Response.Status.NOT_FOUND).build();
			}

			if (lUserInfo.getUserRole().equals("ADMIN")
					|| (lAttendee != null && lAttendee.getQASessionRole()
							.equals("HOST"))) {

				lQASession.setAttendees(pQASession.getAttendees());
				lQASession.setUpdatedBy(lUserInfo.getUserId());
				lQASession.setQASessionDescription(pQASession
						.getQASessionDescription());
				lQASession.setQASessionMaxQuestion(pQASession
						.getQASessionMaxQuestion());
				lQASession.setQASessionStatus(pQASession.getQASessionStatus());
				lQASession.setQASessionTopic(pQASession.getQASessionTopic());

				return Response.ok()
						.entity(mQASessionDao.updateQASession(lQASession))
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
	} // Session deleteSessionById

	@DELETE
	@Path("/{qasessionId}")
	@ApiOperation(value = "Delete a session by session ID", notes = "Delete a session by session id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Delete a session by session ID operation success"),
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionById(
			@PathParam("qasessionId") String pQASessionId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {
			UserInfo lUserInfo = mUserTranslateDao
					.getUserInfo(pHttpServletRequest);

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

			if (lUserInfo.getUserRole().equals("ADMIN")
					|| (lAttendee != null && lAttendee.getQASessionRole()
							.equals("HOST"))) {
				mQASessionDao.deleteQASessionById(pQASessionId);
				return Response.ok().entity("{\"status\":\"ok\"}").build();
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
	} // Session updateSessionById
} // class SessionService
