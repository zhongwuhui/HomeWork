package com.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

public class BaseController {
	protected Map<String, Object> map = new HashMap<String, Object>();
	protected Map<String, Object> pageMap = new HashMap<String, Object>();
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected String currentUser;
	protected String currentUserPwd;

	@ModelAttribute
	public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		this.currentUser = (String) this.session.getAttribute("username");//获取当前登录者的名称
		this.currentUserPwd =(String) this.session.getAttribute("password");//获取当前登录者的登录密码
	}
}
