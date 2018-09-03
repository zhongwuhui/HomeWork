package com.mapper;

import java.util.List;
import java.util.Map;

import com.po.User;

public interface AdminMapperForSubject extends BaseMapper{
		//展示所有老师用于給班级添加教师
		public List<User> listTeachers(Map map);
		//添加任课教师
		public void addTeacherToTheSubject(Map map);
		//删除任课教师
		public void deleteTeacherToTheSubject(Map map);
}
