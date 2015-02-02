package com.qasession.controller.service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Produces("application/json")
@Path("/session/{sessionId}/question/{questionId}/answer")
public class TokenService {
	
	@GET
	@Path("/token")
	@ApiOperation(value = "Find attendee by attendee email", notes = "Returns all attendee record that this session belong")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Attendee ID not found"), @ApiResponse(code = 403, message = "Not authorized") })
	public Response getCurrentUserInfo(@Context HttpServletRequest pHttpServletRequest ) {
		try {
			return Response
					.ok()
					.entity(pHttpServletRequest.getSession().getAttribute("qa_session_token_info")).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance
}
