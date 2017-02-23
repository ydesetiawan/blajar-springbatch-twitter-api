package com.yd.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author edys
 * @version 1.0, May 4, 2014
 * @since 3.0.0
 */
@Component
public class CustomAuthenticationSuccessHandler extends
		SavedRequestAwareAuthenticationSuccessHandler {

	@SuppressWarnings("unused")
	private static Logger log = Logger
			.getLogger(CustomAuthenticationSuccessHandler.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {

		super.onAuthenticationSuccess(request, response, authentication);
	}
}
