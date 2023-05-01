<template>
    <div id="courseInfo">
        <van-popup v-model="showQueryCourseInfoForm"  closeable position="top" :style="{ height: 'auto' }">
            <div class="courseInfoQueryForm">
                <h3>课程查询</h3>
                <van-field clearable v-model="courseNumber" label="课程编号：" placeholder="请输入课程编号" />
                <van-field clearable v-model="courseName" label="课程名称：" placeholder="请输入课程名称" />
                <van-field readonly clickable label="上课老师" :value="courseTeacher_teacherName" right-icon="arrow-down" placeholder="选择上课老师" @click="showCourseTeacherPicker = true" />
                <van-popup v-model="showCourseTeacherPicker" position="bottom">
                    <van-picker show-toolbar :columns="courseTeacherColumns" @cancel="showCourseTeacherPicker = false" @confirm="onConfirmCourseTeacher" />
                </van-popup>
                <div class="queryBtn">
                    <van-button round  size= small type="primary" @click="queryCourseInfo">查询</van-button>
                </div>
            </div>
        </van-popup>

        <nav-bar class="nav" ref="nav" >
            <template v-slot:left>
                <van-icon name="plus" size="25" v-show="false" @click.stop="courseInfoAdd" />
                <van-icon name="arrow-left" v-show="false" size="25"  @click.stop="back" />
            </template>
            <template v-slot:center>课程列表</template>
            <template v-slot:right><van-icon name="search" size="25"  @click.stop="showQueryCourseInfoForm=true" /></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" :isPullDown="true" @pullingDown="pullingDown" :isPullUp="true" @pullingUp="pullingUp"  @iscroll="contentScroll">
            <div class="ignore-pull-down1" ref="pull" v-show="pullFlag">下拉更新</div>
            <div class="ignore-pull-down2" ref="pull" v-show="!pullFlag">
                <span class="ball1"></span><span class="ball2"></span><span class="ball3"></span>
            </div>
            <div class="courseInfoList">
                <template v-for="courseInfo in courseInfoList">
                    <div class="courseInfoItem" :key="courseInfo.courseNumber" @click.stop="courseInfoDetailClick(`${courseInfo.courseNumber}`)">
                        <div class="content_view">
                            <div class="courseInfoText">
                                <div>课程编号：{{courseInfo.courseNumber}}</div>
                                <div>课程名称：{{courseInfo.courseName}}</div>
                                <div>上课老师：{{courseInfo.courseTeacher.teacherName}}</div>
                                <div>课程学分：{{courseInfo.courseScore}}</div>
                            </div>
                        </div>
                        <div class="opt_view" v-show="false">
                            <van-button round  size= small type="primary"  @click.stop="courseInfoEditClick(`${courseInfo.courseNumber}`)" >编辑</van-button>
                            <van-button round  size= small type="warning" @click.stop="courseInfoDeleteClick(`${courseInfo.courseNumber}`)" style="margin-left:10px;">删除</van-button>
                        </div>
                    </div>
                </template>
            </div>
            <div class="block"></div>
        </scroll>
        <return-top @returnTop="returnTop" v-show="isReturnTop"></return-top>
    </div>
</template>

<script>
    import NavBar from '../../components/common/navbar/NavBar'
    import Scroll from '../../components/common/scroll/Scroll'
    import ReturnTop from '../../components/common/returntop/ReturnTop'

    import {queryCourseInfo,deleteCourseInfo} from '../../network/CourseInfo'
    import { queryAllTeacher } from '../../network/Teacher'
    export default {
        name: 'CourseInfoList',
        components: {
            NavBar,
            Scroll,
            ReturnTop
        },
        data() {
            return {
                courseInfoList: [],  //界面需要展示的课程列表数据
                courseNumber: "", //课程编号查询关键字
                courseName: "", //课程名称查询关键字

                courseTeacherList:[], //上课老师列表
                courseTeacher_teacherNumber: '',  //要查询的教师编号
                courseTeacher_teacherName: "不限制", //要查询的教师姓名
                showCourseTeacherPicker: false, //是否显示上课老师Picker
                courseTeacherColumns: ['不限制'], //上课老师Picker的选项初始化

                pullFlag: true,  //是否允许下拉刷新标志
                page: 1, //默认显示结果第1页
                showQueryCourseInfoForm: false, //是否显示查询课程表单
                isReturnTop: false,
            }
        },

        created() {
            //初始化上课老师下拉框数据
            queryAllTeacher().then(res => {
                this.courseTeacherList = res.data;
                for(var i=0;i<this.courseTeacherList.length;i++) {
                    this.courseTeacherColumns.push(this.courseTeacherList[i].teacherName)
                }
            });
            //查询课程信息
            this.courseInfoQuery("");
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
           back() {
                this.$router.go(-1);
           },

           onConfirmCourseTeacher(value,index) {  //选中某个上课老师后响应
                this.courseTeacher_teacherName = value;
                if(index == 0) this.courseTeacher_teacherNumber = '';
                else this.courseTeacher_teacherNumber = this.courseTeacherList[index-1].teacherNumber;
                this.showCourseTeacherPicker = false;
            },

            courseInfoAdd() {
                this.$router.push('/courseInfo/add');  //打开课程添加页面
            },

            queryCourseInfo() {
                this.page = 1;
                this.courseInfoList = [];
                this.pullFlag = false;
                this.showQueryCourseInfoForm = false;
                this.courseInfoQuery("QUERY");
            },

            courseInfoQuery(action) {
                queryCourseInfo(this.courseNumber,this.courseName,this.courseTeacher_teacherNumber,this.page).then(res => {
                    //this.courseInfoList.push(...res.data)
                    this.courseInfoList = this.courseInfoList.concat(res.data.list)
                    if(action == "QUERY") {
                        this.$notify.success('查询成功~');
                        this.$refs.scroll.refresh();
                    }
                    if(action == "UP") {  //上拉加载操作
                        if(res.data.list.length < 10){
                            this.$notify.error('没有更多数据了~')
                            return;
                        }
                        this.$refs.scroll.refresh();
                    }
                    if(action == "DOWN") {  //下拉刷新操作
                        this.$notify.success('刷新成功~');
                        this.$refs.scroll.finishPullDown();
                        this.$refs.scroll.refresh();
                        this.pullFlag = true
                    }
                })
            },

            courseInfoDetailClick(courseNumber) {
                this.$router.push('/courseInfo/detail/' + courseNumber); //跳转到课程详情页
            },
            courseInfoEditClick(courseNumber) {
                this.$router.push('/courseInfo/modify/' + courseNumber); //跳转到课程修改页
            },
            courseInfoDeleteClick(courseNumber) {
                this.$dialog.confirm({
                    title: '删除课程:' + courseNumber,
                    message: '确定删除吗？'
                }).then(() => {
                    deleteCourseInfo(courseNumber).then(res => {
                        console.log(res)
                        this.courseInfoList.forEach(function(courseInfo, index, arr) {
                            if(courseInfo.courseNumber == courseNumber) arr.splice(index, 1);
                        });
                    });
                });
            },

            pullingDown() {  //下拉刷新
                this.page = 1;
                this.courseInfoList = [];
                this.pullFlag = false;
                this.courseInfoQuery("DOWN");
            },

            pullingUp() {  //上拉加载
                console.log("上拉加载")
                this.page = this.page + 1;
                this.$refs.scroll.finishPullUp();
                this.courseInfoQuery("UP");
            },

            returnTop() {
                this.$refs.scroll.scrollTo(0, 0)
            },

            //滚动事件
            contentScroll(position) {
                this.isReturnTop = position.y < -800
            },
        }
    }
</script>

<style scoped>
    @import "../../assets/css/list_base.css";
    @import "CourseInfoList.css";
    #courseInfo {
        height: 100vh;
        width: 100vw;
        position: relative;
    }
</style>
