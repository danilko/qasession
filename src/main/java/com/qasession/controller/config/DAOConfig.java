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

import com.jolbox.bonecp.BoneCPDataSource;
import com.qasession.controller.dao.AnswerDao;
import com.qasession.controller.dao.AttendeeDao;
import com.qasession.controller.dao.QuestionDao;
import com.qasession.controller.dao.SessionDao;
import com.qasession.controller.dao.impl.SpringAnswerDao;
import com.qasession.controller.dao.impl.SpringAttendeeDao;
import com.qasession.controller.dao.impl.SpringQuestionDao;
import com.qasession.controller.dao.impl.SpringSessionDao;

@Configuration
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
	public SessionDao getSessionDao()
	{
		return new SpringSessionDao();
	}  // SessionDao getSessionDao
	
	
	@Bean
	public AnswerDao getSpringAnswerDAO()
	{
		return new SpringAnswerDao();
	}  // Account getUser
	
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
		
		lBean.setPackagesToScan(new String[] {"com.qasession.controller.dao.impl", "com.qasession.controller.model"});
		
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
