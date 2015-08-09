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
 * MainWebAppplicationInitializer class
 */

package com.qasession.controller.config;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class MainWebAppplicationInitializer implements
		WebApplicationInitializer {
	
	public void onStartup(ServletContext pServletContext) throws ServletException 
	{
		// Apply Spring OAuthSecurity to both forward and request dispatcher
		FilterRegistration.Dynamic lFilter = pServletContext.addFilter("securityFilter", "com.qasession.controller.security.SecurityFilter");
		lFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
		
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext lRootContext = new AnnotationConfigWebApplicationContext();
		//lRootContext.scan("com.qasession.controller.config");
		lRootContext.scan("com.qasession.controller.config");
		
		// Manage the lifecycle of the root application context
		pServletContext.addListener(new ContextLoaderListener(lRootContext));
		
		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic lCFXDispatcher = pServletContext.addServlet(
				"CFXServlet", CXFServlet.class);
		lCFXDispatcher.addMapping("/rest/*");
	}
} // class MainWebApplicationInitializer