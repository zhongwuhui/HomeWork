package com.mapper;

import java.util.List;

import com.po.Grade;
import com.po.User;

public interface UserMapper extends BaseMapper{
	//登录用户名密码查询（用于判断用户名密码的对错）
		public List<User> findByNumberPassword(User user) ;
	//用户综合信息查询(精确查找，用于判断身份)
		public User findUser(User user);
		//修改所有作业教师文件上传路径
		public void updateWorkPath(Grade grade);
}
