package com.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.mapper.AdminMapper;
import com.po.Grade;
import com.po.HomeWork;
import com.po.TeacherAndClass;
import com.po.User;
import com.util.PageBean;

@SessionAttributes("ClassId") //将所选班级信息保存到session中
@Controller 
public class AdminController extends BaseController{
	private String classId=null;
	@Autowired
	private  AdminMapper adminMapper;
		// 管理员查询操作
		@RequestMapping("/list_class")
		@ResponseBody
		public Map<String, Object> query(@RequestParam(value = "page", required = false) String page,
				@RequestParam(value = "rows", required = false) String rows,Grade grade,String sort,String order) throws Exception {
			if (page != null && rows != null) {
				PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
				map.put("start", pageBean.getStart());
				map.put("size", pageBean.getPageSize());
			}
			map.put("classId",grade.getClassId());
			map.put("className",grade.getClassName());
			map.put("sort", sort);
			map.put("order", order);
			List<Grade> grades = adminMapper.find(map);
			Long total = adminMapper.getTotal(map);
			// 用来存储分页的数据
			pageMap.put("rows", grades);
			pageMap.put("total", total);
			return pageMap;
		}
		// 跳转至班级列表
		@RequestMapping("/admin_query_class")
		public String adminQuery() throws Exception {
			return "administrator/admin";
		}
		//跳转到管理员首页
		@RequestMapping("/to_admin_main.action")
		public String toAdminMain() throws Exception {
			return "administrator/main";
		}
		// 添加修改班级
		@RequestMapping("/class_save")
		@ResponseBody
		public String save(Grade grade) throws Exception {
			String workPath=null;
			map.put("className", grade.getClassName());	
			List<Grade> grades = adminMapper.find(map);	
			if(grade.getClassId()==null) {
				if (grades.size() < 1) {
					workPath=grade.getWorkPath()+"\\";
					grade.setWorkPath(workPath);
					adminMapper.add(grade);
					return "true";
				} else {
					return "exist";
				}
				}else {
					map.clear();
					map.put("classId", grade.getClassId());
					List<Grade>grade3=adminMapper.find(map);
					if ("".equals(grade.getClassName()) || grade.getClassName() == null ) {
							grade.setClassName(grade3.get(0).getClassName());
						if("".equals(grade.getWorkPath()) ||grade.getWorkPath() == null) {
							grade.setWorkPath(grade3.get(0).getWorkPath());
						}else {
							String newPath =grade.getWorkPath()+"\\";
							grade.setWorkPath(newPath);
						}
						adminMapper.update(grade);
						return "true";
					} else {
						if("".equals(grade.getWorkPath()) ||grade.getWorkPath() == null) {
							grade.setWorkPath(grade3.get(0).getWorkPath());
						}else {
							String newPath =grade.getWorkPath()+"\\";
							grade.setWorkPath(newPath);
						}
						String newname =grade.getClassName();
						grade.setClassName(newname);						
					}
					adminMapper.update(grade);
					return "true";
				}
	} 
		// 删除班级
		@RequestMapping("/class_delete")
		@ResponseBody
		public String delete(@RequestParam(value = "ids") String ids) throws Exception {
			String[] idsStr = ids.split(",");
			for (int i = 0; i < idsStr.length; i++) {
				adminMapper.deleteClassWork(Integer.parseInt(idsStr[i]));
				adminMapper.deleteClassSubject(Integer.parseInt(idsStr[i]));
				adminMapper.deleteClassTeaher(Integer.parseInt(idsStr[i]));
				adminMapper.delete(Integer.parseInt(idsStr[i]));
			}
			return "true";
		}
		/*@RequestMapping("admin_query_students")
		public String litStudents(@RequestParam(value="id")String id,ModelMap model) {
			 model.addAttribute("ClassId",id); //②向ModelMap中添加veiw层传入的id值 
			 map.put("classId", id);admin_to_teachers
			return "true";
		}*/
		@RequestMapping("admin_to_students")
		public String adminToStu(@RequestParam(value="classId")String classId,ModelMap model) {
			this.classId=classId;
			model.addAttribute("ClassId",classId);
			return "administrator/liststu";
		}
		@RequestMapping("admin_to_teachers")
		public String adminToTeacher(@RequestParam(value="classId")String classId,ModelMap model) {
			this.classId=classId;
			model.addAttribute("ClassId",classId); //②向ModelMap中添加一个属性
			return "administrator/listteacher";
		}
		@RequestMapping("/list_stu")
		@ResponseBody
		public Map<String, Object> adminQueryForStu(@RequestParam(value = "page", required = false) String page,
				@RequestParam(value = "rows", required = false) String rows,User user,String sort,String order) throws Exception {
			if (page != null && rows != null) {
				PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
				map.put("start", pageBean.getStart());
				map.put("size", pageBean.getPageSize());
			}
			map.put("classId",classId);
			map.put("userId",user.getUserId());
			map.put("userName",user.getUserName());
			map.put("userNumber", user.getUserNumber());
			map.put("sort", sort);
			map.put("order", order);
			List<User> students = adminMapper.findStu(map);
			Long total = adminMapper.getTotalStu(map);
			// 用来存储分页的数据
			pageMap.put("rows", students);
			pageMap.put("total", total);
			return pageMap;
		}
		//列举教师
		@RequestMapping("/list_teacher")
		@ResponseBody
		public Map<String, Object> adminQueryForTeacher(@RequestParam(value = "page", required = false) String page,
				@RequestParam(value = "rows", required = false) String rows,User user,String sort,String order) throws Exception {
			if (page != null && rows != null) {
				PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
				map.put("start", pageBean.getStart());
				map.put("size", pageBean.getPageSize());
			}
			map.put("classId",classId);
			map.put("userId",user.getUserId());
			map.put("userName",user.getUserName());
			map.put("userNumber", user.getUserNumber());
			map.put("sort", sort);
			map.put("order", order);
			List<User> teachers = adminMapper.findTeacher(map);
			Long total = adminMapper.getTotalTeacher(map);
			// 用来存储分页的数据
			pageMap.put("rows", teachers);
			pageMap.put("total", total);
			return pageMap;
		}
		@RequestMapping("/workPath_save")
		public void setWorkPathForClass(@RequestParam("classId")String classId,@RequestParam("workPath")String workPath) {
			map.put("classId", classId);
			workPath=workPath+"\\";
			map.put("workPath", workPath);
			adminMapper.setWorkPathOfClass(map);
		}
		@RequestMapping("/list_all_teahcer")
		@ResponseBody
		public List<User> listAllTeachers(){
			return adminMapper.allTeacher();
		} 
}

