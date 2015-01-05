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
import org.springframework.web.filter.DelegatingFilterProxy;


public class MainWebAppplicationInitializer implements
		WebApplicationInitializer {
	
	public void onStartup(ServletContext pServletContext) throws ServletException 
	{
		// Apply Spring OAuthSecurity to both forward and request dispatcher
		FilterRegistration.Dynamic lFilter = pServletContext.addFilter("securityFilter",
				new DelegatingFilterProxy("springSecurityFilterChain"));
		lFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD), true, "/*");
		
		// Create the 'root' Spring application context
		AnnotationConfigWebApplicationContext lRootContext = new AnnotationConfigWebApplicationContext();
		//lRootContext.scan("com.qasession.controller.config");
		lRootContext.scan("com.qasession.controller.config");
		
		// Manage the lifecycle of the root application context
		pServletContext.addListener(new ContextLoaderListener(lRootContext));
		
		// Register and map the dispatcher servlet
		ServletRegistration.Dynamic lDispatcher = pServletContext.addServlet(
				"CFXServlet", CXFServlet.class);
		lDispatcher.addMapping("/rest/*");
		

		
		//registerProxyFilter(pServletContext, "springSecurityFilterChain", "/*");
		//registerProxyFilter(pServletContext, "oauth2ClientContextFilter", "/*");
	}
} // class MainWebApplicationInitializer