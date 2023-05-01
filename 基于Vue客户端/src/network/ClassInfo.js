// 班级信息业务处理
import request from './request'
import qs from 'qs'

/* 根据查询条件分页查询班级信息 */
export function queryClassInfo(classNumber, className, classSpecialFieldNumber, classBirthDate, page) {
    return request({
        url: '/classInfo/list',
        method: "POST",
        params: {
            "classNumber": classNumber,
            "className": className,
            "classSpecialFieldNumber": classSpecialFieldNumber,
            "classBirthDate": classBirthDate,
            "page": page,
            "rows": 10
        }
    })
}

/* 查询所有的班级信息*/
export function queryAllClassInfo() {
    return request({
        url: '/classInfo/listAll',
        method: "POST",
    })
}

/* 添加班级信息 */
export function addClassInfo(classInfo) {
    return request({
        url: '/classInfo/add',
        method: "POST",
        data: qs.stringify(classInfo),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 更新班级信息 */
export function updateClassInfo(classInfo) {
    return request({
        url: '/classInfo/update',
        method: "POST",
        data: qs.stringify(classInfo),
        headers: { //添加请求头
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
        }
    })
}

/* 根据主键班级编号查询班级信息 */
export function getClassInfo(classNumber) {
    return request({
        url: '/classInfo/get/' + classNumber,
        method: "POST"
    })
}

/* 根据主键classNumber删除班级 */
export function deleteClassInfo(classNumber) {
    return request({
        url: '/classInfo/delete/' + classNumber,
        method: "POST",
    })
}
