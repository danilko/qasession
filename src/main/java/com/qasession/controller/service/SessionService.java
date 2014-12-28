package com.qasession.controller.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.qasession.controller.model.Session;


@Produces("application/json")
@Path("/session")
public class SessionService 
{
	@GET
	@Path("/{sessionId}")
	public Response getSessionById(@PathParam("sessionId") String pSessionId){
		
		return Response.ok().entity(new Session()).build();
	}  // Session getSessionById
	
	@POST
	@Consumes("application/json")
	@Path("/")
	public Response createSession(Session pSession) {
		return Response.ok().entity(new Session()).build();
	}  // Session createSession
	
	@PUT
	@Consumes("application/json")
	@Path("/{sessionId}")
	public Response deleteSessionById(@PathParam("sessionId") String pSessionId, Session pSession)
	{
		return Response.ok().entity(new Session()).build();
	}  // Session deleteSessionById
	
	@DELETE
	@Consumes("application/json")
	@Path("/{sessionId}")
	public Response updateSessionById(@PathParam("sessionId") String pSessionId) 
	{
		return Response.ok().entity("{\"status\":\"ok\"}").build();
	}  // Session updateSessionById
}  //  class SessionService 
