package com.yorker.imped.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.yorker.imped.form.LoginForm;

/**
 * The Class LoginController.
 */
@Controller
@RequestMapping("/")
public class LoginController {
	
	/**
	 * Logout.
	 *
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(ModelMap model,HttpServletRequest request) {
		model.addAttribute("message","You have been sucessfully logged out.");
		request.getSession().removeAttribute("LOGGEDIN_USER");
		request.getSession().invalidate();
		return "MetaLoad";
	}
	
	/**
	 * Login check.
	 *
	 * @param form the form
	 * @param model the model
	 * @param request the request
	 * @return the string
	 */
	@RequestMapping(value = "/loginCheck", method = RequestMethod.POST)
	public String loginCheck(@ModelAttribute("LoginForm") LoginForm form, ModelMap model, HttpServletRequest request) {
		String retval = "ImpED";
		try {
			
		} catch (Exception e) {
			model.addAttribute("message", "Invalid credentials entered");
		}
		return retval;
	}

}