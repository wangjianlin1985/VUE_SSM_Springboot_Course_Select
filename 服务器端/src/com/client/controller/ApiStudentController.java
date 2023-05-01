package com.client.controller;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.chengxusheji.po.Student;
import com.chengxusheji.po.ClassInfo;
import com.chengxusheji.service.StudentService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/student") 
public class ApiStudentController {
	@Resource StudentService studentService;
	@Resource AuthService authService;

	@InitBinder("studentClassNumber")
	public void initBinderstudentClassNumber(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("studentClassNumber.");
	}
	@InitBinder("student")
	public void initBinderStudent(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("student.");
	}

	/*客户端ajax方式添加学生信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated Student student, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
		if(studentService.getStudent(student.getUser_name()) != null) //验证主键是否重复
			return JsonResultBuilder.error(ReturnCode.PRIMARY_EXIST_ERROR);
        studentService.addStudent(student); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新学生信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated Student student, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        studentService.updateStudent(student);  //更新记录到数据库
        return JsonResultBuilder.ok(studentService.getStudent(student.getUser_name()));
	}

	/*ajax方式显示获取学生详细信息*/
	@RequestMapping(value="/get/{user_name}",method=RequestMethod.POST)
	public JsonResult getStudent(@PathVariable String user_name,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键user_name获取Student对象*/
        Student student = studentService.getStudent(user_name); 
        return JsonResultBuilder.ok(student);
	}

	/*ajax方式删除学生记录*/
	@RequestMapping(value="/delete/{user_name}",method=RequestMethod.POST)
	public JsonResult deleteStudent(@PathVariable String user_name,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			studentService.deleteStudent(user_name);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询学生信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(String user_name,String studentNumber,String studentName,@ModelAttribute("studentClassNumber") ClassInfo studentClassNumber,String studentBirthday,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (user_name == null) user_name = "";
		if (studentNumber == null) studentNumber = "";
		if (studentName == null) studentName = "";
		if (studentBirthday == null) studentBirthday = "";
		if(rows != 0)studentService.setRows(rows);
		List<Student> studentList = studentService.queryStudent(user_name, studentNumber, studentName, studentClassNumber, studentBirthday, page);
	    /*计算总的页数和总的记录数*/
	    studentService.queryTotalPageAndRecordNumber(user_name, studentNumber, studentName, studentClassNumber, studentBirthday);
	    /*获取到总的页码数目*/
	    int totalPage = studentService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = studentService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", studentList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有学生
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<Student> studentList = studentService.queryAllStudent(); 
		return JsonResultBuilder.ok(studentList);
	}
}

