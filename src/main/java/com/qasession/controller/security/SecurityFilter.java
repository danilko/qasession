package com.qasession.controller.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SecurityFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest pServletRequest,
			ServletResponse pServletResponse, FilterChain pFilterChain)
			throws IOException, ServletException {
		
		boolean lAuthorize = false;
		
		HttpServletRequest lRequest = (HttpServletRequest) pServletRequest;
		HttpServletResponse lResponse = (HttpServletResponse) pServletResponse;

		String lAuthCode = lRequest.getParameter("code");
		
		if(StringUtils.isNotBlank(lAuthCode))
		{
			try
			{
				String lAuthResponse = readURI(FacebookClient.getAuthURI(lAuthCode));
				
				ObjectMapper lMapper = new ObjectMapper();
				JsonNode lObject = lMapper.readTree(lAuthResponse);
				
				String lAccessToken = lObject.get("access_token").asText();
				String lExpireIn = lObject.get("expires_in").asText();
				//String lMachineID = lObject.get("machine_id");
				
				if(StringUtils.isNotBlank(lAccessToken) && StringUtils.isNotBlank(lExpireIn))
				{
					HttpSession lSession = lRequest.getSession();
					
					OAuthToken lToken = new OAuthToken();
					lToken.setAccessToken(lAccessToken);
					lToken.setExpiresIn(lExpireIn);
					lToken.setMachineId("");
					
					lSession.setAttribute("qa_session_token_info", lToken);
				}  // if
			}  // try
			catch(Exception pException)
			{
				throw new RuntimeException("Invalid Access Token");
			}
		}  //if
		
		if(!lAuthorize)
		{
			lResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		
		pFilterChain.doFilter(lRequest, lResponse);
	}

	private String readURI(String pURI) throws IOException
	{
		ByteArrayOutputStream lOutputStream = new ByteArrayOutputStream();
		InputStream lInputStream = (new URL(pURI)).openStream();
		
		int lByte;
		
		while((lByte = lInputStream.read()) != -1)
		{
			lOutputStream.write(lByte);
		}  // while
		
		return new String(lOutputStream.toByteArray());
	}
	
	public void init(FilterConfig arg0) throws ServletException {

	}

}
