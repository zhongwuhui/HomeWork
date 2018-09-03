package com.mapper;

import java.util.List;
import java.util.Map;

import com.po.Grade;
import com.po.Subject;
import com.po.TeacherAndClass;
import com.po.User;

public interface AdminMapper extends BaseMapper{
	//列表显示班级所有学生
	public List<User> findStu(Map map);
	//显示班级学生數
	public Long getTotalStu(Map map) throws Exception;
	//显示班级所有老师
	public List<User> findTeacher(Map map);
	public Long getTotalTeacher(Map map);
	//删除班级时分步进行删除所有与该记录相关的外键
	//1.删除该班级相关作业
	public void deleteClassWork(int classId);
	//2.删除该班级相关老师
	public void deleteClassTeaher(int classId);
	//3.删除该班级相关科目
	public void deleteClassSubject(int classId);
	//设定班级教师文件上传路径
	public void setWorkPathOfClass(Map map);
	//列举所有教师
	public List<User> allTeacher();
	
}
