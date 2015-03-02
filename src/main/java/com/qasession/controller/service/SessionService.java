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

import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.model.Session;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Produces("application/json")
@Path("/session")
public class SessionService {
	@Resource(shareable=true, name="getSessionDao")
	private SessionDao mSessionDao;

	@GET
	@Path("/{sessionId}")
	@ApiOperation(value = "Find session by session ID", notes = "Return the session record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getSessionById(@PathParam("sessionId") String pSessionId) {
		try {
			Session lSession = mSessionDao.getSessionById(pSessionId);
			
			if (lSession != null)
			{
			return Response
					.ok()
					.entity(lSession).build();
			}
			else
			{
				return Response.status(404).build();
			}
			
		} // try
		catch (Exception pExeception) {
			System.out.println(pExeception);
			return Response.serverError().build();
		} // catch
	} // Session getSessionById

	@POST
	@Consumes("application/json")
	@Path("/")
	@ApiOperation(value = "Create a session", notes = "Create a session")
	@ApiResponses(value = { @ApiResponse(code = 403, message = "Not authorized") })
	public Response createSession(Session pSession) {
		try {

			System.out.println("Create SEssion");

			return Response
					.ok()
					.entity(mSessionDao.createSession(pSession)).build();
			
		} // try
		catch (Exception pExeception) {
			System.out.println("Create SEssion Error");
			System.out.println(pExeception);
			return Response.serverError().entity(pExeception).build();
		} // catch
	} // Session createSession

	@PUT
	@Consumes("application/json")
	@Path("/{sessionId}")
	@ApiOperation(value = "Update a session", notes = "Update the session record that this session belong")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response deleteSessionById(
			@PathParam("sessionId") String pSessionId, Session pSession) {
		try {
			return Response
					.ok()
					.entity(mSessionDao.updateSession(pSession)).build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session deleteSessionById

	@DELETE
	@Path("/{sessionId}")
	@ApiOperation(value = "Delete a session by session id", notes = "Delete a session by session id")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Session ID not found"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response updateSessionById(@PathParam("sessionId") String pSessionId) {
		try {
			mSessionDao.deleteSessionById(pSessionId);
			return Response.ok().entity("{\"status\":\"ok\"}").build();
		} // try
		catch (Exception pExeception) {
			return Response.serverError().build();
		} // catch
	} // Session updateSessionById
} // class SessionService
