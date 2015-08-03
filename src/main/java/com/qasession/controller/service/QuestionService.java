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

import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.dao.QASessionDao;
import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.model.Question;
import com.qasession.controller.model.QASession;
import com.qasession.controller.security.FacebookClient;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")
@Path("/qasession/{qasessionId}/question")
public class QuestionService {
	private static Logger LOGGER = LoggerFactory
			.getLogger(QuestionService.class);

	@Resource(shareable = true, name = "getUserTranslateDao")
	private UserTranslateDao mUserTranslateDao;

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;

	@Resource(shareable = true, name = "getQuestionDao")
	private QuestionDao mQuestionDao;
	
	@Resource(shareable = true, name = "getQASessionDao")
	private QASessionDao mQASessionDao;
	@GET
	@Path("/{questionId}")
	@ApiOperation(value = "Find question by question ID", notes = "Returns all question record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getQuestionById(@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId) {
		try {

			return Response.ok()
					.entity(mQuestionDao.getQuestionById(pQuestionId)).build();
		} // try
		catch (Exception pExeception) {

			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance

	@PUT
	@Consumes("application/json")
	@Path("/{questionId}")
	@ApiOperation(value = "Update an question by question ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionQuestion(
			@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId, Question pQuestion,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());
			
			Question lQuestion = mQuestionDao.getQuestionById(pQuestionId);

			if (lQuestion == null)
			{
				return Response
						.status(Response.Status.NOT_FOUND).build();
			}  // if
			
			if (lUserInfo.getUserRole().equals("ADMIN") ||
				(lQuestion.getCreatedBy().equals(lUserInfo.getUserId()) == false
							|| lAttendee == null || !lAttendee.getQASessionRole()
							.equals("HOST") || ! mQASessionDao.getQASessionById(pQASessionId).equals("OPEN"))) {

				lQuestion.setQuestionStatus(pQuestion.getQuestionStatus());
				lQuestion.setQuestionContent(pQuestion.getQuestionContent());
				lQuestion.setUpdatedBy(lUserInfo.getUserId());
				
				
				return Response.ok().entity(mQuestionDao.updateQuestion(lQuestion))
						.build();
			} // if
			return Response
					.status(Response.Status.FORBIDDEN)
					.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
					.build();
			

		} // try
		catch (Exception pExeception) {

			LOGGER.error(pExeception.toString());
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
	public Response createSessionqQuestion(
			@PathParam("qasessionId") String pQASessionId, Question pQuestion,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());

			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

			QASession lSession = mQASessionDao.getQASessionById(pQASessionId);
			
			if (!lUserInfo.getUserRole().equals("ADMIN") &&
					(lAttendee == null || !lSession.getQASessionStatus().equals("OPEN"))) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
						.build();
			} // if

			if ((mQuestionDao.getQuestionsBySessionIdUserId(pQASessionId,
					lUserInfo.getUserId()).size() >= lSession
					.getQASessionMaxQuestion()) || !lAttendee.getQASessionRole().equalsIgnoreCase("HOST")) {
				return Response
						.status(Response.Status.FORBIDDEN)
						.entity("{\"status\":\"forbidden\", \"message\":\"user exceeds allowed questions in the session\"}")
						.build();
			} // if

			pQuestion.setCreatedBy(lUserInfo.getUserId());
			pQuestion.setUpdatedBy(lUserInfo.getUserId());
			pQuestion.setQASessionId(lSession.getQASessionId());

			return Response.ok().entity(mQuestionDao.createQuestion(pQuestion))
					.build();
		} // try
		catch (Exception pExeception) {

			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session createSessionAttendee

	@DELETE
	@Path("/{questionId}")
	@ApiOperation(value = "Delete an question by question ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Question ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionQuestion(
			@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = (UserInfo) pHttpServletRequest.getSession()
					.getAttribute(FacebookClient.getUserInfoSessionId());
			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

			QASession lSession = mQASessionDao.getQASessionById(pQASessionId);
			
			Question lQuestion = mQuestionDao.getQuestionById(pQuestionId);
			
			
			if (!lUserInfo.getUserRole().equals("ADMIN") || (
							lAttendee != null && lSession.getQASessionStatus().equals("OPEN") && (lAttendee.getQASessionRole()
							.equals("HOST") || lQuestion.getCreatedBy().equals(lAttendee.getUserId())))) {
				mQuestionDao.deleteQuestionById(pQuestionId);

				return Response.ok().entity("{\"status\":\"success\"}").build();

			} // if
		
			return Response
					.status(Response.Status.FORBIDDEN)
					.entity("{\"status\":\"forbidden\", \"message\":\"user does not have the previliege to perform the particular action\"}")
					.build();

		} // try
		catch (Exception pExeception) {

			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionAttendee
} // class AttendeeService
