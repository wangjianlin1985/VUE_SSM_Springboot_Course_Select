<template>
    <div id="scoreInfo">
        <van-popup v-model="showQueryScoreInfoForm"  closeable position="top" :style="{ height: 'auto' }">
            <div class="scoreInfoQueryForm">
                <h3>成绩信息查询</h3>
                <van-field v-show="false" readonly clickable label="学生" :value="studentNumber_studentName" right-icon="arrow-down" placeholder="选择学生" @click="showStudentNumberPicker = true" />
                <van-popup v-model="showStudentNumberPicker" position="bottom">
                    <van-picker show-toolbar :columns="studentNumberColumns" @cancel="showStudentNumberPicker = false" @confirm="onConfirmStudentNumber" />
                </van-popup>
                <van-field readonly clickable label="课程" :value="courseNumber_courseName" right-icon="arrow-down" placeholder="选择课程" @click="showCourseNumberPicker = true" />
                <van-popup v-model="showCourseNumberPicker" position="bottom">
                    <van-picker show-toolbar :columns="courseNumberColumns" @cancel="showCourseNumberPicker = false" @confirm="onConfirmCourseNumber" />
                </van-popup>
                <div class="queryBtn">
                    <van-button round  size= small type="primary" @click="queryScoreInfo">查询</van-button>
                </div>
            </div>
        </van-popup>

        <nav-bar class="nav" ref="nav" >
            <template v-slot:left>
                <van-icon name="plus" size="25" v-show="false" @click.stop="scoreInfoAdd" />
                <van-icon name="arrow-left" v-show="false" size="25"  @click.stop="back" />
            </template>
            <template v-slot:center>成绩信息列表</template>
            <template v-slot:right><van-icon name="search" size="25"  @click.stop="showQueryScoreInfoForm=true" /></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" :isPullDown="true" @pullingDown="pullingDown" :isPullUp="true" @pullingUp="pullingUp"  @iscroll="contentScroll">
            <div class="ignore-pull-down1" ref="pull" v-show="pullFlag">下拉更新</div>
            <div class="ignore-pull-down2" ref="pull" v-show="!pullFlag">
                <span class="ball1"></span><span class="ball2"></span><span class="ball3"></span>
            </div>
            <div class="scoreInfoList">
                <template v-for="scoreInfo in scoreInfoList">
                    <div class="scoreInfoItem" :key="scoreInfo.scoreId" @click.stop="scoreInfoDetailClick(`${scoreInfo.scoreId}`)">
                        <div class="content_view">
                            <div class="scoreInfoText">
                                <div>学生：{{scoreInfo.studentNumber.studentName}}</div>
                                <div>课程：{{scoreInfo.courseNumber.courseName}}</div>
                                <div>成绩得分：{{scoreInfo.scoreValue}}</div>
                            </div>
                        </div>
                        <div class="opt_view" v-show="false">
                            <van-button round  size= small type="primary"  @click.stop="scoreInfoEditClick(`${scoreInfo.scoreId}`)" >编辑</van-button>
                            <van-button round  size= small type="warning" @click.stop="scoreInfoDeleteClick(`${scoreInfo.scoreId}`)" style="margin-left:10px;">删除</van-button>
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

    import {queryScoreInfo,deleteScoreInfo} from '../../network/ScoreInfo'
    import { queryAllCourseInfo } from '../../network/CourseInfo'
    import { queryAllStudent } from '../../network/Student'
    export default {
        name: 'ScoreInfoList',
        components: {
            NavBar,
            Scroll,
            ReturnTop
        },
        data() {
            return {
                scoreInfoList: [],  //界面需要展示的成绩信息列表数据

                studentNumberList:[], //学生列表
                studentNumber_user_name: '',  //要查询的手机号
                studentNumber_studentName: "不限制", //要查询的姓名
                showStudentNumberPicker: false, //是否显示学生Picker
                studentNumberColumns: ['不限制'], //学生Picker的选项初始化

                courseNumberList:[], //课程列表
                courseNumber_courseNumber: '',  //要查询的课程编号
                courseNumber_courseName: "不限制", //要查询的课程名称
                showCourseNumberPicker: false, //是否显示课程Picker
                courseNumberColumns: ['不限制'], //课程Picker的选项初始化

                pullFlag: true,  //是否允许下拉刷新标志
                page: 1, //默认显示结果第1页
                showQueryScoreInfoForm: false, //是否显示查询成绩信息表单
                isReturnTop: false,
            }
        },

        created() {
            //初始化学生下拉框数据
            queryAllStudent().then(res => {
                this.studentNumberList = res.data;
                for(var i=0;i<this.studentNumberList.length;i++) {
                    this.studentNumberColumns.push(this.studentNumberList[i].studentName)
                }
            });
            //初始化课程下拉框数据
            queryAllCourseInfo().then(res => {
                this.courseNumberList = res.data;
                for(var i=0;i<this.courseNumberList.length;i++) {
                    this.courseNumberColumns.push(this.courseNumberList[i].courseName)
                }
            });
            //查询成绩信息信息
            this.scoreInfoQuery("");
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
           back() {
                this.$router.go(-1);
           },

           onConfirmStudentNumber(value,index) {  //选中某个学生后响应
                this.studentNumber_studentName = value;
                if(index == 0) this.studentNumber_user_name = '';
                else this.studentNumber_user_name = this.studentNumberList[index-1].user_name;
                this.showStudentNumberPicker = false;
            },

           onConfirmCourseNumber(value,index) {  //选中某个课程后响应
                this.courseNumber_courseName = value;
                if(index == 0) this.courseNumber_courseNumber = '';
                else this.courseNumber_courseNumber = this.courseNumberList[index-1].courseNumber;
                this.showCourseNumberPicker = false;
            },

            scoreInfoAdd() {
                this.$router.push('/scoreInfo/add');  //打开成绩信息添加页面
            },

            queryScoreInfo() {
                this.page = 1;
                this.scoreInfoList = [];
                this.pullFlag = false;
                this.showQueryScoreInfoForm = false;
                this.scoreInfoQuery("QUERY");
            },

            scoreInfoQuery(action) {
                queryScoreInfo(this.studentNumber_user_name,this.courseNumber_courseNumber,this.page).then(res => {
                    //this.scoreInfoList.push(...res.data)
                    this.scoreInfoList = this.scoreInfoList.concat(res.data.list)
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

            scoreInfoDetailClick(scoreId) {
                this.$router.push('/scoreInfo/detail/' + scoreId); //跳转到成绩信息详情页
            },
            scoreInfoEditClick(scoreId) {
                this.$router.push('/scoreInfo/modify/' + scoreId); //跳转到成绩信息修改页
            },
            scoreInfoDeleteClick(scoreId) {
                this.$dialog.confirm({
                    title: '删除成绩信息:' + scoreId,
                    message: '确定删除吗？'
                }).then(() => {
                    deleteScoreInfo(scoreId).then(res => {
                        console.log(res)
                        this.scoreInfoList.forEach(function(scoreInfo, index, arr) {
                            if(scoreInfo.scoreId == scoreId) arr.splice(index, 1);
                        });
                    });
                });
            },

            pullingDown() {  //下拉刷新
                this.page = 1;
                this.scoreInfoList = [];
                this.pullFlag = false;
                this.scoreInfoQuery("DOWN");
            },

            pullingUp() {  //上拉加载
                console.log("上拉加载")
                this.page = this.page + 1;
                this.$refs.scroll.finishPullUp();
                this.scoreInfoQuery("UP");
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
    @import "ScoreInfoList.css";
    #scoreInfo {
        height: 100vh;
        width: 100vw;
        position: relative;
    }
</style>
