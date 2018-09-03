package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import com.mapper.AdminMapperForSubject;
import com.po.Subject;
import com.po.User;
import com.util.PageBean;


@Controller 
public class AdminForSubjectController extends BaseController{
	private String subjectId = null;
	@Autowired
	private  AdminMapperForSubject amfs;
		// 管理员查询操作
		@RequestMapping("/list_subject")
		@ResponseBody
		public Map<String, Object> query(@RequestParam(value = "page", required = false) String page,
				@RequestParam(value = "rows", required = false) String rows,Subject subject,String sort,String order) throws Exception {
			if (page != null && rows != null) {
				PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
				map.put("start", pageBean.getStart());
				map.put("size", pageBean.getPageSize());
			}
			map.put("subjectId",subject.getSubjectId());
			map.put("subjectName",subject.getSubjectId());
			map.put("sort", sort);
			map.put("order", order);
			List<Subject> subjects = amfs.find(map);
			Long total = amfs.getTotal(map);
			// 用来存储分页的数据
			pageMap.put("rows", subjects);
			pageMap.put("total", total);
			return pageMap;
		}
		// 跳转至科目列表
		@RequestMapping("/admin_query_subject")
		public String adminQuery() throws Exception {
			return "administrator/subject";
		}
		// 添加修改班级
		@RequestMapping("/subject_save")
		@ResponseBody
		public String save(Subject subject) throws Exception {
			map.put("subjectName", subject.getSubjectName());	
			List<Subject> subjects = amfs.find(map);	
			if(subject.getSubjectId()==null) {
				if (subjects.size() < 1) {										
					amfs.add(subject);
					return "true";
				} else {
					return "exist";
				}
				}else {
					if ("".equals(subject.getSubjectName()) || subject.getSubjectName() == null) {
						subject.setSubjectName(subjects.get(0).getSubjectName());
					} else {
						String newname =subject.getSubjectName();
						subject.setSubjectName(newname);
					}
					amfs.update(subject);
					return "true";
				}
	} 
		// 删除科目并且将与该科目相关联的班级和老师一并删除
		@RequestMapping("/subject_delete")
		@ResponseBody
		public String delete(@RequestParam(value = "ids") String ids) throws Exception {
			String[] idsStr = ids.split(",");
			for (int i = 0; i < idsStr.length; i++) {
				amfs.delete(Integer.parseInt(idsStr[i]));
			}
			return "true";
		}
		//跳转到任课教师界面
		@RequestMapping("/subject_to_teacher")
		public String subjectToTeacher(@RequestParam(value = "subjectId") String subjectId) throws Exception {
			this.subjectId=subjectId;//在切换界面时将科目Id赋值給全局变量
			return "administrator/listteacher2";
		}
		//列举科目任课教师
		@RequestMapping("/find_teacher")
		@ResponseBody
		public List<User> findTeacher(){
			map.put("subjectId", subjectId);
			List<User> teachers= amfs.listTeachers(map);
			return teachers;
			
		}
		//添加任课教师
		@RequestMapping("/add_teacher")
		@ResponseBody
		public String addTeacher(@RequestParam(value = "chooseTeacher") String chooseTeacher) {
			map.put("subjectId", subjectId);
			map.put("userId", chooseTeacher);
			amfs.addTeacherToTheSubject(map);
			return "success";
		}
		//删除任课教师
		@RequestMapping("/subject_delete_teacher")
		@ResponseBody
		public String deleteTeacher(@RequestParam(value = "ids") String ids) {
			String[] idsStr = ids.split(",");
			for (int i = 0; i < idsStr.length; i++) {
				map.put("subjectId", subjectId);
				map.put("userId",Integer.parseInt(idsStr[i]));
				amfs.deleteTeacherToTheSubject(map);
				map.clear();
			}
			return "true";
		}
}

