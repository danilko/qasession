package com.qasession.controller.service;

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

import com.qasession.controller.dao.AnswerDao;
import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.model.Answer;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")
@Path("/session/{sessionId}/question/{questionId}/answer")
public class AnswerService {
	private static Logger LOGGER = LoggerFactory.getLogger(AnswerService.class);

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;
	@Resource(shareable = true, name = "getAnswerDao")
	private AnswerDao mAnswerDao;
	@Resource(shareable = true, name = "getQuestionDao")
	private QuestionDao mQuestionDao;

	@GET
	@Path("/{answerid}")
	@ApiOperation(value = "Find answer by answer ID", notes = "Returns the answer that has this question id")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getAnswer(@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId,
			@PathParam("answerId") String pAnswerId) {
		try {
			return Response.ok().entity(mAnswerDao.getAnswerById(pAnswerId))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Response getAnswer

	@PUT
	@Consumes("application/json")
	@Path("/{answerid}")
	@ApiOperation(value = "Update an answer by answer ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateAnswer(@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId,
			@PathParam("answerId") String pAnswerId, Answer pAnswer,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (lUserInfo.getUserRole().equals("ADMIN") || lAttendee == null
					|| !lAttendee.getSessionRole().equals("HOST")) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			pAnswer.setAnswerId(pAnswerId);
			pAnswer.setQuestion(mQuestionDao.getQuestionById(pQuestionId));
			mAnswerDao.updateAnswerById(pAnswer);
			return Response.ok().entity(mAnswerDao.createAnswer(pAnswer))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Response updateAnswer

	@POST
	@Consumes("application/json")
	@Path("/")
	@ApiOperation(value = "Create an answer within a session id and a question id", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response createAnswer(@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId, Answer pAnswer,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (lAttendee == null || !lAttendee.getSessionRole().equals("HOST")) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			pAnswer.setQuestion(mQuestionDao.getQuestionById(pQuestionId));
			return Response.ok().entity(mAnswerDao.createAnswer(pAnswer))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Response createAnswer

	@DELETE
	@Path("/{answerid}")
	@ApiOperation(value = "Delete an answer by answer ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Answer ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteAnswer(@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId,
			@PathParam("answerId") String pAnswerId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeBySessionIdUserId(
					pSessionId, lUserInfo.getUserId());

			if (lUserInfo.getUserRole().equals("ADMIN") || lAttendee == null
					|| !lAttendee.getSessionRole().equals("HOST")) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			mAnswerDao.deleteAnswerById(pQuestionId);
			return Response.ok().entity("{\"status\":\"success\"}").build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Response deleteAnswer
} // class AttendeeService
