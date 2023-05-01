<template>
    <div id="courseInfo">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>课程详情</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <div class="main-box">
                <div class="info-bar">
                    <span class="title2">&nbsp;课程编号：</span>
                    <span class="title3">{{courseNumber}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;课程名称：</span>
                    <span class="title3">{{courseName}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;上课老师：</span>
                    <span class="title3">{{courseTeacher_teacherName}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;上课时间：</span>
                    <span class="title3">{{courseTime}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;上课地点：</span>
                    <span class="title3">{{coursePlace}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;课程学分：</span>
                    <span class="title3">{{courseScore}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;课程简介:</span>
                    <span class="desc" v-html="courseMemo"> </span>
                </div>
            </div>
            <div style="margin: 16px;">
                <van-button round block color="green" type="primary" @click.stop="selectCourse();" style="margin-bottom:15px;">我要选课</van-button>

                <van-button round block type="info" native-type="submit" @click.stop="back">返回</van-button>
            
            </div>
            

        </scroll>
    </div>
</template>

<script>
    import NavBar from '../../components/common/navbar/NavBar'
    import Scroll from '../../components/common/scroll/Scroll'
    import { getCourseInfo } from "../../network/CourseInfo";
    import { studentSelectCourse} from "../../network/CourseSelect";

    export default {
        name: "CourseInfoDetail",
        components: {
            NavBar,
            Scroll,
        },
        data() {
            return {
                courseNumber: "", //课程编号
                courseName: "", //课程名称
                courseTeacher_teacherName: "", //上课老师
                courseTime: "", //上课时间
                coursePlace: "", //上课地点
                courseScore: "", //课程学分
                courseMemo: "", //课程简介
            }
        },
        created() {
            this.courseNumber = this.$route.params.courseNumber; //接收到的课程编号
            //向后端请求单条课程记录
            getCourseInfo(this.courseNumber).then(res => {
                var courseInfo = res.data;
                this.courseName = courseInfo.courseName;
                this.courseTeacher_teacherName = courseInfo.courseTeacher.teacherName;
                this.courseTime = courseInfo.courseTime;
                this.coursePlace = courseInfo.coursePlace;
                this.courseScore = courseInfo.courseScore;
                this.courseMemo = courseInfo.courseMemo;
            })
          document.getElementById("tabbar").style.display = "flex";
        },
        methods: {
            back() {
                //this.$router.replace('/courseInfo/list')
                this.$router.go(-1);
            }, 
            selectCourse() {  
                var studentNumber = localStorage.getItem("username");
                var courseNumber = this.courseNumber;  
                studentSelectCourse(studentNumber,courseNumber).then(res => { 
                    this.$notify.success('选课成功~')
                    setTimeout(()=>{
                        this.$router.go(-1);
                    },200)
                }).catch(error => { this.$notify.error(error)}) 
            }
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

    .header {
        background-color: #fff;
        position: relative;
        padding: 0 15px;
        padding-bottom: 10px;
        overflow-x: scroll;
        box-shadow: 0 1px 6px rgba(0, 0, 0, .08);
    }

    .header p{
        margin-top: 40px;
        color: #3c3c3c;
        line-height: 25px;
        text-indent: 25px;
    }

    .main-box {
        background-color: #fff;
        margin: 10px 0;
        box-shadow: 0 1px 6px rgba(0, 0, 0, .08);
    }

    .info-bar {
        height: auto;
        border-bottom: 1px solid #f3f3f3;
        padding: 0 15px;
        display: block;
        clear:both;
    }

    .title2 {
        width:35%;
        line-height: 46px;
        font-weight: 600;
        font-family: 'icomoon';
    }

    .title3 {
        width: auto;
        height: 100%;
        text-align: right;
        line-height: 46px;
        color: #3c3c3c;
        float: right;
    }

    .desc {
        width: 100%;
        line-height: 25px;
        color: #3c3c3c;
        clear:both;
    }

    .avatar {
        width: 100px;
        height: 100px;
        background-color: #f3f3f3;
        border-radius: 3px;
    }
</style>
