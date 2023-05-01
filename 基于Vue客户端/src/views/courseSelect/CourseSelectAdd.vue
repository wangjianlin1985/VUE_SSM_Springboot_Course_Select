<template>
    <div id="courseSelect">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>添加学生选课</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <van-form @submit="onSubmit">
                <van-field v-show="false" clearable name="courseSelect.studentNumber.user_name" v-model="studentNumber_user_name" label="选课学生：" />
                <van-field readonly clickable label="选课学生" :value="studentNumber_studentName"  right-icon="arrow-down" placeholder="选择选课学生" @click="showStudentNumberPicker = true" />
                <van-picker v-show="showStudentNumberPicker" show-toolbar :columns="studentNumberColumns" :default-index="1" @cancel="showStudentNumberPicker = false" @confirm="onConfirmStudentNumber" />
                <van-field v-show="false" clearable name="courseSelect.courseNumber.courseNumber" v-model="courseNumber_courseNumber" label="选择课程：" />
                <van-field readonly clickable label="选择课程" :value="courseNumber_courseName"  right-icon="arrow-down" placeholder="选择选择课程" @click="showCourseNumberPicker = true" />
                <van-picker v-show="showCourseNumberPicker" show-toolbar :columns="courseNumberColumns" :default-index="1" @cancel="showCourseNumberPicker = false" @confirm="onConfirmCourseNumber" />
                <van-field readonly name="courseSelect.selectTime" label="选课时间" :value="selectTime"   placeholder="选择选课时间" @click="showSelectTimePicker = true" :rules="[{ required: true, message: '选课时间' }]">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearSelectTime">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showSelectTimePicker"  v-model="picker_selectTime" type="date" @cancel="showSelectTimePicker = false" title="选择选课时间" @confirm="onConfirmSelectTime" />
                <div style="margin: 16px;">
                    <van-button round block type="info" native-type="submit">提交</van-button>
                </div>
            </van-form>
        </scroll>
    </div>
</template>

<script>
    import NavBar from '../../components/common/navbar/NavBar'
    import Scroll from '../../components/common/scroll/Scroll'
    import { queryAllCourseInfo } from '../../network/CourseInfo'
    import { queryAllStudent } from '../../network/Student'
    import { addCourseSelect} from "../../network/CourseSelect";

    export default {
        name: "CourseSelectAdd",
        components: {
            NavBar,
            Scroll,
        },
        data() {
            return {
                studentNumberList: [], //选课学生列表
                studentNumber_user_name: '', //手机号
                studentNumber_studentName: "", //姓名
                showStudentNumberPicker: false, //是否显示选课学生Picker
                studentNumberColumns: [], //选课学生Picker的选项
                courseNumberList: [], //选择课程列表
                courseNumber_courseNumber: '', //课程编号
                courseNumber_courseName: "", //课程名称
                showCourseNumberPicker: false, //是否显示选择课程Picker
                courseNumberColumns: [], //选择课程Picker的选项
                showSelectTimePicker:false, //是否显示选课时间Picker
                selectTime: '', //选课时间
                picker_selectTime: new Date(), //选课时间Picker的日期
            }
        },

        mounted() {

        },

        created() {
            /*查询所有选课学生初始化下拉Picker*/
            queryAllStudent().then(res => {
                this.studentNumberList = res.data;
                for(var i=0;i<this.studentNumberList.length;i++) {
                    this.studentNumberColumns.push(this.studentNumberList[i].studentName)
                }
                if(this.studentNumberList.length > 0) {
                    var firstStudentNumber = this.studentNumberList[0]; //默认选中第一个选项
                    this.studentNumber_studentName = firstStudentNumber.studentName;
                    this.studentNumber_user_name = firstStudentNumber.user_name;
                }
            });

            /*查询所有选择课程初始化下拉Picker*/
            queryAllCourseInfo().then(res => {
                this.courseNumberList = res.data;
                for(var i=0;i<this.courseNumberList.length;i++) {
                    this.courseNumberColumns.push(this.courseNumberList[i].courseName)
                }
                if(this.courseNumberList.length > 0) {
                    var firstCourseNumber = this.courseNumberList[0]; //默认选中第一个选项
                    this.courseNumber_courseName = firstCourseNumber.courseName;
                    this.courseNumber_courseNumber = firstCourseNumber.courseNumber;
                }
            });

            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
            back() {
                this.$router.go(-1);
            },

            onConfirmStudentNumber(value,index) {
                this.studentNumber_studentName = value; //选中某个选课学生后响应
                this.studentNumber_user_name = this.studentNumberList[index].user_name;
                this.showStudentNumberPicker = false;
            },
            onConfirmCourseNumber(value,index) {
                this.courseNumber_courseName = value; //选中某个选择课程后响应
                this.courseNumber_courseNumber = this.courseNumberList[index].courseNumber;
                this.showCourseNumberPicker = false;
            },
            onSubmit(courseSelect) {  //向后台请求添加学生选课
                addCourseSelect(courseSelect).then(res => {
                    console.log(res)
                    this.$notify.success('添加成功~')
                    setTimeout(()=>{
                        this.$router.go(-1);
                    },200)
                }).catch(error => { this.$notify.error(error)})
            },
            onConfirmSelectTime(value) {  //选中某个选课时间后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.selectTime = year + "-" +  month + "-" + day ;
                this.showSelectTimePicker = false;
            },
            clearSelectTime() { //清空选课时间
                this.selectTime='';
                this.showSelectTimePicker=false;
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

    #courseSelect {
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

</style>
