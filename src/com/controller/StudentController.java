package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.mapper.StudentMapper;
import com.po.HomeWork;
import com.po.User;
import com.util.PageBean;
import com.util.StringUtil;

@Controller
@SessionAttributes("User")
public class StudentController extends BaseController{
	@Autowired
	private StudentMapper studentMapper;
	// 教师查询作业历史操作
			@RequestMapping("/stu_list_work")
			@ResponseBody
			public Map<String, Object> query(@RequestParam(value = "page", required = false) String page,
					@RequestParam(value = "rows", required = false) String rows,HomeWork homework,String sort,String order,
					String createTimeStart,String createTimeEnd,@ModelAttribute("User")User user,ModelMap model) throws Exception {
				if (page != null && rows != null) {
					PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(rows));
					map.put("start", pageBean.getStart());
					map.put("size", pageBean.getPageSize());
				}
				map.put("userId", user.getUserId());//user会自动绑定到名为"User"中所存储的user信息
				map.put("userName",homework.getUserName());
				map.put("workId",homework.getWorkId());
				map.put("subjectName", homework.getSubjectName());
				map.put("workDetail",StringUtil.formatLike(homework.getWorkDetail()));								
				map.put("fileName", homework.getFileName());
				map.put("createTime", homework.getCreateTime());
				map.put("createTimeStart", createTimeStart);
				map.put("createTimeEnd", createTimeEnd);
				map.put("sort", sort);
				map.put("order", order);
				List<HomeWork> homeworks = studentMapper.find(map);
				Long total = studentMapper.getTotal(map);
				// 用来存储分页的数据
				pageMap.put("rows", homeworks);
				pageMap.put("total", total);
				return pageMap;
			}
			//下载附件
		     /* @RequestMapping("/down")  
		      public ResponseEntity<byte[]> download(HttpServletRequest request,@RequestParam(value = "workId") String workId) throws Exception {
		    	  map.put("workId", workId);		    	  
		    	  List<HomeWork> homeworks=studentMapper.find(map);
		    	  String filePath = homeworks.get(0).getWorkPath();
		          File file = new File(filePath + "14级英语四级考试登记表.xls");
		          byte[] body = null;
		          InputStream is = new FileInputStream(file);
		          body = new byte[is.available()];
		          is.read(body);
		          HttpHeaders headers = new HttpHeaders();
		          headers.add("Content-Disposition", "attchement;filename=" + file.getName());
		          HttpStatus statusCode = HttpStatus.OK;
		          ResponseEntity<byte[]> entity = new ResponseEntity<byte[]>(body, headers, statusCode);
		          return entity;
		      }*/
			@RequestMapping("/down")
			public ResponseEntity<byte[]> dowload(@RequestParam(value = "workId") String workId) throws Exception{
				map.put("workId", workId);		    	  
		    	  List<HomeWork> homeworks=studentMapper.find(map);
		    	  String filePath = homeworks.get(0).getWorkPath();
		    	  String name = homeworks.get(0).getFileName();	
		    		  String path=filePath+name;
					    File file=new File(path);
					    String fileName=new String(file.getName().getBytes("utf-8"),"iso-8859-1"); //解决中文乱码问题
					    HttpHeaders headers=new HttpHeaders();
					    headers.setContentDispositionFormData("attachment", fileName);// aatachment  附件
					    headers.setContentType(MediaType.IMAGE_PNG);
					    ResponseEntity<byte[]> entity=new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers,HttpStatus.CREATED);
					        return entity;
			    }
			//学生提交作业
			   /*
			    *采用spring提供的上传文件的方法
			    */
			   @RequestMapping("HandInWorks")
			   @ResponseBody
			   public String  springUpload(HttpServletRequest request,@RequestParam(value = "workId") String workId) throws Exception
			   {	map.put("workId", workId);
			   		List<HomeWork> homeworks=studentMapper.find(map);

			   		String filePath = homeworks.get(0).getWorkUpPath();//获取文件指定的上传的路劲
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

			                   String path=filePath+name;
			                   //上传
			                   file.transferTo(new File(path));
			               }

			           }

			       }
			       return "true";
			   }
}
