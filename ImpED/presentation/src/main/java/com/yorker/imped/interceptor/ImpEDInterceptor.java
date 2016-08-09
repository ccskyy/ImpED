package com.yorker.imped.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yorker.imped.form.LoginForm;

@Component
public class ImpEDInterceptor extends HandlerInterceptorAdapter {
	
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//System.out.println("XINEInterceptor: REQUEST Intercepted for URI: preHandle :" + request.getRequestURI());

		 if (!request.getRequestURI().equals("/ImpED/")) {
			LoginForm userData = (LoginForm) request.getSession().getAttribute("LOGGEDIN_USER");
			if (userData == null) {
				response.sendRedirect("/ImpED");
				return false;
			}
		}
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		//System.out.println("XINEInterceptor: REQUEST Intercepted for URI: postHandle :" + request.getRequestURI());
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//System.out.println("XINEInterceptor: REQUEST Intercepted for URI: afterCompletion :" + request.getRequestURI());
	}

}