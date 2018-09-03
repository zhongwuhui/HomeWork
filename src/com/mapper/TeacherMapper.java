package com.mapper;

import java.util.List;

import com.po.Grade;
import com.po.HomeWork;
import com.po.Subject;
import com.po.User;

public interface TeacherMapper extends BaseMapper{
	//显示改老师所布置过的所有作业
	public List<HomeWork> listWork(User user);
	//新增作业按两步进行先把布置作业老师的id写入作业表，下一步更行作业下达班级，再在下一步更新作业内容
	public void teacherAdd(User user);
	
	//更新作业下达班级
	public void classAdd(String classId);
	//接上一步操作
	public void workAdd(HomeWork work);
	//添加作业创建时间
	public void timeAdd(HomeWork time);
	//添加作业科目
	public void subjectAdd(String subjectId);
	//获取该老师所管辖班级
	public List<Grade> getGrade(User user);
	//获取该老师所教授课程
	public List<Subject> getSubject(User user);
	//将附件名存入数据库中
	public void setFileName(HomeWork homework);
	//设定学生作业上传路劲
	public void setStuUploadPath(String path);
	//根据作业下达班级获取教师文件上传路径
	//public String getTeacherUploadPath(String classId);
	public void updateTheWorkPathOfNewWork(String classId);
}
