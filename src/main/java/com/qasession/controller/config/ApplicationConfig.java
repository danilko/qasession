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
 * ApplicationConfig class
 */

package com.qasession.controller.config;

import java.util.Arrays;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.qasession.controller.service.AnswerService;
import com.qasession.controller.service.AttendeeService;
import com.qasession.controller.service.QuestionService;
import com.qasession.controller.service.QASessionService;
import com.qasession.controller.service.UserService;
import com.wordnik.swagger.jaxrs.config.BeanConfig;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;

@Configuration
public class ApplicationConfig {
	@Bean(destroyMethod = "shutdown")
	

	public SpringBus cxf()
	{
		return new SpringBus();
	}  // public SpringBus cxf
	
    @Bean
    public JacksonJsonProvider getJSONProvider() {
        return new JacksonJsonProvider();
    }  // public JacksonJsonProvider getJSONProvider()
    
	@Bean
	public AnswerService getAnswerService()
	{
		return new AnswerService();
	}  // public AnswerService getAnswerService()
    
    
	@Bean
	public QuestionService getQuestionService()
	{
		return new QuestionService();
	}  // public QuestionService getQuestionService()
    
	
	@Bean
	public AttendeeService getAttendeeService()
	{
		return new AttendeeService();
	}  // public AttendeeService getAttendeeService()
	
	@Bean
	public QASessionService getSessionService()
	{
		return new QASessionService();
	}  // public SessionService getSessionService()

	@Bean
	public UserService getUserService()
	{
		return new UserService();
	}  // public UserService getUserService()

	@Bean
	public BeanConfig getSwaggerService()
	{
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("/controller/rest");
        beanConfig.setResourcePackage("com.qasession.controller.service");
        beanConfig.setScan(true);
        
        return beanConfig;
	}  // public UserService getUserService()

	@Bean
	public ApiDeclarationProvider getApiDeclarationProivder()
	{
		return new ApiDeclarationProvider();
	}  // public ApiDeclarationProvider getApiDeclarationProivder
	
	@Bean
	public ApiListingResourceJSON getApiListingResourceJSON()
	{
		return new ApiListingResourceJSON();
	}  // public ApiListResoureJSON getApiListingResourceJSON
	
	@Bean
	public ResourceListingProvider getResourceListingProvider()
	{
		return new ResourceListingProvider();
	}  // public ApiListResoureJSON getApiListResoureJSON
	
	@Bean
	public Server initJAXRSServer() {
		JAXRSServerFactoryBean lFactory = RuntimeDelegate.getInstance().createEndpoint(new Application(),
				JAXRSServerFactoryBean.class);
		lFactory.setServiceBeans(Arrays.<Object>asList(getSessionService(), getAnswerService(), getQuestionService(), getAttendeeService(), getUserService(), getApiListingResourceJSON()));
		
		lFactory.setAddress(lFactory.getAddress());
	
		lFactory.setProviders(Arrays.<Object>asList(getJSONProvider(),  getResourceListingProvider(), getApiDeclarationProivder() ));
		
		return lFactory.create();
	}  // Server initJAXRSServer
}
