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
 * DAOConfig class
 */

package com.qasession.controller.config;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jolbox.bonecp.BoneCPDataSource;
import com.qasession.controller.dao.AnswerDao;
import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.dao.QASessionDao;
import com.qasession.controller.dao.UserTranslateDao;
import com.qasession.controller.dao.impl.SpringAnswerDao;
import com.qasession.controller.dao.impl.SpringAttendeeDao;
import com.qasession.controller.dao.impl.SpringQuestionDao;
import com.qasession.controller.dao.impl.SpringQASessionDao;
import com.qasession.controller.dao.impl.SpringUserTranslateDao;

@Configuration
@EnableTransactionManagement
@PropertySource(value="classpath:application.properties")
public class DAOConfig
{
	// Inject PropertySource into environment instance
	@Inject
	private Environment mEnvironment;
	
	@Bean
	public AnswerDao getAnswerDao() {
		return new SpringAnswerDao();
	}  // AnswerDao getAnswerDao

	@Bean
	public AttendeeDao getAttendeeDao()
	{
		return new SpringAttendeeDao();
	}  // AttendeeDAO  getAttendeeDao

	@Bean
	public QuestionDao getQuestionDao()
	{
		return new SpringQuestionDao();
	}  // QuestionDao getQuestionDao
	
	@Bean
	public QASessionDao getQASessionDao()
	{
		return new SpringQASessionDao();
	}  // SessionDao getQASessionDao
	
	
	@Bean
	public AnswerDao getSpringAnswerDao()
	{
		return new SpringAnswerDao();
	}  // Account getUser
	
	@Bean
	public UserTranslateDao getUserTranslateDao()
	{
		return new SpringUserTranslateDao();
	}  // UserTranslate getUserTranslateDao
	
	@Bean
	public JpaVendorAdapter getJpaVendorAdapter()
	{
		JpaVendorAdapter lVendorAdapter = new OpenJpaVendorAdapter();
		
		return lVendorAdapter;
	}  // JpaVendorAdapter getJpaVendorAdapter
	
	@Bean
	public LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean()
	{
		LocalContainerEntityManagerFactoryBean lBean = new LocalContainerEntityManagerFactoryBean();
		
		lBean.setJpaVendorAdapter(getJpaVendorAdapter());
		
		lBean.setDataSource(getDataSource());
		
		lBean.setPackagesToScan(new String[] {"com.qasession.controller.dao", "com.qasession.controller.model"});
		
		lBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		
		return lBean;
	}  // LocalContainerEntityManagerFactoryBean getEntityManagerFactoryBean
	
	@Bean
	public PlatformTransactionManager getTransactionManager()
	{
		JpaTransactionManager lTransactionManager = new JpaTransactionManager();
		lTransactionManager.setEntityManagerFactory(getEntityManagerFactoryBean().getObject());
		return lTransactionManager;
	}  // 
	
	@Bean
	public DataSource getDataSource()
	{
		BoneCPDataSource lDataSource = new BoneCPDataSource();
		lDataSource.setDriverClass(mEnvironment.getProperty("jdbc.driverClassName"));
		lDataSource.setJdbcUrl(mEnvironment.getProperty("jdbc.url"));
		lDataSource.setUsername(mEnvironment.getProperty("jdbc.username"));
		lDataSource.setPassword(mEnvironment.getProperty("jdbc.password"));
		lDataSource.setPassword(mEnvironment.getProperty("jdbc.password"));
		lDataSource.setMaxConnectionsPerPartition(Integer.parseInt(mEnvironment.getProperty("jdbc.maxConnectionsPerPartition")));
		lDataSource.setMinConnectionsPerPartition(Integer.parseInt(mEnvironment.getProperty("jdbc.minConnectionsPerPartition")));

		return lDataSource;
	}  // DriverManagerDataSource getDataSource
	
	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation()
	{
		return new PersistenceExceptionTranslationPostProcessor();
	}  // PersistenceExceptionTranslationPostProcessor exceptionTranslation
}  // DAOConfig
