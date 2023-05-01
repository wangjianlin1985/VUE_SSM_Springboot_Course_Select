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
import com.chengxusheji.po.Teacher;
import com.chengxusheji.service.TeacherService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/teacher") 
public class ApiTeacherController {
	@Resource TeacherService teacherService;
	@Resource AuthService authService;

	@InitBinder("teacher")
	public void initBinderTeacher(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("teacher.");
	}

	/*客户端ajax方式添加教师信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated Teacher teacher, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
		if(teacherService.getTeacher(teacher.getTeacherNumber()) != null) //验证主键是否重复
			return JsonResultBuilder.error(ReturnCode.PRIMARY_EXIST_ERROR);
        teacherService.addTeacher(teacher); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新教师信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated Teacher teacher, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        teacherService.updateTeacher(teacher);  //更新记录到数据库
        return JsonResultBuilder.ok(teacherService.getTeacher(teacher.getTeacherNumber()));
	}

	/*ajax方式显示获取教师详细信息*/
	@RequestMapping(value="/get/{teacherNumber}",method=RequestMethod.POST)
	public JsonResult getTeacher(@PathVariable String teacherNumber,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键teacherNumber获取Teacher对象*/
        Teacher teacher = teacherService.getTeacher(teacherNumber); 
        return JsonResultBuilder.ok(teacher);
	}

	/*ajax方式删除教师记录*/
	@RequestMapping(value="/delete/{teacherNumber}",method=RequestMethod.POST)
	public JsonResult deleteTeacher(@PathVariable String teacherNumber,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			teacherService.deleteTeacher(teacherNumber);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询教师信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(String teacherNumber,String teacherName,String teacherBirthday,String teacherArriveDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (teacherNumber == null) teacherNumber = "";
		if (teacherName == null) teacherName = "";
		if (teacherBirthday == null) teacherBirthday = "";
		if (teacherArriveDate == null) teacherArriveDate = "";
		if(rows != 0)teacherService.setRows(rows);
		List<Teacher> teacherList = teacherService.queryTeacher(teacherNumber, teacherName, teacherBirthday, teacherArriveDate, page);
	    /*计算总的页数和总的记录数*/
	    teacherService.queryTotalPageAndRecordNumber(teacherNumber, teacherName, teacherBirthday, teacherArriveDate);
	    /*获取到总的页码数目*/
	    int totalPage = teacherService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = teacherService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", teacherList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有教师
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<Teacher> teacherList = teacherService.queryAllTeacher(); 
		return JsonResultBuilder.ok(teacherList);
	}
}

