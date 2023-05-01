<template>
    <div id="courseSelect">
        <van-popup v-model="showQueryCourseSelectForm"  closeable position="top" :style="{ height: 'auto' }">
            <div class="courseSelectQueryForm">
                <h3>学生选课查询</h3>
                <van-field readonly label="选课时间" :value="selectTime"   placeholder="选择选课时间" @click="showSelectTimePicker = true">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearSelectTime">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showSelectTimePicker" v-model="picker_selectTime" type="date" @cancel="showSelectTimePicker = false" title="选择选课时间" @confirm="onConfirmSelectTime" />
                <van-field v-show="false" readonly clickable label="选课学生" :value="studentNumber_studentName" right-icon="arrow-down" placeholder="选择选课学生" @click="showStudentNumberPicker = true" />
                <van-popup v-model="showStudentNumberPicker" position="bottom">
                    <van-picker show-toolbar :columns="studentNumberColumns" @cancel="showStudentNumberPicker = false" @confirm="onConfirmStudentNumber" />
                </van-popup>
                <van-field readonly clickable label="选择课程" :value="courseNumber_courseName" right-icon="arrow-down" placeholder="选择选择课程" @click="showCourseNumberPicker = true" />
                <van-popup v-model="showCourseNumberPicker" position="bottom">
                    <van-picker show-toolbar :columns="courseNumberColumns" @cancel="showCourseNumberPicker = false" @confirm="onConfirmCourseNumber" />
                </van-popup>
                <div class="queryBtn">
                    <van-button round  size= small type="primary" @click="queryCourseSelect">查询</van-button>
                </div>
            </div>
        </van-popup>

        <nav-bar class="nav" ref="nav" >
            <template v-slot:left>
                <van-icon name="plus" size="25" v-show="false" @click.stop="courseSelectAdd" />
                <van-icon name="arrow-left" v-show="false" size="25"  @click.stop="back" />
            </template>
            <template v-slot:center>学生选课列表</template>
            <template v-slot:right><van-icon name="search" size="25"  @click.stop="showQueryCourseSelectForm=true" /></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" :isPullDown="true" @pullingDown="pullingDown" :isPullUp="true" @pullingUp="pullingUp"  @iscroll="contentScroll">
            <div class="ignore-pull-down1" ref="pull" v-show="pullFlag">下拉更新</div>
            <div class="ignore-pull-down2" ref="pull" v-show="!pullFlag">
                <span class="ball1"></span><span class="ball2"></span><span class="ball3"></span>
            </div>
            <div class="courseSelectList">
                <template v-for="courseSelect in courseSelectList">
                    <div class="courseSelectItem" :key="courseSelect.selectId" @click.stop="courseSelectDetailClick(`${courseSelect.selectId}`)">
                        <div class="content_view">
                            <div class="courseSelectText">
                                <div>选择课程：{{courseSelect.courseNumber.courseName}}</div>
                                <div>选课学生：{{courseSelect.studentNumber.studentName}}</div> 
                                <div>选课时间：{{courseSelect.selectTime}}</div>
                            </div>
                        </div>
                        <div class="opt_view">
                            <van-button v-show="false" round  size= small type="primary"  @click.stop="courseSelectEditClick(`${courseSelect.selectId}`)" >编辑</van-button>
                            <van-button round  size= small type="warning" @click.stop="courseSelectDeleteClick(`${courseSelect.selectId}`)" style="margin-left:10px;">退选</van-button>
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

    import {queryCourseSelect,deleteCourseSelect} from '../../network/CourseSelect'
    import { queryAllCourseInfo } from '../../network/CourseInfo'
    import { queryAllStudent } from '../../network/Student'
    export default {
        name: 'CourseSelectList',
        components: {
            NavBar,
            Scroll,
            ReturnTop
        },
        data() {
            return {
                courseSelectList: [],  //界面需要展示的学生选课列表数据

                selectTime: "",  //选课时间
                showSelectTimePicker: false, //是否显示选择选课时间Picker
                picker_selectTime: new Date(), //选课时间Picker绑定的日期

                studentNumberList:[], //选课学生列表
                studentNumber_user_name: '',  //要查询的手机号
                studentNumber_studentName: "不限制", //要查询的姓名
                showStudentNumberPicker: false, //是否显示选课学生Picker
                studentNumberColumns: ['不限制'], //选课学生Picker的选项初始化

                courseNumberList:[], //选择课程列表
                courseNumber_courseNumber: '',  //要查询的课程编号
                courseNumber_courseName: "不限制", //要查询的课程名称
                showCourseNumberPicker: false, //是否显示选择课程Picker
                courseNumberColumns: ['不限制'], //选择课程Picker的选项初始化

                pullFlag: true,  //是否允许下拉刷新标志
                page: 1, //默认显示结果第1页
                showQueryCourseSelectForm: false, //是否显示查询学生选课表单
                isReturnTop: false,
            }
        },

        created() {
            //初始化选课学生下拉框数据
            queryAllStudent().then(res => {
                this.studentNumberList = res.data;
                for(var i=0;i<this.studentNumberList.length;i++) {
                    this.studentNumberColumns.push(this.studentNumberList[i].studentName)
                }
            });
            //初始化选择课程下拉框数据
            queryAllCourseInfo().then(res => {
                this.courseNumberList = res.data;
                for(var i=0;i<this.courseNumberList.length;i++) {
                    this.courseNumberColumns.push(this.courseNumberList[i].courseName)
                }
            });
            //查询学生选课信息
            this.courseSelectQuery("");
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
           back() {
                this.$router.go(-1);
           },

            onConfirmSelectTime(value) {  //选中某个选课时间后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.selectTime = year + "-" +  month + "-" + day ;
                this.showSelectTimePicker = false;
            },
            clearSelectTime() {  //清空选课时间
               this.selectTime=''
               this.showSelectTimePicker=false;
            },

           onConfirmStudentNumber(value,index) {  //选中某个选课学生后响应
                this.studentNumber_studentName = value;
                if(index == 0) this.studentNumber_user_name = '';
                else this.studentNumber_user_name = this.studentNumberList[index-1].user_name;
                this.showStudentNumberPicker = false;
            },

           onConfirmCourseNumber(value,index) {  //选中某个选择课程后响应
                this.courseNumber_courseName = value;
                if(index == 0) this.courseNumber_courseNumber = '';
                else this.courseNumber_courseNumber = this.courseNumberList[index-1].courseNumber;
                this.showCourseNumberPicker = false;
            },

            courseSelectAdd() {
                this.$router.push('/courseSelect/add');  //打开学生选课添加页面
            },

            queryCourseSelect() {
                this.page = 1;
                this.courseSelectList = [];
                this.pullFlag = false;
                this.showQueryCourseSelectForm = false;
                this.courseSelectQuery("QUERY");
            },

            courseSelectQuery(action) {
                queryCourseSelect(this.selectTime,this.studentNumber_user_name,this.courseNumber_courseNumber,this.page).then(res => {
                    //this.courseSelectList.push(...res.data)
                    this.courseSelectList = this.courseSelectList.concat(res.data.list)
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

            courseSelectDetailClick(selectId) {
                this.$router.push('/courseSelect/detail/' + selectId); //跳转到学生选课详情页
            },
            courseSelectEditClick(selectId) {
                this.$router.push('/courseSelect/modify/' + selectId); //跳转到学生选课修改页
            },
            courseSelectDeleteClick(selectId) {
                this.$dialog.confirm({
                    title: '删除学生选课:' + selectId,
                    message: '确定删除吗？'
                }).then(() => {
                    deleteCourseSelect(selectId).then(res => {
                        console.log(res)
                        this.courseSelectList.forEach(function(courseSelect, index, arr) {
                            if(courseSelect.selectId == selectId) arr.splice(index, 1);
                        });
                    });
                });
            },

            pullingDown() {  //下拉刷新
                this.page = 1;
                this.courseSelectList = [];
                this.pullFlag = false;
                this.courseSelectQuery("DOWN");
            },

            pullingUp() {  //上拉加载
                console.log("上拉加载")
                this.page = this.page + 1;
                this.$refs.scroll.finishPullUp();
                this.courseSelectQuery("UP");
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
    @import "CourseSelectList.css";
    #courseSelect {
        height: 100vh;
        width: 100vw;
        position: relative;
    }
</style>
