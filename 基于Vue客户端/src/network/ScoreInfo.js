// 成绩信息信息业务处理
import request from './request'
import qs from 'qs'

/* 根据查询条件分页查询成绩信息信息 */
export function queryScoreInfo(studentNumber_user_name, courseNumber_courseNumber, page) {
    return request({
        url: '/scoreInfo/list',
        method: "POST",
        params: {
            "studentNumber.user_name": studentNumber_user_name,
            "courseNumber.courseNumber": courseNumber_courseNumber,
            "page": page,
            "rows": 10
        }
    })
}

/* 查询所有的成绩信息信息*/
export function queryAllScoreInfo() {
    return request({
        url: '/scoreInfo/listAll',
        method: "POST",
    })
}

/* 添加成绩信息信息 */
export function addScoreInfo(scoreInfo) {
    return request({
        url: '/scoreInfo/add',
        method: "POST",
        data: qs.stringify(scoreInfo),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 更新成绩信息信息 */
export function updateScoreInfo(scoreInfo) {
    return request({
        url: '/scoreInfo/update',
        method: "POST",
        data: qs.stringify(scoreInfo),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 根据主键记录编号查询成绩信息信息 */
export function getScoreInfo(scoreId) {
    return request({
        url: '/scoreInfo/get/' + scoreId,
        method: "POST"
    })
}

/* 根据主键scoreId删除成绩信息 */
export function deleteScoreInfo(scoreId) {
    return request({
        url: '/scoreInfo/delete/' + scoreId,
        method: "POST",
    })
}
