package com.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.mapper.UserMapper;
import com.po.User;

@Controller //注明为控制层，不加会找不到handler
@SessionAttributes("User") //把当前登录者信息存入名为User的session中
public class LoginController  extends BaseController{
	@Autowired
	protected UserMapper userMapper;
		@RequestMapping("/check")
		@ResponseBody
		 public String login(HttpServletRequest req,HttpServletResponse resp,HttpSession session,User user,ModelMap model) throws UnsupportedEncodingException{
	          req.setCharacterEncoding("utf-8");//设置参数的编码格式
	          String userNumber =req.getParameter("usernumber");
	          String userPwd =req.getParameter("password");
	          user.setPassWord(userPwd);
	          user.setUserNumber(userNumber);
	        // 进行用户身份验证
	   		List<User> users = userMapper.findByNumberPassword(user);
	   		if (users.size() < 1) {
	   			return "false";
	   		} else {
	   			// 在session中保存用户身份信息
	   			session.setAttribute("user", users.get(0));
	   			model.addAttribute("User",users.get(0)); //②向ModelMap中添加User
	   			return "true";
	   		}
	      }
		// 跳转系统默认首页
		@RequestMapping("/body")
		public String body() throws Exception {
			return "body";
		}
		//根据权限跳转到相应操作界面
		@RequestMapping("/authority")
		public String authority(HttpServletRequest req,User user) throws Exception {
			req.setCharacterEncoding("utf-8");//设置参数的编码格式
	          String userNumber =req.getParameter("usernumber");
	          String userPwd =req.getParameter("password");
	          user.setPassWord(userPwd);
	          user.setUserNumber(userNumber);
	          user = userMapper.findUser(user);
			if(user.getRoleId()==0){
				return "administrator/main";
			}else if(user.getRoleId()==1){
				return "teacher/main";
			}else {
				return "student/main";
			}
			
		}
		@RequestMapping("/stu_query")
		public String stuQuery() throws Exception {
			return "student/stu";
		}
		// 退出
		@RequestMapping("/logout")
		public String logout(HttpSession session) throws Exception {
			// 清除session
			session.invalidate();
			// 重定向到商品列表页面
			return "redirect:/login.jsp";
		}
	}

