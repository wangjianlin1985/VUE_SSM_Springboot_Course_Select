// 教师信息业务处理
import request from './request'
import qs from 'qs'

/* 根据查询条件分页查询教师信息 */
export function queryTeacher(teacherNumber, teacherName, teacherBirthday, teacherArriveDate, page) {
    return request({
        url: '/teacher/list',
        method: "POST",
        params: {
            "teacherNumber": teacherNumber,
            "teacherName": teacherName,
            "teacherBirthday": teacherBirthday,
            "teacherArriveDate": teacherArriveDate,
            "page": page,
            "rows": 10
        }
    })
}

/* 查询所有的教师信息*/
export function queryAllTeacher() {
    return request({
        url: '/teacher/listAll',
        method: "POST",
    })
}

/* 添加教师信息 */
export function addTeacher(teacher) {
    return request({
        url: '/teacher/add',
        method: "POST",
        data: qs.stringify(teacher),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 更新教师信息 */
export function updateTeacher(teacher) {
    return request({
        url: '/teacher/update',
        method: "POST",
        data: qs.stringify(teacher),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 根据主键教师编号查询教师信息 */
export function getTeacher(teacherNumber) {
    return request({
        url: '/teacher/get/' + teacherNumber,
        method: "POST"
    })
}

/* 根据主键teacherNumber删除教师 */
export function deleteTeacher(teacherNumber) {
    return request({
        url: '/teacher/delete/' + teacherNumber,
        method: "POST",
    })
}
