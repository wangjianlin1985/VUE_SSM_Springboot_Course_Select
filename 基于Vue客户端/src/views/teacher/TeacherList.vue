<template>
    <div id="teacher">
        <van-popup v-model="showQueryTeacherForm"  closeable position="top" :style="{ height: 'auto' }">
            <div class="teacherQueryForm">
                <h3>教师查询</h3>
                <van-field clearable v-model="teacherNumber" label="教师编号：" placeholder="请输入教师编号" />
                <van-field clearable v-model="teacherName" label="教师姓名：" placeholder="请输入教师姓名" />
                <van-field readonly label="出生日期" :value="teacherBirthday"   placeholder="选择出生日期" @click="showTeacherBirthdayPicker = true">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearTeacherBirthday">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showTeacherBirthdayPicker" v-model="picker_teacherBirthday" type="date" @cancel="showTeacherBirthdayPicker = false" title="选择出生日期" @confirm="onConfirmTeacherBirthday" />
                <van-field readonly label="入职日期" :value="teacherArriveDate"   placeholder="选择入职日期" @click="showTeacherArriveDatePicker = true">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearTeacherArriveDate">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showTeacherArriveDatePicker" v-model="picker_teacherArriveDate" type="date" @cancel="showTeacherArriveDatePicker = false" title="选择入职日期" @confirm="onConfirmTeacherArriveDate" />
                <div class="queryBtn">
                    <van-button round  size= small type="primary" @click="queryTeacher">查询</van-button>
                </div>
            </div>
        </van-popup>

        <nav-bar class="nav" ref="nav" >
            <template v-slot:left>
                <van-icon name="plus" size="25" v-show="false" @click.stop="teacherAdd" />
                <van-icon name="arrow-left" v-show="false" size="25"  @click.stop="back" />
            </template>
            <template v-slot:center>教师列表</template>
            <template v-slot:right><van-icon name="search" size="25"  @click.stop="showQueryTeacherForm=true" /></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" :isPullDown="true" @pullingDown="pullingDown" :isPullUp="true" @pullingUp="pullingUp"  @iscroll="contentScroll">
            <div class="ignore-pull-down1" ref="pull" v-show="pullFlag">下拉更新</div>
            <div class="ignore-pull-down2" ref="pull" v-show="!pullFlag">
                <span class="ball1"></span><span class="ball2"></span><span class="ball3"></span>
            </div>
            <div class="teacherList">
                <template v-for="teacher in teacherList">
                    <div class="teacherItem" :key="teacher.teacherNumber" @click.stop="teacherDetailClick(`${teacher.teacherNumber}`)">
                        <div class="content_view">
                            <div class="teacherImg">
                                <img v-lazy="'/' + teacher.teacherPhoto">
                            </div>
                            <div class="teacherText">
                                <div>教师编号：{{teacher.teacherNumber}}</div>
                                <div>教师姓名：{{teacher.teacherName}}</div>
                                <div>性别：{{teacher.teacherSex}}</div>
                                <div>出生日期：{{teacher.teacherBirthday}}</div>
                            </div>
                        </div>
                        <div class="opt_view" v-show="false">
                            <van-button round  size= small type="primary"  @click.stop="teacherEditClick(`${teacher.teacherNumber}`)" >编辑</van-button>
                            <van-button round  size= small type="warning" @click.stop="teacherDeleteClick(`${teacher.teacherNumber}`)" style="margin-left:10px;">删除</van-button>
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

    import {queryTeacher,deleteTeacher} from '../../network/Teacher'
    export default {
        name: 'TeacherList',
        components: {
            NavBar,
            Scroll,
            ReturnTop
        },
        data() {
            return {
                teacherList: [],  //界面需要展示的教师列表数据
                teacherNumber: "", //教师编号查询关键字
                teacherName: "", //教师姓名查询关键字

                teacherBirthday: "",  //出生日期
                showTeacherBirthdayPicker: false, //是否显示选择出生日期Picker
                picker_teacherBirthday: new Date(), //出生日期Picker绑定的日期

                teacherArriveDate: "",  //入职日期
                showTeacherArriveDatePicker: false, //是否显示选择入职日期Picker
                picker_teacherArriveDate: new Date(), //入职日期Picker绑定的日期

                pullFlag: true,  //是否允许下拉刷新标志
                page: 1, //默认显示结果第1页
                showQueryTeacherForm: false, //是否显示查询教师表单
                isReturnTop: false,
            }
        },

        created() {
            //查询教师信息
            this.teacherQuery("");
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
           back() {
                this.$router.go(-1);
           },

            onConfirmTeacherBirthday(value) {  //选中某个出生日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.teacherBirthday = year + "-" +  month + "-" + day ;
                this.showTeacherBirthdayPicker = false;
            },
            clearTeacherBirthday() {  //清空出生日期
               this.teacherBirthday=''
               this.showTeacherBirthdayPicker=false;
            },

            onConfirmTeacherArriveDate(value) {  //选中某个入职日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.teacherArriveDate = year + "-" +  month + "-" + day ;
                this.showTeacherArriveDatePicker = false;
            },
            clearTeacherArriveDate() {  //清空入职日期
               this.teacherArriveDate=''
               this.showTeacherArriveDatePicker=false;
            },

            teacherAdd() {
                this.$router.push('/teacher/add');  //打开教师添加页面
            },

            queryTeacher() {
                this.page = 1;
                this.teacherList = [];
                this.pullFlag = false;
                this.showQueryTeacherForm = false;
                this.teacherQuery("QUERY");
            },

            teacherQuery(action) {
                queryTeacher(this.teacherNumber,this.teacherName,this.teacherBirthday,this.teacherArriveDate,this.page).then(res => {
                    //this.teacherList.push(...res.data)
                    this.teacherList = this.teacherList.concat(res.data.list)
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

            teacherDetailClick(teacherNumber) {
                this.$router.push('/teacher/detail/' + teacherNumber); //跳转到教师详情页
            },
            teacherEditClick(teacherNumber) {
                this.$router.push('/teacher/modify/' + teacherNumber); //跳转到教师修改页
            },
            teacherDeleteClick(teacherNumber) {
                this.$dialog.confirm({
                    title: '删除教师:' + teacherNumber,
                    message: '确定删除吗？'
                }).then(() => {
                    deleteTeacher(teacherNumber).then(res => {
                        console.log(res)
                        this.teacherList.forEach(function(teacher, index, arr) {
                            if(teacher.teacherNumber == teacherNumber) arr.splice(index, 1);
                        });
                    });
                });
            },

            pullingDown() {  //下拉刷新
                this.page = 1;
                this.teacherList = [];
                this.pullFlag = false;
                this.teacherQuery("DOWN");
            },

            pullingUp() {  //上拉加载
                console.log("上拉加载")
                this.page = this.page + 1;
                this.$refs.scroll.finishPullUp();
                this.teacherQuery("UP");
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
    @import "TeacherList.css";
    #teacher {
        height: 100vh;
        width: 100vw;
        position: relative;
    }
</style>
