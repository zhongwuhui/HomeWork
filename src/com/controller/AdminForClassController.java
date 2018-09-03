package com.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.mapper.AdminMapper;
import com.mapper.AdminMapperForClass;
import com.po.User;

@SessionAttributes("ClassId") //将所选班级信息保存到session中
@Controller 
public class AdminForClassController extends BaseController{
	@Autowired
	private AdminMapperForClass amfc;
	@Autowired
	private AdminMapper adminMapper;
	
			//列举教师
			@RequestMapping("/teacher_save")
			@ResponseBody
			public String saveTeacher(@RequestParam(value="chooseTeacher")String chooseTeacher,
					@ModelAttribute("ClassId")String classId,ModelMap model) throws Exception {
				//TeacherAndClass tac = new TeacherAndClass();
				//User teacher= amfc.getTeacherById(chooseTeacher);
				//map.put("classId",classId);			
				 //List<Grade> grades= adminMapper.find(map);
				 map.put("classId", classId);
				 map.put("userId", chooseTeacher);
				//tac.setGrade(grades.get(0));
				//tac.setUser(teacher);
				amfc.addTeacherForTheClass(map);
				return "true";
			}
			//删除学生
			@RequestMapping(value="student_delete")
		    @ResponseBody
		    public String deleteStudents(@RequestParam(value = "ids") String ids) throws Exception {
				String[] idsStr = ids.split(",");
				for (int i = 0; i < idsStr.length; i++) {
					amfc.deleteStudents(Integer.parseInt(idsStr[i]));
				}
				return "true";
			}
			//从excel表批量导入学生数据
			 //处理2007版本
		    @RequestMapping(value="readXLSX")
		    @ResponseBody
		    public String readXLSX(@ModelAttribute("ClassId")String classId,ModelMap model,
		    		@RequestParam("file")CommonsMultipartFile partFile,HttpServletRequest request) {
		        InputStream is = null;
		        XSSFWorkbook xssfWorkbook = null;
		        try {
		        	//String path = request.getServletContext().getRealPath("/upload");
		        	//String name = request.getParameter("name");
		        	//String filename = partFile.getOriginalFilename();
		        	//File file = new File(path+"/"+filename);
		        	//获取上传的文件流
		        	is = partFile.getInputStream();
		            // 1 打开Excel文件
		            //is = new FileInputStream("E:\\UseForUpload\\users.xlsx");

		            // 2 通过POI的工作薄类解析xls文件
		            xssfWorkbook = new XSSFWorkbook(is);

		        } catch (Exception e) {
		            is = null;
		            e.printStackTrace();
		        }

		        if (is == null)
		            return "failed";

		        // 3 创建user对象准备接收数据
		        User user = new User();

		        // 4 循环工作表Sheet，一个excel中可能有多个sheet
		        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
		            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
		            if (xssfSheet == null) {
		                continue;
		            }
		            // 循环行Row,一个sheet中可以有多行
		            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {

		                // 取得当前行的数据
		                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
		                if (xssfRow != null) {

		                    // 获取单元格对象
		                    XSSFCell userNumberFromExcel= xssfRow.getCell(0);
		                    XSSFCell passwordFromExcel = xssfRow.getCell(1);
		                    XSSFCell roleIdFromExcel = xssfRow.getCell(2);
		                    XSSFCell userNameFromExcel = xssfRow.getCell(3);		     
		                    // 全部先作为字符串数据提取出来
		                    String userNumber = getXValue(userNumberFromExcel);
		                    String password = getXValue(passwordFromExcel);
		                    String roleId = getXValue(roleIdFromExcel);
		                    String userName = getXValue(userNameFromExcel);		                   

		                    try {
		                        // 转换并赋值给user
		                        // userNumber是string类型
		                        user.setUserNumber(userNumber);
		                        // name是String类型
		                        user.setPassword(password);
		                        // roleId是int类型
		                        user.setRoleId(Integer.parseInt(roleId));
		                        //userName是string类型
		                        user.setUserName(userName);
		                        // 入库
		                        amfc.insertStudentIntoClass(user);
		                        //更新学生班级
		                        amfc.setClassIdForStudent(classId);
		                       
		                    } catch (Exception e) {

		                    }
		                } // 结束不为空的行的处理
		            } // 结束循环处理row
		        } // 结束循环处理sheet

		        // 关闭EXCEL表
		        try {
		            xssfWorkbook.close();
		        } catch (Exception e) {

		        }

		        return "success";
		    }

		//对应版本读取数据的函数,工具类2007版本
		private String getXValue(XSSFCell xssfCell) {

		        if (xssfCell.getCellTypeEnum() == CellType.BOOLEAN) {
		            // 返回布尔类型的值
		            return String.valueOf(xssfCell.getBooleanCellValue());
		        } else if (xssfCell.getCellTypeEnum() == CellType.NUMERIC) {
		            // 返回数值类型的值，日期类型的数据在EXCEL表中也是数值型
		            String cellValue = "";
		            if (HSSFDateUtil.isCellDateFormatted(xssfCell)) { // 判断是日期类型
		                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
		                Date dt = HSSFDateUtil.getJavaDate(xssfCell.getNumericCellValue());// 获取成DATE类型
		                cellValue = dateformat.format(dt);
		            } else {
		                DecimalFormat df = new DecimalFormat("0");
		                cellValue = df.format(xssfCell.getNumericCellValue());
		            }
		            return cellValue;
		        } else if (xssfCell.getCellTypeEnum() == CellType._NONE) {
		            return "";
		        } else if (xssfCell.getCellTypeEnum() == CellType.BLANK) {
		            return "";
		        } else if (xssfCell.getCellTypeEnum() == CellType.STRING) {
		            // 返回字符串类型的值
		            return String.valueOf(xssfCell.getStringCellValue());
		        }
		        return null;
		    }
}
