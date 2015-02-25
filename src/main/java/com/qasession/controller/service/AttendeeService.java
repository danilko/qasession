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

import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.model.Attendee;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;

@Produces("application/json")

public class AttendeeService {
	@Resource(shareable=true, name="getAttendeeDao")
	private AttendeeDao mAttendeeDao;
	
	@GET
	@Path("/attendee/{attendeeEmail}")
	@ApiOperation(value = "Find attendee by attendee email", notes = "Returns all attendee record that this session belong")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response getAllAttendeeOccurance(
			@PathParam("attendeeEmail") String pAttendeeEmail) {
		try {
			return Response
					.ok()
					.entity(mAttendeeDao.getAttendeeByKeyValue("attendee_email", pAttendeeEmail)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance

	@GET
	@Path("/session/{sessionId}/attendee/{attendeeEmail}")
	@ApiOperation(value = "Find attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response getSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			@PathParam("attendeeEmail") String pAttendeeEmail) {
		try {
			return Response
					.ok()
					.entity(mAttendeeDao.getAttendeeBySessionIdAttendeeEmail(pSessionId, pAttendeeEmail)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session getSessionAttendee

	@PUT
	@Consumes("application/json")
	@Path("/session/{sessionId}/attendee")
	@ApiOperation(value = "Update an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			Attendee pAttendee) {
		try {
			pAttendee.setSessionId(pSessionId);
			
			return Response
					.ok()
					.entity(mAttendeeDao.updateAttende(pAttendee)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session updateSessionAttendee

	@POST
	@Consumes("application/json")
	@Path("/session/{sessionId}/attendee/")
	@ApiOperation(value = "Create an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response createSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			Attendee pAttendee) {
		try {
			pAttendee.setSessionId(pSessionId);
			
			return Response
					.ok()
					.entity(mAttendeeDao.createAttendee(pAttendee)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session createSessionAttendee

	@DELETE
	@Path("/session/{sessionId}/attendee/{attendeeEmail}")
	@ApiOperation(value = "Delete an attendee by attendee ID within a session ID", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionAttendee(
			@PathParam("sessionId") String pSessionId,
			@PathParam("attendeeEmail") String pAttendeeEmail) {
		try {
			mAttendeeDao.deleteAttendeeBySessionIdAttendeeEmail(pSessionId, pAttendeeEmail);
			return Response
					.ok()
					.entity("{\"status\":\"success\"}").build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionAttendee
} // class AttendeeService
