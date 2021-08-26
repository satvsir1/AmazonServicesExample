package com.example.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

@Component
public class FilterConfig implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
	    Enumeration<String> headerNames = httpRequest.getHeaderNames();
	    

	    
		System.out.println("Filter is called");

		if (headerNames != null) {
			while (headerNames.hasMoreElements()) {
				System.out.println("Header: " + httpRequest.getHeader(headerNames.nextElement()));
			}
		}
		chain.doFilter(request, response);
	}

}
