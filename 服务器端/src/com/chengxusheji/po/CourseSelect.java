package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;
import com.client.utils.JsonUtils;
import com.client.utils.SessionConsts;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class CourseSelect {
    /*记录编号*/
    private Integer selectId;
    public Integer getSelectId(){
        return selectId;
    }
    public void setSelectId(Integer selectId){
        this.selectId = selectId;
    }

    /*选课学生*/
    private Student studentNumber;
    public Student getStudentNumber() {
        return studentNumber;
    }
    public void setStudentNumber(Student studentNumber) {
        this.studentNumber = studentNumber;
    }

    /*选择课程*/
    private CourseInfo courseNumber;
    public CourseInfo getCourseNumber() {
        return courseNumber;
    }
    public void setCourseNumber(CourseInfo courseNumber) {
        this.courseNumber = courseNumber;
    }

    /*选课时间*/
    @NotEmpty(message="选课时间不能为空")
    private String selectTime;
    public String getSelectTime() {
        return selectTime;
    }
    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }

    @JsonIgnore
    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonCourseSelect=new JSONObject(); 
		jsonCourseSelect.accumulate("selectId", this.getSelectId());
		jsonCourseSelect.accumulate("studentNumber", this.getStudentNumber().getStudentName());
		jsonCourseSelect.accumulate("studentNumberPri", this.getStudentNumber().getUser_name());
		jsonCourseSelect.accumulate("courseNumber", this.getCourseNumber().getCourseName());
		jsonCourseSelect.accumulate("courseNumberPri", this.getCourseNumber().getCourseNumber());
		jsonCourseSelect.accumulate("selectTime", this.getSelectTime().length()>19?this.getSelectTime().substring(0,19):this.getSelectTime());
		return jsonCourseSelect;
    }

    @Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}