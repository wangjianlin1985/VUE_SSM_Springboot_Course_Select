//前端路由代码
import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)

/**
 * 重写路由的push方法
 */
// const routerPush = Router.prototype.push
// Router.prototype.push = function push(location) {
//     return routerPush.call(this, location).catch(error => error)
// }

const Home = () => import ('../views/home/Home') //引入首页组件页
const ClassInfoList = () => import('../views/classInfo/ClassInfoList.vue') //引入班级列表组件页
const ClassInfoAdd = () => import('../views/classInfo/ClassInfoAdd') //引入班级添加组件页
const ClassInfoModify = () => import('../views/classInfo/ClassInfoModify') //引入班级修改组件页
const ClassInfoDetail = () => import('../views/classInfo/ClassInfoDetail') //引入班级详情组件页

const StudentList = () => import('../views/student/StudentList.vue') //引入学生列表组件页
const StudentAdd = () => import('../views/student/StudentAdd') //引入学生添加组件页
const StudentModify = () => import('../views/student/StudentModify') //引入学生修改组件页
const StudentDetail = () => import('../views/student/StudentDetail') //引入学生详情组件页

const TeacherList = () => import('../views/teacher/TeacherList.vue') //引入教师列表组件页
const TeacherAdd = () => import('../views/teacher/TeacherAdd') //引入教师添加组件页
const TeacherModify = () => import('../views/teacher/TeacherModify') //引入教师修改组件页
const TeacherDetail = () => import('../views/teacher/TeacherDetail') //引入教师详情组件页

const CourseInfoList = () => import('../views/courseInfo/CourseInfoList.vue') //引入课程列表组件页
const CourseInfoAdd = () => import('../views/courseInfo/CourseInfoAdd') //引入课程添加组件页
const CourseInfoModify = () => import('../views/courseInfo/CourseInfoModify') //引入课程修改组件页
const CourseInfoDetail = () => import('../views/courseInfo/CourseInfoDetail') //引入课程详情组件页

const CourseSelectList = () => import('../views/courseSelect/CourseSelectList.vue') //引入学生选课列表组件页
const CourseSelectAdd = () => import('../views/courseSelect/CourseSelectAdd') //引入学生选课添加组件页
const CourseSelectModify = () => import('../views/courseSelect/CourseSelectModify') //引入学生选课修改组件页
const CourseSelectDetail = () => import('../views/courseSelect/CourseSelectDetail') //引入学生选课详情组件页

const ScoreInfoList = () => import('../views/scoreInfo/ScoreInfoList.vue') //引入成绩信息列表组件页
const ScoreInfoAdd = () => import('../views/scoreInfo/ScoreInfoAdd') //引入成绩信息添加组件页
const ScoreInfoModify = () => import('../views/scoreInfo/ScoreInfoModify') //引入成绩信息修改组件页
const ScoreInfoDetail = () => import('../views/scoreInfo/ScoreInfoDetail') //引入成绩信息详情组件页

const NoticeList = () => import('../views/notice/NoticeList.vue') //引入新闻公告列表组件页
const NoticeAdd = () => import('../views/notice/NoticeAdd') //引入新闻公告添加组件页
const NoticeModify = () => import('../views/notice/NoticeModify') //引入新闻公告修改组件页
const NoticeDetail = () => import('../views/notice/NoticeDetail') //引入新闻公告详情组件页

const Profile = () => import ('../views/profile/Profile') //引入个人中心组件页
const Login = () => import ('../views/login/Login') //引入登录组件页
const CreateAccount = () => import ('../views/login/CreateAccount') //引入创建账号组件页
const Coupon = () => import ('../views/profile/CouponExchange') //引入优惠卷组件页
const About = () => import ('../views/profile/About') //引入关于我们组件页

const routes = [{
    path: '',  // 根路径跳到登录页路由
    redirect: '/login'
  },
  {
    path: '/login', // 系统登录页路由
    component: Login
  },
  {
    path: '/register', // 创建新账号页路由
    component: CreateAccount
  },
  {
    path: '/home',   // 首页路由
    component: Home
  },

  {
    path: '/classInfo/add', // 添加班级页路由
    component: ClassInfoAdd
  },
  {
    path: '/classInfo/modify/:classNumber', // 修改班级页路由
    component: ClassInfoModify
  },
  {
    path: '/classInfo/list', // 班级查询页路由
    component: ClassInfoList
  },
  {
    path: '/classInfo/detail/:classNumber', // 班级详情页路由
    component: ClassInfoDetail
  },
  {
    path: '/student/add', // 添加学生页路由
    component: StudentAdd
  },
  {
    path: '/student/modify/:user_name', // 修改学生页路由
    component: StudentModify
  },
  {
    path: '/student/list', // 学生查询页路由
    component: StudentList
  },
  {
    path: '/student/detail/:user_name', // 学生详情页路由
    component: StudentDetail
  },
  {
    path: '/teacher/add', // 添加教师页路由
    component: TeacherAdd
  },
  {
    path: '/teacher/modify/:teacherNumber', // 修改教师页路由
    component: TeacherModify
  },
  {
    path: '/teacher/list', // 教师查询页路由
    component: TeacherList
  },
  {
    path: '/teacher/detail/:teacherNumber', // 教师详情页路由
    component: TeacherDetail
  },
  {
    path: '/courseInfo/add', // 添加课程页路由
    component: CourseInfoAdd
  },
  {
    path: '/courseInfo/modify/:courseNumber', // 修改课程页路由
    component: CourseInfoModify
  },
  {
    path: '/courseInfo/list', // 课程查询页路由
    component: CourseInfoList
  },
  {
    path: '/courseInfo/detail/:courseNumber', // 课程详情页路由
    component: CourseInfoDetail
  },
  {
    path: '/courseSelect/add', // 添加学生选课页路由
    component: CourseSelectAdd
  },
  {
    path: '/courseSelect/modify/:selectId', // 修改学生选课页路由
    component: CourseSelectModify
  },
  {
    path: '/courseSelect/list', // 学生选课查询页路由
    component: CourseSelectList
  },
  {
    path: '/courseSelect/detail/:selectId', // 学生选课详情页路由
    component: CourseSelectDetail
  },
  {
    path: '/scoreInfo/add', // 添加成绩信息页路由
    component: ScoreInfoAdd
  },
  {
    path: '/scoreInfo/modify/:scoreId', // 修改成绩信息页路由
    component: ScoreInfoModify
  },
  {
    path: '/scoreInfo/list', // 成绩信息查询页路由
    component: ScoreInfoList
  },
  {
    path: '/scoreInfo/detail/:scoreId', // 成绩信息详情页路由
    component: ScoreInfoDetail
  },
  {
    path: '/notice/add', // 添加新闻公告页路由
    component: NoticeAdd
  },
  {
    path: '/notice/modify/:noticeId', // 修改新闻公告页路由
    component: NoticeModify
  },
  {
    path: '/notice/list', // 新闻公告查询页路由
    component: NoticeList
  },
  {
    path: '/notice/detail/:noticeId', // 新闻公告详情页路由
    component: NoticeDetail
  },

  {
    path: '/Profile', // 个人中心页路由
    component: Profile,
  },
  {
    path: '/about', //关于我们页路由
    component: About
  },
  {
    path: '/coupon', // 优惠卷页路由
    component: Coupon
  }
]

const router = new Router({
  routes,
  mode: 'history'
})

export default router

