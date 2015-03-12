package com.qasession.controller.service;

import javax.annotation.Resource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.model.Question;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")
@Path("/session/{sessionId}/question")
public class QuestionService {
	private static Logger LOGGER = LoggerFactory.getLogger(QuestionService.class);
	
	@Resource(shareable=true, name="getQuestionDao")
	private QuestionDao mQuestionDao;
	@Resource(shareable=true, name="getSessionDao")
	private SessionDao mSessionDao;
	@GET
	@Path("/")
	@ApiOperation(value = "Find question by question ID", notes = "Returns all question record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getAllQuestion(@PathParam("sessionId") String pSessionId,
			@PathParam("attendeeId") String pAttendeeId) {
		try {
			return Response
					.ok()
					.entity(mQuestionDao.getQuestionsByKeyValue("session_id",
							pSessionId)).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance

	@GET
	@Path("/{questionId}")
	@ApiOperation(value = "Find question by question ID", notes = "Returns all question record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getQuestionById(@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId) {
		try {
			return Response
					.ok()
					.entity(mQuestionDao.getQuestionById(pQuestionId)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance

	@PUT
	@Consumes("application/json")
	@Path("/{questionId}")
	@ApiOperation(value = "Update an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Attendee ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId, Question pQuestion) {
		try {
			pQuestion.setSession(mSessionDao.getSessionById(pSessionId));
			return Response
					.ok()
					.entity(mQuestionDao.createQuestion(pQuestion)).build();
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
	public Response createSessionAttendee(
			@PathParam("sessionId") String pSessionId,
		    Question pQuestion) {
		try {
			pQuestion.setSession(mSessionDao.getSessionById(pSessionId));
			return Response
					.ok()
					.entity(mQuestionDao.createQuestion(pQuestion)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session createSessionAttendee

	@DELETE
	@Path("/{questionId}")
	@ApiOperation(value = "Delete an question by question ID within a session ID", notes = "")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Question ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			@PathParam("questionId") String pQuestionId) {
		try {
			mQuestionDao.deleteQuestionById(pQuestionId);
			return Response
					.ok()
					.entity("{\"status\":\"success\"}").build();
		} // try
		catch (Exception pExeception) {

			LOGGER.error(pExeception.toString());
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionAttendee
} // class AttendeeService
