<template>
    <div id="teacher">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>教师详情</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <div class="main-box">
                <div class="info-bar">
                    <span class="title2">&nbsp;教师编号：</span>
                    <span class="title3">{{teacherNumber}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;教师姓名：</span>
                    <span class="title3">{{teacherName}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;性别：</span>
                    <span class="title3">{{teacherSex}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;出生日期：</span>
                    <span class="title3">{{teacherBirthday}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;入职日期：</span>
                    <span class="title3">{{teacherArriveDate}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;身份证号：</span>
                    <span class="title3">{{teacherCardNumber}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;联系电话：</span>
                    <span class="title3">{{teacherPhone}}</span>
                </div>
                <div class="info-bar" style="height:140px;">
                    <span class="title2">&nbsp;教师照片：</span>
                    <span class="title3"> <img class="avatar" :src="teacherPhotoUrl" style="width:auto;height: 100%;" /></span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;家庭地址：</span>
                    <span class="title3">{{teacherAddress}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;教师简介:</span>
                    <span class="desc" v-html="teacherMemo"> </span>
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
    import { getTeacher } from "../../network/Teacher";

    export default {
        name: "TeacherDetail",
        components: {
            NavBar,
            Scroll,
        },
        data() {
            return {
                teacherNumber: "", //教师编号
                teacherName: "", //教师姓名
                teacherSex: "", //性别
                teacherBirthday: "", //出生日期
                teacherArriveDate: "", //入职日期
                teacherCardNumber: "", //身份证号
                teacherPhone: "", //联系电话
                teacherPhotoUrl: '/upload/NoImage.jpg', //教师照片路径
                teacherAddress: "", //家庭地址
                teacherMemo: "", //教师简介
            }
        },
        created() {
            this.teacherNumber = this.$route.params.teacherNumber; //接收到的教师编号
            //向后端请求单条教师记录
            getTeacher(this.teacherNumber).then(res => {
                var teacher = res.data;
                this.teacherName = teacher.teacherName;
                this.teacherSex = teacher.teacherSex;
                this.teacherBirthday = teacher.teacherBirthday;
                this.teacherArriveDate = teacher.teacherArriveDate;
                this.teacherCardNumber = teacher.teacherCardNumber;
                this.teacherPhone = teacher.teacherPhone;
                this.teacherPhotoUrl = "/" + teacher.teacherPhoto;
                this.teacherAddress = teacher.teacherAddress;
                this.teacherMemo = teacher.teacherMemo;
            })
          document.getElementById("tabbar").style.display = "flex";
        },
        methods: {
            back() {
                //this.$router.replace('/teacher/list')
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

    #teacher {
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
