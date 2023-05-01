package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ClassInfo;
import com.chengxusheji.po.Student;

import com.chengxusheji.mapper.StudentMapper;
@Service
public class StudentService {

	@Resource StudentMapper studentMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加学生记录*/
    public void addStudent(Student student) throws Exception {
    	studentMapper.addStudent(student);
    }

    /*按照查询条件分页查询学生记录*/
    public ArrayList<Student> queryStudent(String user_name,String studentNumber,String studentName,ClassInfo studentClassNumber,String studentBirthday,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!user_name.equals("")) where = where + " and t_student.user_name like '%" + user_name + "%'";
    	if(!studentNumber.equals("")) where = where + " and t_student.studentNumber like '%" + studentNumber + "%'";
    	if(!studentName.equals("")) where = where + " and t_student.studentName like '%" + studentName + "%'";
    	if(null != studentClassNumber &&  studentClassNumber.getClassNumber() != null  && !studentClassNumber.getClassNumber().equals(""))  where += " and t_student.studentClassNumber='" + studentClassNumber.getClassNumber() + "'";
    	if(!studentBirthday.equals("")) where = where + " and t_student.studentBirthday like '%" + studentBirthday + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return studentMapper.queryStudent(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Student> queryStudent(String user_name,String studentNumber,String studentName,ClassInfo studentClassNumber,String studentBirthday) throws Exception  { 
     	String where = "where 1=1";
    	if(!user_name.equals("")) where = where + " and t_student.user_name like '%" + user_name + "%'";
    	if(!studentNumber.equals("")) where = where + " and t_student.studentNumber like '%" + studentNumber + "%'";
    	if(!studentName.equals("")) where = where + " and t_student.studentName like '%" + studentName + "%'";
    	if(null != studentClassNumber &&  studentClassNumber.getClassNumber() != null && !studentClassNumber.getClassNumber().equals(""))  where += " and t_student.studentClassNumber='" + studentClassNumber.getClassNumber() + "'";
    	if(!studentBirthday.equals("")) where = where + " and t_student.studentBirthday like '%" + studentBirthday + "%'";
    	return studentMapper.queryStudentList(where);
    }

    /*查询所有学生记录*/
    public ArrayList<Student> queryAllStudent()  throws Exception {
        return studentMapper.queryStudentList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String user_name,String studentNumber,String studentName,ClassInfo studentClassNumber,String studentBirthday) throws Exception {
     	String where = "where 1=1";
    	if(!user_name.equals("")) where = where + " and t_student.user_name like '%" + user_name + "%'";
    	if(!studentNumber.equals("")) where = where + " and t_student.studentNumber like '%" + studentNumber + "%'";
    	if(!studentName.equals("")) where = where + " and t_student.studentName like '%" + studentName + "%'";
    	if(null != studentClassNumber &&  studentClassNumber.getClassNumber() != null && !studentClassNumber.getClassNumber().equals(""))  where += " and t_student.studentClassNumber='" + studentClassNumber.getClassNumber() + "'";
    	if(!studentBirthday.equals("")) where = where + " and t_student.studentBirthday like '%" + studentBirthday + "%'";
        recordNumber = studentMapper.queryStudentCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取学生记录*/
    public Student getStudent(String user_name) throws Exception  {
        Student student = studentMapper.getStudent(user_name);
        return student;
    }

    /*根据微信openid获取学生记录*/
    public Student getStudentByOpenid(String openid) throws Exception {
        Student student = studentMapper.getStudentByOpenid(openid);
        return student;
    }

    /*更新学生记录*/
    public void updateStudent(Student student) throws Exception {
        studentMapper.updateStudent(student);
    }

    /*关联读者微信openid记录*/
    public void updateStudentOpenid(String user_name,String openId) throws Exception {
        studentMapper.updateStudentOpenid(user_name, openId);
    }

    /*删除一条学生记录*/
    public void deleteStudent (String user_name) throws Exception {
        studentMapper.deleteStudent(user_name);
    }

    /*删除多条学生信息*/
    public int deleteStudents (String user_names) throws Exception {
    	String _user_names[] = user_names.split(",");
    	for(String _user_name: _user_names) {
    		studentMapper.deleteStudent(_user_name);
    	}
    	return _user_names.length;
    }
}
