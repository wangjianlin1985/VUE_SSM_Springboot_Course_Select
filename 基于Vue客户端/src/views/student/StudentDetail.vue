<template>
    <div id="student">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>学生详情</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <div class="main-box">
                <div class="info-bar">
                    <span class="title2">&nbsp;手机号：</span>
                    <span class="title3">{{user_name}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;学号：</span>
                    <span class="title3">{{studentNumber}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;登录密码：</span>
                    <span class="title3">{{password}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;姓名：</span>
                    <span class="title3">{{studentName}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;性别：</span>
                    <span class="title3">{{studentSex}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;所在班级：</span>
                    <span class="title3">{{studentClassNumber_className}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;出生日期：</span>
                    <span class="title3">{{studentBirthday}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;政治面貌：</span>
                    <span class="title3">{{studentState}}</span>
                </div>
                <div class="info-bar" style="height:140px;">
                    <span class="title2">&nbsp;学生照片：</span>
                    <span class="title3"> <img class="avatar" :src="studentPhotoUrl" style="width:auto;height: 100%;" /></span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;联系电话：</span>
                    <span class="title3">{{studentTelephone}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;学生邮箱：</span>
                    <span class="title3">{{studentEmail}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;联系qq：</span>
                    <span class="title3">{{studentQQ}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;家庭地址：</span>
                    <span class="title3">{{studentAddress}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;附加信息：</span>
                    <span class="title3">{{studentMemo}}</span>
                </div>
            </div>
            <div style="margin: 16px;">
                <van-button round block type="info" native-type="submit" @click.stop="back">返回</van-button>
            </div>

        </scroll>
    </div>
</template>

<script>
    import NavBar from '../../components/common/navbar/NavBar'
    import Scroll from '../../components/common/scroll/Scroll'
    import { getStudent } from "../../network/Student";

    export default {
        name: "StudentDetail",
        components: {
            NavBar,
            Scroll,
        },
        data() {
            return {
                user_name: "", //手机号
                studentNumber: "", //学号
                password: "", //登录密码
                studentName: "", //姓名
                studentSex: "", //性别
                studentClassNumber_className: "", //所在班级
                studentBirthday: "", //出生日期
                studentState: "", //政治面貌
                studentPhotoUrl: '/upload/NoImage.jpg', //学生照片路径
                studentTelephone: "", //联系电话
                studentEmail: "", //学生邮箱
                studentQQ: "", //联系qq
                studentAddress: "", //家庭地址
                studentMemo: "", //附加信息
            }
        },
        created() {
            this.user_name = this.$route.params.user_name; //接收到的手机号
            //向后端请求单条学生记录
            getStudent(this.user_name).then(res => {
                var student = res.data;
                this.studentNumber = student.studentNumber;
                this.password = student.password;
                this.studentName = student.studentName;
                this.studentSex = student.studentSex;
                this.studentClassNumber_className = student.studentClassNumber.className;
                this.studentBirthday = student.studentBirthday;
                this.studentState = student.studentState;
                this.studentPhotoUrl = "/" + student.studentPhoto;
                this.studentTelephone = student.studentTelephone;
                this.studentEmail = student.studentEmail;
                this.studentQQ = student.studentQQ;
                this.studentAddress = student.studentAddress;
                this.studentMemo = student.studentMemo;
            })
          document.getElementById("tabbar").style.display = "flex";
        },
        methods: {
            back() {
                //this.$router.replace('/student/list')
                this.$router.go(-1);
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

    #student {
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
