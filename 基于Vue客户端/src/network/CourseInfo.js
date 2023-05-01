// 课程信息业务处理
import request from './request'
import qs from 'qs'

/* 根据查询条件分页查询课程信息 */
export function queryCourseInfo(courseNumber, courseName, courseTeacher_teacherNumber, page) {
    return request({
        url: '/courseInfo/list',
        method: "POST",
        params: {
            "courseNumber": courseNumber,
            "courseName": courseName,
            "courseTeacher.teacherNumber": courseTeacher_teacherNumber,
            "page": page,
            "rows": 10
        }
    })
}

/* 查询所有的课程信息*/
export function queryAllCourseInfo() {
    return request({
        url: '/courseInfo/listAll',
        method: "POST",
    })
}

/* 添加课程信息 */
export function addCourseInfo(courseInfo) {
    return request({
        url: '/courseInfo/add',
        method: "POST",
        data: qs.stringify(courseInfo),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 更新课程信息 */
export function updateCourseInfo(courseInfo) {
    return request({
        url: '/courseInfo/update',
        method: "POST",
        data: qs.stringify(courseInfo),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 根据主键课程编号查询课程信息 */
export function getCourseInfo(courseNumber) {
    return request({
        url: '/courseInfo/get/' + courseNumber,
        method: "POST"
    })
}

/* 根据主键courseNumber删除课程 */
export function deleteCourseInfo(courseNumber) {
    return request({
        url: '/courseInfo/delete/' + courseNumber,
        method: "POST",
    })
}
