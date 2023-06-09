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
import com.chengxusheji.po.ClassInfo;
import com.chengxusheji.service.ClassInfoService;
import com.client.service.AuthService;
import com.client.utils.JsonResult;
import com.client.utils.JsonResultBuilder;
import com.client.utils.ReturnCode;

@RestController
@RequestMapping("/api/classInfo") 
public class ApiClassInfoController {
	@Resource ClassInfoService classInfoService;
	@Resource AuthService authService;

	@InitBinder("classInfo")
	public void initBinderClassInfo(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("classInfo.");
	}

	/*客户端ajax方式添加班级信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public JsonResult add(@Validated ClassInfo classInfo, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors()) //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR);
		if(classInfoService.getClassInfo(classInfo.getClassNumber()) != null) //验证主键是否重复
			return JsonResultBuilder.error(ReturnCode.PRIMARY_EXIST_ERROR);
        classInfoService.addClassInfo(classInfo); //添加到数据库
        return JsonResultBuilder.ok();
	}

	/*客户端ajax更新班级信息*/
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public JsonResult update(@Validated ClassInfo classInfo, BindingResult br, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		if (br.hasErrors())  //验证输入参数
			return JsonResultBuilder.error(ReturnCode.INPUT_PARAM_ERROR); 
        classInfoService.updateClassInfo(classInfo);  //更新记录到数据库
        return JsonResultBuilder.ok(classInfoService.getClassInfo(classInfo.getClassNumber()));
	}

	/*ajax方式显示获取班级详细信息*/
	@RequestMapping(value="/get/{classNumber}",method=RequestMethod.POST)
	public JsonResult getClassInfo(@PathVariable String classNumber,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键classNumber获取ClassInfo对象*/
        ClassInfo classInfo = classInfoService.getClassInfo(classNumber); 
        return JsonResultBuilder.ok(classInfo);
	}

	/*ajax方式删除班级记录*/
	@RequestMapping(value="/delete/{classNumber}",method=RequestMethod.POST)
	public JsonResult deleteClassInfo(@PathVariable String classNumber,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
		//通过accessToken获取到用户信息 
		String userName = authService.getUserName(request);
		if(userName == null) return JsonResultBuilder.error(ReturnCode.TOKEN_VALID_ERROR);
		try {
			classInfoService.deleteClassInfo(classNumber);
			return JsonResultBuilder.ok();
		} catch (Exception ex) {
			return JsonResultBuilder.error(ReturnCode.FOREIGN_KEY_CONSTRAINT_ERROR);
		}
	}

	//客户端查询班级信息
	@RequestMapping(value="/list",method=RequestMethod.POST)
	public JsonResult list(String classNumber,String className,String classSpecialFieldNumber,String classBirthDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (classNumber == null) classNumber = "";
		if (className == null) className = "";
		if (classSpecialFieldNumber == null) classSpecialFieldNumber = "";
		if (classBirthDate == null) classBirthDate = "";
		if(rows != 0)classInfoService.setRows(rows);
		List<ClassInfo> classInfoList = classInfoService.queryClassInfo(classNumber, className, classSpecialFieldNumber, classBirthDate, page);
	    /*计算总的页数和总的记录数*/
	    classInfoService.queryTotalPageAndRecordNumber(classNumber, className, classSpecialFieldNumber, classBirthDate);
	    /*获取到总的页码数目*/
	    int totalPage = classInfoService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = classInfoService.getRecordNumber();
	    HashMap<String, Object> resultMap = new HashMap<String, Object>();
	    resultMap.put("totalPage", totalPage);
	    resultMap.put("list", classInfoList);
	    return JsonResultBuilder.ok(resultMap);
	}

	//客户端ajax获取所有班级
	@RequestMapping(value="/listAll",method=RequestMethod.POST)
	public JsonResult listAll() throws Exception{
		List<ClassInfo> classInfoList = classInfoService.queryAllClassInfo(); 
		return JsonResultBuilder.ok(classInfoList);
	}
}

