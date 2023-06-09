package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;
import com.client.utils.JsonUtils;
import com.client.utils.SessionConsts;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Student {
    /*手机号*/
    @NotEmpty(message="手机号不能为空")
    private String user_name;
    public String getUser_name(){
        return user_name;
    }
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    /*学号*/
    @NotEmpty(message="学号不能为空")
    private String studentNumber;
    public String getStudentNumber() {
        return studentNumber;
    }
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    /*登录密码*/
    @NotEmpty(message="登录密码不能为空")
    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    /*姓名*/
    @NotEmpty(message="姓名不能为空")
    private String studentName;
    public String getStudentName() {
        return studentName;
    }
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    /*性别*/
    @NotEmpty(message="性别不能为空")
    private String studentSex;
    public String getStudentSex() {
        return studentSex;
    }
    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    /*所在班级*/
    private ClassInfo studentClassNumber;
    public ClassInfo getStudentClassNumber() {
        return studentClassNumber;
    }
    public void setStudentClassNumber(ClassInfo studentClassNumber) {
        this.studentClassNumber = studentClassNumber;
    }

    /*出生日期*/
    @NotEmpty(message="出生日期不能为空")
    private String studentBirthday;
    public String getStudentBirthday() {
        return studentBirthday;
    }
    public void setStudentBirthday(String studentBirthday) {
        this.studentBirthday = studentBirthday;
    }

    /*政治面貌*/
    private String studentState;
    public String getStudentState() {
        return studentState;
    }
    public void setStudentState(String studentState) {
        this.studentState = studentState;
    }

    /*学生照片*/
    private String studentPhoto;
    public String getStudentPhoto() {
        return studentPhoto;
    }
    public void setStudentPhoto(String studentPhoto) {
        this.studentPhoto = studentPhoto;
    }

    private String studentPhotoUrl;
    public void setStudentPhotoUrl(String studentPhotoUrl) {
		this.studentPhotoUrl = studentPhotoUrl;
	}
	public String getStudentPhotoUrl() {
		return  SessionConsts.BASE_URL + studentPhoto;
	}
    /*联系电话*/
    private String studentTelephone;
    public String getStudentTelephone() {
        return studentTelephone;
    }
    public void setStudentTelephone(String studentTelephone) {
        this.studentTelephone = studentTelephone;
    }

    /*学生邮箱*/
    private String studentEmail;
    public String getStudentEmail() {
        return studentEmail;
    }
    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    /*联系qq*/
    private String studentQQ;
    public String getStudentQQ() {
        return studentQQ;
    }
    public void setStudentQQ(String studentQQ) {
        this.studentQQ = studentQQ;
    }

    /*家庭地址*/
    private String studentAddress;
    public String getStudentAddress() {
        return studentAddress;
    }
    public void setStudentAddress(String studentAddress) {
        this.studentAddress = studentAddress;
    }

    /*附加信息*/
    private String studentMemo;
    public String getStudentMemo() {
        return studentMemo;
    }
    public void setStudentMemo(String studentMemo) {
        this.studentMemo = studentMemo;
    }

    @JsonIgnore
    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonStudent=new JSONObject(); 
		jsonStudent.accumulate("user_name", this.getUser_name());
		jsonStudent.accumulate("studentNumber", this.getStudentNumber());
		jsonStudent.accumulate("password", this.getPassword());
		jsonStudent.accumulate("studentName", this.getStudentName());
		jsonStudent.accumulate("studentSex", this.getStudentSex());
		jsonStudent.accumulate("studentClassNumber", this.getStudentClassNumber().getClassName());
		jsonStudent.accumulate("studentClassNumberPri", this.getStudentClassNumber().getClassNumber());
		jsonStudent.accumulate("studentBirthday", this.getStudentBirthday().length()>19?this.getStudentBirthday().substring(0,19):this.getStudentBirthday());
		jsonStudent.accumulate("studentState", this.getStudentState());
		jsonStudent.accumulate("studentPhoto", this.getStudentPhoto());
		jsonStudent.accumulate("studentTelephone", this.getStudentTelephone());
		jsonStudent.accumulate("studentEmail", this.getStudentEmail());
		jsonStudent.accumulate("studentQQ", this.getStudentQQ());
		jsonStudent.accumulate("studentAddress", this.getStudentAddress());
		jsonStudent.accumulate("studentMemo", this.getStudentMemo());
		return jsonStudent;
    }

    @Override
	public String toString() {
		return JsonUtils.toJson(this);
	}
}