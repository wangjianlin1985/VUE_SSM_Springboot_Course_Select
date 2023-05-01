// 学生信息业务处理
import request from './request'
import qs from 'qs'

/* 根据查询条件分页查询学生信息 */
export function queryStudent(user_name, studentNumber, studentName, studentClassNumber_classNumber, studentBirthday, page) {
    return request({
        url: '/student/list',
        method: "POST",
        params: {
            "user_name": user_name,
            "studentNumber": studentNumber,
            "studentName": studentName,
            "studentClassNumber.classNumber": studentClassNumber_classNumber,
            "studentBirthday": studentBirthday,
            "page": page,
            "rows": 10
        }
    })
}

/* 查询所有的学生信息*/
export function queryAllStudent() {
    return request({
        url: '/student/listAll',
        method: "POST",
    })
}

/* 添加学生信息 */
export function addStudent(student) {
    return request({
        url: '/student/add',
        method: "POST",
        data: qs.stringify(student),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 更新学生信息 */
export function updateStudent(student) {
    return request({
        url: '/student/update',
        method: "POST",
        data: qs.stringify(student),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 根据主键手机号查询学生信息 */
export function getStudent(user_name) {
    return request({
        url: '/student/get/' + user_name,
        method: "POST"
    })
}

/* 根据主键user_name删除学生 */
export function deleteStudent(user_name) {
    return request({
        url: '/student/delete/' + user_name,
        method: "POST",
    })
}
