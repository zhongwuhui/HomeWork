package com.controller;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONArray;
import com.mapper.TeacherMapper;
import com.po.Grade;
import com.po.HomeWork;
import com.po.Subject;
import com.po.User;
import com.util.DateUtil;
import com.util.PageBean;
import com.util.StringUtil;

@Controller
@SessionAttributes("User") 
public class TeacherController extends BaseController{
	@Autowired
	private TeacherMapper teacherMapper;
	// 教师查询作业历史操作
			@RequestMapping("/list_work")
			@ResponseBody
			public Map<String, Object> query(@RequestParam(value = "page", required = false) String page,String className,
					@RequestParam(value = "rows", required = false) String rows,HomeWork homework,String sort,String order,
					String createTimeStart,String createTimeEnd,@ModelAttribute("User")User user,ModelMap model) throws Exception {
				if (page != null && rows != null) {
					PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
					map.put("start", pageBean.getStart());
					map.put("size", pageBean.getPageSize());
				}
				map.put("classId", homework.getClassId());
				map.put("subjectId", homework.getSubjectId());
				map.put("subjectName", homework.getSubjectName());
				map.put("userId", user.getUserId());//user会自动绑定到名为"User"中所存储的user信息
				map.put("workId",homework.getWorkId());
				map.put("className", homework.getClassName());
				map.put("workDetail",StringUtil.formatLike(homework.getWorkDetail()));
				map.put("createTime", homework.getCreateTime());
				map.put("createTimeStart", createTimeStart);
				map.put("createTimeEnd", createTimeEnd);
				map.put("sort", sort);
				map.put("order", order);
				List<HomeWork> homeworks = teacherMapper.find(map);
				Long total = teacherMapper.getTotal(map);
				// 用来存储分页的数据
				pageMap.put("rows", homeworks);
				pageMap.put("total", total);
				return pageMap;
			}
			//跳转至作业管理列表
			@RequestMapping("teach_query")
			public String teachQuery() {
				return "teacher/teach";
			}
			@RequestMapping("/work_save")
			@ResponseBody
			public String save(HomeWork homework,@ModelAttribute("User")User user,ModelMap model,
					@RequestParam("stuUpload")String stuUpload,@RequestParam("chooseSubject")String chooseSubject,
					@RequestParam("chooseClass")String chooseClass) throws Exception {
				map.put("workDetail", homework.getWorkDetail());	
				List<HomeWork> homeworks = teacherMapper.find(map);	
					if (homeworks.size() < 1) {
						//teacherMapper
						homework.setCreateTime(DateUtil.getCurrentDateStr());
						teacherMapper.teacherAdd(user);
						teacherMapper.workAdd(homework);
						teacherMapper.timeAdd(homework);
						teacherMapper.subjectAdd(chooseSubject);
						teacherMapper.setStuUploadPath(stuUpload+"\\");
						teacherMapper.updateTheWorkPathOfNewWork(chooseClass);
						teacherMapper.classAdd(chooseClass);
						return "true";
					} else {
						return "exist";
					}
			}
			//修改作业记录
			@RequestMapping("work_update")
			@ResponseBody
			public String workUpdate(@RequestParam(value="editStuUpload",required=false)String editStuUpload,
					@RequestParam(value="editWorkDetail",required=false)String editWorkDetail,@RequestParam("workId")String workId) throws Exception {
				HomeWork homework = new HomeWork();
				//map.put("workUpPath", editStuUpload);
				//map.put("workDetil", editWorkDetail);
				map.put("workId", workId);
				List<HomeWork> homeworks = teacherMapper.find(map);
				if("".equals(editStuUpload)||editStuUpload==null) {
						homework.setWorkUpPath(homeworks.get(0).getWorkUpPath());
					if("".equals(editWorkDetail)||editWorkDetail==null) {
						homework.setWorkDetail(homeworks.get(0).getWorkDetail());
					}else {
						String newWorkDetail = editWorkDetail;
						homework.setWorkDetail(newWorkDetail);
					}
				}else {
					String newWorkUpPath = editStuUpload;
					homework.setWorkUpPath(newWorkUpPath );
					if("".equals(editWorkDetail)||editWorkDetail==null) {
						homework.setWorkDetail(homeworks.get(0).getWorkDetail());
					}else {
						String newWorkDetail = editWorkDetail;
						homework.setWorkDetail(newWorkDetail);
					}
				}
				homework.setWorkId(Integer.parseInt(workId));
				teacherMapper.update(homework);
				return "true";
			}
			@RequestMapping("work_delete")
			@ResponseBody
			public String deleteWork(@RequestParam(value = "ids") String ids) throws Exception {
				String[] idsStr = ids.split(",");
				for (int i = 0; i < idsStr.length; i++) {
					teacherMapper.delete(Integer.parseInt(idsStr[i]));
				}
				return "true";
			}
			@RequestMapping("find_grade")
		    @ResponseBody
		    public List<Grade> getGrade(@ModelAttribute("User")User user,ModelMap model){
		        map.put("userId", user.getUserId());
		        List<Grade> grades = teacherMapper.getGrade(user);
		        return grades;
		    }
			//获取当前教师教授科目
			@RequestMapping("find_subject")
		    @ResponseBody
		    public List<Subject> getSubject(@ModelAttribute("User")User user,ModelMap model){
		        map.put("userId", user.getUserId());
		        List<Subject> subjects = teacherMapper.getSubject(user);
		        return subjects;
		    } 
			
			//教师上传附件
			   /*
			    *采用spring提供的上传文件的方法
			    */
			   @RequestMapping("springUpload")
			   @ResponseBody
			   public String  springUpload(HttpServletRequest request,@RequestParam(value = "workId") String workId) throws Exception
			   {	map.put("workId", workId);
			   		List<HomeWork> homeworks=teacherMapper.find(map);
			   		HomeWork theWork = homeworks.get(0);
			   		String filePath = homeworks.get(0).getWorkPath();
			        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
			       CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
			               request.getSession().getServletContext());
			       //检查form中是否有enctype="multipart/form-data"
			       if(multipartResolver.isMultipart(request))
			       {
			           //将request变成多部分request
			           MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
			          //获取multiRequest 中所有的文件名
			           Iterator<String> iter=multiRequest.getFileNames();
			           while(iter.hasNext())
			           {
			               //一次遍历所有文件
			               MultipartFile file=multiRequest.getFile(iter.next().toString());
			               if(file!=null)
			               {	String name =file.getOriginalFilename();
			               		theWork.setFileName(name);
			               		teacherMapper.setFileName(theWork);//将文件名存入数据库中
			                   String path=filePath+name;
			                   //上传
			                   file.transferTo(new File(path));
			               }

			           }

			       }
			       return "true";
			   }
}
