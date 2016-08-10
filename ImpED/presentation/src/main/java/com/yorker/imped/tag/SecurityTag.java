package com.yorker.imped.tag;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.yorker.imped.form.LoginForm;

public class SecurityTag extends TagSupport {
	private static final long serialVersionUID = 1679101235377980637L;
	private String objectName;

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int doStartTag() throws JspException {
		boolean flag = false;
		{
			HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
			String[] objectNames = null;
	        Set<String> userObjectNames = null;
	        userObjectNames =((LoginForm) request.getSession().getAttribute("LOGGEDIN_USER")).getFeatureCodes();
	        objectNames = objectName.split(",");
	        byte len = (byte) objectNames.length;
	        if (userObjectNames != null){
	            for (byte i=0; i < len; i++){
	           	 if (userObjectNames.contains(objectNames[i])){
	                	flag = true;
	                	break;
	                }else{
	                	flag = false;
	                }
	           }
	        }
		}
        if (flag){
        	return EVAL_BODY_INCLUDE;
        }else{
        	return SKIP_BODY;
        }
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
}
