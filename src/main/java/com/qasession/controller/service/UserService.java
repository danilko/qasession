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
 * User Service class
 */

package com.qasession.controller.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.model.UserTranslate;
import com.qasession.controller.security.UserInfo;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "User Service", description = "manage current user info")
@Produces("application/json")
@Path("/user")
public class UserService {
	private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

	@Resource(shareable = true, name = "getUserTranslateDao")
	private UserTranslateDao mUserTranslateDao;

	@GET
	@ApiOperation(value = "Get current user info", response = UserInfo.class, notes = "Return user info for this user")
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Get current user info"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getUserTranslate(
			@Context HttpServletRequest pHttpServletRequest) {
		try {
			return Response.ok()
					.entity(mUserTranslateDao.getUserInfo(pHttpServletRequest))
					.build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error performing the request", pExeception);
			return Response.serverError().build();
		} // catch
	} // Session getAllAttendeeOccurance

	@Path("/_all")
	@GET
	@ApiOperation(value = "Get current token info", response = UserTranslate.class, responseContainer = "List", notes = "Return user info for this user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Return All User Translate"),
			@ApiResponse(code = 403, message = "Not authorized") })
	public Response getAllUserTranslate() {
		try {
			return Response.ok()
					.entity(mUserTranslateDao.getAllUserTranslate()).build();
		} // try
		catch (Exception pExeception) {
			LOGGER.error("Error performing the request", pExeception);
			return Response.serverError().build();
		} // catch
	}
} // class UserTranslateService
