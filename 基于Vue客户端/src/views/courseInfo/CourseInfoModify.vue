<template>
    <div id="courseInfo">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>编辑课程</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <van-form @submit="onSubmit">
                <van-field readonly name="courseInfo.courseNumber" v-model="courseNumber" label="课程编号：" placeholder="请输入课程编号" :rules="[{ required: true, message: '请填写课程编号' }]" />
                <van-field autosize clearable name="courseInfo.courseName" v-model="courseName" label="课程名称：" placeholder="请输入课程名称" :rules="[{ required: true, message: '请填写课程名称' }]" />
                <van-field v-show="false" clearable name="courseInfo.courseTeacher.teacherNumber" v-model="courseTeacher_teacherNumber" label="上课老师：" />
                <van-field readonly clickable label="上课老师" :value="courseTeacher_teacherName"  right-icon="arrow-down" placeholder="选择上课老师" @click="showCourseTeacherPicker = true" />
                <van-picker v-show="showCourseTeacherPicker" show-toolbar :columns="courseTeacherColumns" :default-index="1" @cancel="showCourseTeacherPicker = false" @confirm="onConfirmCourseTeacher" />
                <van-field autosize clearable name="courseInfo.courseTime" v-model="courseTime" label="上课时间：" placeholder="请输入上课时间" :rules="[{ required: true, message: '请填写上课时间' }]" />
                <van-field autosize clearable name="courseInfo.coursePlace" v-model="coursePlace" label="上课地点：" placeholder="请输入上课地点" :rules="[{ required: true, message: '请填写上课地点' }]" />
                <van-field clearable name="courseInfo.courseScore" v-model="courseScore" type="number" label="课程学分：" placeholder="请输入课程学分" :rules="[{ required: true, message: '请填写课程学分' }]" />
                <div class="van-cell van-field" style="pointer-events: auto;">
                    <div class="van-cell__title van-field__label"><span>课程简介：</span></div>
                    <div class="van-cell__value van-field__value">
                        <div class="van-field__body"></div>
                    </div>
                </div>
                <van-field  v-show="false" name="courseInfo.courseMemo" v-model="courseMemo" rows="5" label="课程简介" type="textarea" maxlength="8000"  placeholder="请输入课程简介" show-word-limit />
                <VueUeditorWrap  v-model="courseMemo" :config="editorConfig" />
                <div style="margin: 16px;">
                    <van-button round block type="info" native-type="submit">更新</van-button>
                </div>
            </van-form>
        </scroll>
    </div>
</template>

<script>
    import NavBar from '../../components/common/navbar/NavBar'
    import Scroll from '../../components/common/scroll/Scroll'
    import VueUeditorWrap from 'vue-ueditor-wrap'
    import { queryAllTeacher } from '../../network/Teacher'
    import { updateCourseInfo} from "../../network/CourseInfo";
    import { getCourseInfo } from "../../network/CourseInfo";
    export default {
        name: "CourseInfoAdd",
        components: {
            NavBar,
            Scroll,
            VueUeditorWrap
        },
        data() {
            return {
                courseNumber: "",  //课程编号
                courseName: "", //课程名称
                courseTeacherList: [], //上课老师列表
                courseTeacher_teacherNumber: '', //教师编号
                courseTeacher_teacherName: "", //教师姓名
                showCourseTeacherPicker: false, //是否显示上课老师Picker
                courseTeacherColumns: [], //上课老师Picker的选项
                courseTime: "", //上课时间
                coursePlace: "", //上课地点
                courseScore: "", //课程学分
                courseMemo: "", //课程简介
                // 简单配置
                editorConfig: {
                    // 编辑器不自动被内容撑高
                    autoHeightEnabled: false,
                    // 初始容器高度
                    initialFrameHeight: 280,
                    // 初始容器宽度
                    initialFrameWidth: '100%',
                    // 上传文件接口
                    //serverUrl: 'http://localhost:8080/JavaWebProject/ueditor1_4_3/jsp/controller.jsp',
                }
            }
        },

        mounted() {

        },

        created() {
            /*查询所有上课老师初始化下拉Picker*/
            queryAllTeacher().then(res => {
                this.courseTeacherList = res.data;
                for(var i=0;i<this.courseTeacherList.length;i++) {
                    this.courseTeacherColumns.push(this.courseTeacherList[i].teacherName)
                }
            });

            this.courseNumber = this.$route.params.courseNumber;  //接收课程编号参数
            //向后端请求单条课程记录
            getCourseInfo(this.courseNumber).then(res => {
                var courseInfo = res.data;
                this.courseNumber = courseInfo.courseNumber; //课程编号
                this.courseName = courseInfo.courseName;  //课程名称
                this.courseTeacher_teacherNumber = courseInfo.courseTeacher.teacherNumber; //上课老师
                this.courseTeacher_teacherName = courseInfo.courseTeacher.teacherName;
                this.courseTime = courseInfo.courseTime;  //上课时间
                this.coursePlace = courseInfo.coursePlace;  //上课地点
                this.courseScore = courseInfo.courseScore;  //课程学分
                this.courseMemo = courseInfo.courseMemo;  //课程简介
            })
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
            back() {
                this.$router.go(-1);
            },

            onConfirmCourseTeacher(value,index) {
                this.courseTeacher_teacherName = value; //选中某个上课老师后响应
                this.courseTeacher_teacherNumber = this.courseTeacherList[index].teacherNumber;
                this.showCourseTeacherPicker = false;
            },
            onSubmit(courseInfo) {  //向后台请求更新课程
                updateCourseInfo(courseInfo).then(res => {
                    console.log(res)
                    this.$notify.success('更新成功~')
                }).catch(error => { this.$notify.error(error)})
            },
        },
    }
</script>

<style scoped>
    .nav {
        background-color: #54bf8e;
        color: #fff;
    }

    .back {
        font-family: 'icomoon';
        font-size: 30px;
    }

    #courseInfo {
        height: 100vh;
        width: 100vw;
        position: relative;
    }

    .content {
        position: fixed;
        top: 50px;
        left: 0;
        right: 0px;
        bottom: 49px;
    }

    .van-dropdown-menu {  width: 100%; }

    .avatar {
        width: 100px;
        height: 100px;
        background-color: #f3f3f3;
        float: left;
        top: 5px;
        right: 5px;
        border-radius: 3px;
       margin-bottom: 26px;
    }
</style>

