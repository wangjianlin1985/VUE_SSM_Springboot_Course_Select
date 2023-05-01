<template>
    <div id="student">
        <van-popup v-model="showQueryStudentForm"  closeable position="top" :style="{ height: 'auto' }">
            <div class="studentQueryForm">
                <h3>学生查询</h3>
                <van-field clearable v-model="user_name" label="手机号：" placeholder="请输入手机号" />
                <van-field clearable v-model="studentNumber" label="学号：" placeholder="请输入学号" />
                <van-field clearable v-model="studentName" label="姓名：" placeholder="请输入姓名" />
                <van-field readonly clickable label="所在班级" :value="studentClassNumber_className" right-icon="arrow-down" placeholder="选择所在班级" @click="showStudentClassNumberPicker = true" />
                <van-popup v-model="showStudentClassNumberPicker" position="bottom">
                    <van-picker show-toolbar :columns="studentClassNumberColumns" @cancel="showStudentClassNumberPicker = false" @confirm="onConfirmStudentClassNumber" />
                </van-popup>
                <van-field readonly label="出生日期" :value="studentBirthday"   placeholder="选择出生日期" @click="showStudentBirthdayPicker = true">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearStudentBirthday">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showStudentBirthdayPicker" v-model="picker_studentBirthday" type="date" @cancel="showStudentBirthdayPicker = false" title="选择出生日期" @confirm="onConfirmStudentBirthday" />
                <div class="queryBtn">
                    <van-button round  size= small type="primary" @click="queryStudent">查询</van-button>
                </div>
            </div>
        </van-popup>

        <nav-bar class="nav" ref="nav" >
            <template v-slot:left>
                <van-icon name="plus" size="25" v-show="false" @click.stop="studentAdd" />
                <van-icon name="arrow-left" v-show="false" size="25"  @click.stop="back" />
            </template>
            <template v-slot:center>学生列表</template>
            <template v-slot:right><van-icon name="search" size="25"  @click.stop="showQueryStudentForm=true" /></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" :isPullDown="true" @pullingDown="pullingDown" :isPullUp="true" @pullingUp="pullingUp"  @iscroll="contentScroll">
            <div class="ignore-pull-down1" ref="pull" v-show="pullFlag">下拉更新</div>
            <div class="ignore-pull-down2" ref="pull" v-show="!pullFlag">
                <span class="ball1"></span><span class="ball2"></span><span class="ball3"></span>
            </div>
            <div class="studentList">
                <template v-for="student in studentList">
                    <div class="studentItem" :key="student.user_name" @click.stop="studentDetailClick(`${student.user_name}`)">
                        <div class="content_view">
                            <div class="studentImg">
                                <img v-lazy="'/' + student.studentPhoto">
                            </div>
                            <div class="studentText">
                                <div>手机号：{{student.user_name}}</div>
                                <div>学号：{{student.studentNumber}}</div>
                                <div>姓名：{{student.studentName}}</div>
                                <div>性别：{{student.studentSex}}</div>
                                <div>所在班级：{{student.studentClassNumber.className}}</div>
                            </div>
                        </div>
                        <div class="opt_view" v-show="false">
                            <van-button round  size= small type="primary"  @click.stop="studentEditClick(`${student.user_name}`)" >编辑</van-button>
                            <van-button round  size= small type="warning" @click.stop="studentDeleteClick(`${student.user_name}`)" style="margin-left:10px;">删除</van-button>
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

    import {queryStudent,deleteStudent} from '../../network/Student'
    import { queryAllClassInfo } from '../../network/ClassInfo'
    export default {
        name: 'StudentList',
        components: {
            NavBar,
            Scroll,
            ReturnTop
        },
        data() {
            return {
                studentList: [],  //界面需要展示的学生列表数据
                user_name: "", //手机号查询关键字
                studentNumber: "", //学号查询关键字
                studentName: "", //姓名查询关键字

                studentClassNumberList:[], //所在班级列表
                studentClassNumber_classNumber: '',  //要查询的班级编号
                studentClassNumber_className: "不限制", //要查询的班级名称
                showStudentClassNumberPicker: false, //是否显示所在班级Picker
                studentClassNumberColumns: ['不限制'], //所在班级Picker的选项初始化

                studentBirthday: "",  //出生日期
                showStudentBirthdayPicker: false, //是否显示选择出生日期Picker
                picker_studentBirthday: new Date(), //出生日期Picker绑定的日期

                pullFlag: true,  //是否允许下拉刷新标志
                page: 1, //默认显示结果第1页
                showQueryStudentForm: false, //是否显示查询学生表单
                isReturnTop: false,
            }
        },

        created() {
            //初始化所在班级下拉框数据
            queryAllClassInfo().then(res => {
                this.studentClassNumberList = res.data;
                for(var i=0;i<this.studentClassNumberList.length;i++) {
                    this.studentClassNumberColumns.push(this.studentClassNumberList[i].className)
                }
            });
            //查询学生信息
            this.studentQuery("");
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
           back() {
                this.$router.go(-1);
           },

           onConfirmStudentClassNumber(value,index) {  //选中某个所在班级后响应
                this.studentClassNumber_className = value;
                if(index == 0) this.studentClassNumber_classNumber = '';
                else this.studentClassNumber_classNumber = this.studentClassNumberList[index-1].classNumber;
                this.showStudentClassNumberPicker = false;
            },

            onConfirmStudentBirthday(value) {  //选中某个出生日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.studentBirthday = year + "-" +  month + "-" + day ;
                this.showStudentBirthdayPicker = false;
            },
            clearStudentBirthday() {  //清空出生日期
               this.studentBirthday=''
               this.showStudentBirthdayPicker=false;
            },

            studentAdd() {
                this.$router.push('/student/add');  //打开学生添加页面
            },

            queryStudent() {
                this.page = 1;
                this.studentList = [];
                this.pullFlag = false;
                this.showQueryStudentForm = false;
                this.studentQuery("QUERY");
            },

            studentQuery(action) {
                queryStudent(this.user_name,this.studentNumber,this.studentName,this.studentClassNumber_classNumber,this.studentBirthday,this.page).then(res => {
                    //this.studentList.push(...res.data)
                    this.studentList = this.studentList.concat(res.data.list)
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

            studentDetailClick(user_name) {
                this.$router.push('/student/detail/' + user_name); //跳转到学生详情页
            },
            studentEditClick(user_name) {
                this.$router.push('/student/modify/' + user_name); //跳转到学生修改页
            },
            studentDeleteClick(user_name) {
                this.$dialog.confirm({
                    title: '删除学生:' + user_name,
                    message: '确定删除吗？'
                }).then(() => {
                    deleteStudent(user_name).then(res => {
                        console.log(res)
                        this.studentList.forEach(function(student, index, arr) {
                            if(student.user_name == user_name) arr.splice(index, 1);
                        });
                    });
                });
            },

            pullingDown() {  //下拉刷新
                this.page = 1;
                this.studentList = [];
                this.pullFlag = false;
                this.studentQuery("DOWN");
            },

            pullingUp() {  //上拉加载
                console.log("上拉加载")
                this.page = this.page + 1;
                this.$refs.scroll.finishPullUp();
                this.studentQuery("UP");
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
    @import "StudentList.css";
    #student {
        height: 100vh;
        width: 100vw;
        position: relative;
    }
</style>
