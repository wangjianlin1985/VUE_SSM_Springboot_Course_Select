<template>
    <div id="classInfo">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>班级详情</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <div class="main-box">
                <div class="info-bar">
                    <span class="title2">&nbsp;班级编号：</span>
                    <span class="title3">{{classNumber}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;班级名称：</span>
                    <span class="title3">{{className}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;所属专业：</span>
                    <span class="title3">{{classSpecialFieldNumber}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;成立日期：</span>
                    <span class="title3">{{classBirthDate}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;班主任：</span>
                    <span class="title3">{{classTeacherCharge}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;联系电话：</span>
                    <span class="title3">{{classTelephone}}</span>
                </div>
                <div class="info-bar">
                    <span class="title2">&nbsp;附加信息：</span>
                    <span class="title3">{{classMemo}}</span>
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
    import { getClassInfo } from "../../network/ClassInfo";

    export default {
        name: "ClassInfoDetail",
        components: {
            NavBar,
            Scroll,
        },
        data() {
            return {
                classNumber: "", //班级编号
                className: "", //班级名称
                classSpecialFieldNumber: "", //所属专业
                classBirthDate: "", //成立日期
                classTeacherCharge: "", //班主任
                classTelephone: "", //联系电话
                classMemo: "", //附加信息
            }
        },
        created() {
            this.classNumber = this.$route.params.classNumber; //接收到的班级编号
            //向后端请求单条班级记录
            getClassInfo(this.classNumber).then(res => {
                var classInfo = res.data;
                this.className = classInfo.className;
                this.classSpecialFieldNumber = classInfo.classSpecialFieldNumber;
                this.classBirthDate = classInfo.classBirthDate;
                this.classTeacherCharge = classInfo.classTeacherCharge;
                this.classTelephone = classInfo.classTelephone;
                this.classMemo = classInfo.classMemo;
            })
          document.getElementById("tabbar").style.display = "flex";
        },
        methods: {
            back() {
                //this.$router.replace('/classInfo/list')
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

    #classInfo {
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
