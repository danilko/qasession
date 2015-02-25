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
import com.qasession.controller.service.SessionService;

@Configuration
public class ApplicationConfig {
	@Bean(destroyMethod = "shutdown")
	

	public SpringBus cxf()
	{
		return new SpringBus();
	}  // SpringBus cxf
	
    @Bean
    public JacksonJsonProvider getJSONProvider() {
        return new JacksonJsonProvider();
    }
    
	@Bean
	public AnswerService getAnswerService()
	{
		return new AnswerService();
	}  // public
    
    
	@Bean
	public QuestionService getQuestionService()
	{
		return new QuestionService();
	}  // public
    
	
	@Bean
	public AttendeeService getAttendeeService()
	{
		return new AttendeeService();
	}  // public
	
	@Bean
	public SessionService getSessionService()
	{
		return new SessionService();
	}  // public

	
	@Bean
	public Server initJAXRSServer() {
		JAXRSServerFactoryBean lFactory = RuntimeDelegate.getInstance().createEndpoint(new Application(),
				JAXRSServerFactoryBean.class);
		lFactory.setServiceBeans(Arrays.<Object>asList(getSessionService(), getAnswerService(), getQuestionService(), getAttendeeService()));
		
		lFactory.setAddress(lFactory.getAddress());
	
		lFactory.setProviders(Arrays.<Object>asList(getJSONProvider()));
		
		return lFactory.create();
	}  // Server initJAXRSServer
}
