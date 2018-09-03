package com.mapper;

import java.util.Map;

import com.po.User;

public interface AdminMapperForClass extends BaseMapper{
	//为当前班级添加教师
		public User getTeacherById(String userId);
		public void addTeacherForTheClass(Map map);
		//为当前班级添加学生
		public void insertStudentIntoClass(User student);
		public void setClassIdForStudent(String classId);
		public void deleteStudents(int studentId);
}
