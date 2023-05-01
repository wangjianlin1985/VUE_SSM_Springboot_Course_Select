// 学生选课信息业务处理
import request from './request'
import qs from 'qs'

/* 根据查询条件分页查询学生选课信息 */
export function queryCourseSelect(selectTime, studentNumber_user_name, courseNumber_courseNumber, page) {
    return request({
        url: '/courseSelect/list',
        method: "POST",
        params: {
            "selectTime": selectTime,
            "studentNumber.user_name": studentNumber_user_name,
            "courseNumber.courseNumber": courseNumber_courseNumber,
            "page": page,
            "rows": 10
        }
    })
}

/* 查询所有的学生选课信息*/
export function queryAllCourseSelect() {
    return request({
        url: '/courseSelect/listAll',
        method: "POST",
    })
}

/* 添加学生选课信息 */
export function addCourseSelect(courseSelect) {
    return request({
        url: '/courseSelect/add',
        method: "POST",
        data: qs.stringify(courseSelect),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 添加学生选课信息 */
export function studentSelectCourse(studentNumber,courseNumber) { 
    var courseSelect = {} 
    return request({
        url: '/courseSelect/add2',
        method: "POST",
        params: {
            "courseSelect.studentNumber.user_name": studentNumber,
            "courseSelect.courseNumber.courseNumber": courseNumber
        },
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }

    })
}



/* 更新学生选课信息 */
export function updateCourseSelect(courseSelect) {
    return request({
        url: '/courseSelect/update',
        method: "POST",
        data: qs.stringify(courseSelect),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 根据主键记录编号查询学生选课信息 */
export function getCourseSelect(selectId) {
    return request({
        url: '/courseSelect/get/' + selectId,
        method: "POST"
    })
}

/* 根据主键selectId删除学生选课 */
export function deleteCourseSelect(selectId) {
    return request({
        url: '/courseSelect/delete/' + selectId,
        method: "POST",
    })
}
