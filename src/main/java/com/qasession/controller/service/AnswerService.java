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
import com.qasession.controller.dao.QASessionDao;
import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.Answer;
import com.qasession.controller.model.Attendee;
import com.qasession.controller.model.QASession;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")
@Path("/qasession/{qasessionId}/question/{questionId}/answer")
public class AnswerService {
	private static Logger LOGGER = LoggerFactory.getLogger(AnswerService.class);

	@Resource(shareable = true, name = "getAttendeeDao")
	private AttendeeDao mAttendeeDao;
	@Resource(shareable = true, name = "getAnswerDao")
	private AnswerDao mAnswerDao;
	@Resource(shareable = true, name = "getQASessionDao")
	private QASessionDao mQASessionDao;
	@Resource(shareable = true, name = "getQuestionDao")
	private QuestionDao mQuestionDao;
	@Resource(shareable = true, name = "getUserTranslateDao")
	private UserTranslateDao mUserTranslateDao;
	
	@GET
	@Path("/{answerid}")
	@ApiOperation(value = "Find answer by answer ID", notes = "Returns the answer that has this question id")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getAnswer(@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId,
			@PathParam("answerId") String pAnswerId) {
		try {
            
            Answer lAnswer = mAnswerDao.getAnswerById(pAnswerId);
            
			if (lAnswer == null)
			{
				return Response
						.status(Response.Status.NOT_FOUND).build();
			}  // if
			
			return Response.ok().entity(lAnswer)
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Response getAnswer

	@PUT
	@Consumes("application/json")
	@Path("/{answerId}")
	@ApiOperation(value = "Update an answer by answer ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateAnswer(@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId,
			@PathParam("answerId") String pAnswerId, Answer pAnswer,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = mUserTranslateDao.getUserInfo(pHttpServletRequest);
			
			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

            QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);
            
            Answer lAnswer = mAnswerDao.getAnswerById(pAnswerId);
            
			if (lAnswer == null)
			{
				return Response
						.status(Response.Status.NOT_FOUND).build();
			}  // if
            
			if (lUserInfo.getUserRole().equals("ADMIN") || 
					(lAttendee != null && lQASession.getQASessionStatus().equals("OPEN") && lAttendee.getQASessionRole().equals("HOST"))) {
				
			    lAnswer.setAnswerContent(pAnswer.getAnswerContent());
			    lAnswer.setUpdatedBy(lUserInfo.getUserId());
				
				return Response.ok().entity(mAnswerDao.updateAnswerById(lAnswer))
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
	} // Response updateAnswer

	@POST
	@Consumes("application/json")
	@Path("/")
	@ApiOperation(value = "Create an answer within a qasession id and a question id", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response createAnswer(@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId, Answer pAnswer,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = mUserTranslateDao.getUserInfo(pHttpServletRequest);
			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());

            QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);
            
			if (lUserInfo.getUserRole().equals("ADMIN") || 
					(lAttendee != null && lQASession.getQASessionStatus().equals("OPEN") && lAttendee.getQASessionRole().equals("HOST"))) {
				pAnswer.setQuestionId(pQuestionId);
				
				pAnswer.setCreatedBy(lUserInfo.getUserId());
				pAnswer.setUpdatedBy(lUserInfo.getUserId());
				
				return Response.ok().entity(mAnswerDao.createAnswer(pAnswer))
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
	} // Response createAnswer

	@DELETE
	@Path("/{answerId}")
	@ApiOperation(value = "Delete an answer by answer ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Answer ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteAnswer(@PathParam("qasessionId") String pQASessionId,
			@PathParam("questionId") String pQuestionId,
			@PathParam("answerId") String pAnswerId,
			@Context HttpServletRequest pHttpServletRequest) {
		try {

			UserInfo lUserInfo = mUserTranslateDao.getUserInfo(pHttpServletRequest);
			Attendee lAttendee = mAttendeeDao.getAttendeeByQASessionIdUserId(
					pQASessionId, lUserInfo.getUserId());
            QASession lQASession = mQASessionDao.getQASessionById(pQASessionId);
			if (lUserInfo.getUserRole().equals("ADMIN") || 
					(lAttendee != null && lQASession.getQASessionStatus().equals("OPEN") && lAttendee.getQASessionRole().equals("HOST"))) {
				mAnswerDao.deleteAnswerById(pAnswerId);
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
	} // Response deleteAnswer
} // class AttendeeService
