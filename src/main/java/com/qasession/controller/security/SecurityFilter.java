package com.qasession.controller.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SecurityFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest pServletRequest,
			ServletResponse pServletResponse, FilterChain pFilterChain)
			throws IOException, ServletException {
		
		boolean lAuthorize = false;
		boolean lOAuthConsumerCode = false;
		
		HttpServletRequest lRequest = (HttpServletRequest) pServletRequest;
		HttpServletResponse lResponse = (HttpServletResponse) pServletResponse;

		String lOAuthConsumerCodePath = lRequest.getRequestURI();
		if(StringUtils.isNotBlank(lOAuthConsumerCodePath))
		{
			lOAuthConsumerCode = lOAuthConsumerCodePath.contains("/OAuthConsumerCode");
		} // if
		
		System.out.println("PATH INFO " + lOAuthConsumerCodePath);
		
		// Case 1 - OAuthConsumerCode endpoint with auth code
		if(lOAuthConsumerCode)
		{
			String lAuthCode = lRequest.getParameter("code");
			
			if(StringUtils.isNotBlank(lAuthCode))
			{
				try
				{
					String lAuthResponse = readURI(FacebookClient.getAuthURI(lAuthCode));
					String [] lAuthResponseParts = lAuthResponse.split("&");

					String lAccessToken = lAuthResponseParts[0].split("=")[1];
					String lExpireIn = lAuthResponseParts[1].split("=")[1];
					//String lMachineID = lObject.get("machine_id");
					
					
					if(StringUtils.isNotBlank(lAccessToken) && StringUtils.isNotBlank(lExpireIn))
					{
						HttpSession lSession = lRequest.getSession();
						
						UserInfo lUserInfo = new UserInfo();
						lUserInfo.setAccessToken(lAccessToken);
						lUserInfo.setExpiresIn(lExpireIn);
						
						refreshSessionInfo(lAccessToken, lUserInfo, lSession);
						
						lAuthorize = true;
					}  // if
				}  // try
				catch(Exception pException)
				{
					System.out.println(pException.toString());
				}
			}  //if
		}  //if
		
		// Case 2 - Get access token from header
		String lTokenHeader = lRequest.getHeader("Authorization");

		if(!lAuthorize && StringUtils.isNotBlank(lTokenHeader))
		{
			System.out.println(lTokenHeader);

			try
			{
				String lAccessToken = lTokenHeader.replace("Bearer ", "");
				readURI(FacebookClient.getDebugTokenURI(lAccessToken));

				HttpSession lSession = lRequest.getSession();
				
				UserInfo lUserInfo = new UserInfo();
				lUserInfo.setAccessToken(lAccessToken);
				//lUserInfo.setExpiresIn(lExpireIn);
				
				refreshSessionInfo(lAccessToken, lUserInfo, lSession);
				
				lAuthorize = true;
			}  // try
			catch(Exception pException)
			{
				System.out.println(pException.toString());
			}  // catch
			
		}  // if
		
		
		// Case 3 - Get access token from body
		UserInfo lUserInfo = (UserInfo)lRequest.getSession().getAttribute(FacebookClient.getUserInfoSessionId());
		
		if(!lAuthorize && lUserInfo != null)
		{
			System.out.println(lUserInfo.getAccessToken());
			System.out.println(lUserInfo.getExpiresIn());
			
			try
			{
				readURI(FacebookClient.getDebugTokenURI(lUserInfo.getAccessToken()));

				refreshSessionInfo(lUserInfo.getAccessToken(), lUserInfo, lRequest.getSession());
				
				lAuthorize = true;
			}  // try
			catch(Exception pException)
			{
				System.out.println(pException.toString());
			}
			
		}  // if
		
		// Redirect to front page
		if(lAuthorize && lOAuthConsumerCode)
		{
			lResponse.sendRedirect(lRequest.getContextPath());

			
			return;
		}  // if
		
		if(!lAuthorize)
		{
			String lContentType = lRequest.getContentType();
			
			// Send JSON payload if it is coming from client with JSON type
			if(StringUtils.equalsIgnoreCase("application/json", lContentType))
			{
				lResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				
				lResponse.getWriter().println("{\"errorCode\":\"401\", \"reason\":\"invalid access token\", \"redirect_uri\":\""+FacebookClient.getLogInRedirectionURI()+"\"}");
			}  // if
			else
			{
				lResponse.sendRedirect(FacebookClient.getLogInRedirectionURI());
			}  // else
			
			return;
		}  // if
		
		pFilterChain.doFilter(lRequest, lResponse);
	}

	private void refreshSessionInfo(String pAccessToken, UserInfo pUserInfo, HttpSession pSession) throws JsonProcessingException, IOException
	{
		String lAuthResponse = readURI(FacebookClient.getUserInfoURI(pAccessToken));
		
		ObjectMapper lMapper = new ObjectMapper();
		JsonNode lNode = lMapper.readTree(lAuthResponse);
		
		pUserInfo.setFacebookProfileId(lNode.get("id").asText());
		pUserInfo.setFacebookProfileLink(lNode.get("link").asText());
		pUserInfo.setFirstName(lNode.get("first_name").asText());
		pUserInfo.setLastName(lNode.get("last_name").asText());
		pUserInfo.setEmail(lNode.get("email").asText());
		pSession.setAttribute(FacebookClient.getUserInfoSessionId(), pUserInfo);
		
	}
	
	private String readURI(String pURI) throws IOException
	{
		BufferedReader lInput = new BufferedReader(new InputStreamReader(new URL(pURI).openStream()));
		
		String lResponse = "";
		String lInputLine = "";
		
		while ((lInputLine = lInput.readLine()) != null)
		{
			lResponse = lResponse + lInputLine;
		}  // while
		
		lInput.close();
		
		return lResponse;
	}
	
	public void init(FilterConfig arg0) throws ServletException {

	}

}
