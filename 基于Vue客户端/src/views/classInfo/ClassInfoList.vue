<template>
    <div id="classInfo">
        <van-popup v-model="showQueryClassInfoForm"  closeable position="top" :style="{ height: 'auto' }">
            <div class="classInfoQueryForm">
                <h3>班级查询</h3>
                <van-field clearable v-model="classNumber" label="班级编号：" placeholder="请输入班级编号" />
                <van-field clearable v-model="className" label="班级名称：" placeholder="请输入班级名称" />
                <van-field clearable v-model="classSpecialFieldNumber" label="所属专业：" placeholder="请输入所属专业" />
                <van-field readonly label="成立日期" :value="classBirthDate"   placeholder="选择成立日期" @click="showClassBirthDatePicker = true">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearClassBirthDate">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showClassBirthDatePicker" v-model="picker_classBirthDate" type="date" @cancel="showClassBirthDatePicker = false" title="选择成立日期" @confirm="onConfirmClassBirthDate" />
                <div class="queryBtn">
                    <van-button round  size= small type="primary" @click="queryClassInfo">查询</van-button>
                </div>
            </div>
        </van-popup>

        <nav-bar class="nav" ref="nav" >
            <template v-slot:left>
                <van-icon name="plus" size="25" v-show="false" @click.stop="classInfoAdd" />
                <van-icon name="arrow-left" v-show="false" size="25"  @click.stop="back" />
            </template>
            <template v-slot:center>班级列表</template>
            <template v-slot:right><van-icon name="search" size="25"  @click.stop="showQueryClassInfoForm=true" /></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" :isPullDown="true" @pullingDown="pullingDown" :isPullUp="true" @pullingUp="pullingUp"  @iscroll="contentScroll">
            <div class="ignore-pull-down1" ref="pull" v-show="pullFlag">下拉更新</div>
            <div class="ignore-pull-down2" ref="pull" v-show="!pullFlag">
                <span class="ball1"></span><span class="ball2"></span><span class="ball3"></span>
            </div>
            <div class="classInfoList">
                <template v-for="classInfo in classInfoList">
                    <div class="classInfoItem" :key="classInfo.classNumber" @click.stop="classInfoDetailClick(`${classInfo.classNumber}`)">
                        <div class="content_view">
                            <div class="classInfoText">
                                <div>班级编号：{{classInfo.classNumber}}</div>
                                <div>班级名称：{{classInfo.className}}</div>
                                <div>所属专业：{{classInfo.classSpecialFieldNumber}}</div>
                            </div>
                        </div>
                        <div class="opt_view" v-show="false">
                            <van-button round  size= small type="primary"  @click.stop="classInfoEditClick(`${classInfo.classNumber}`)" >编辑</van-button>
                            <van-button round  size= small type="warning" @click.stop="classInfoDeleteClick(`${classInfo.classNumber}`)" style="margin-left:10px;">删除</van-button>
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

    import {queryClassInfo,deleteClassInfo} from '../../network/ClassInfo'
    export default {
        name: 'ClassInfoList',
        components: {
            NavBar,
            Scroll,
            ReturnTop
        },
        data() {
            return {
                classInfoList: [],  //界面需要展示的班级列表数据
                classNumber: "", //班级编号查询关键字
                className: "", //班级名称查询关键字
                classSpecialFieldNumber: "", //所属专业查询关键字

                classBirthDate: "",  //成立日期
                showClassBirthDatePicker: false, //是否显示选择成立日期Picker
                picker_classBirthDate: new Date(), //成立日期Picker绑定的日期

                pullFlag: true,  //是否允许下拉刷新标志
                page: 1, //默认显示结果第1页
                showQueryClassInfoForm: false, //是否显示查询班级表单
                isReturnTop: false,
            }
        },

        created() {
            //查询班级信息
            this.classInfoQuery("");
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
           back() {
                this.$router.go(-1);
           },

            onConfirmClassBirthDate(value) {  //选中某个成立日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.classBirthDate = year + "-" +  month + "-" + day ;
                this.showClassBirthDatePicker = false;
            },
            clearClassBirthDate() {  //清空成立日期
               this.classBirthDate=''
               this.showClassBirthDatePicker=false;
            },

            classInfoAdd() {
                this.$router.push('/classInfo/add');  //打开班级添加页面
            },

            queryClassInfo() {
                this.page = 1;
                this.classInfoList = [];
                this.pullFlag = false;
                this.showQueryClassInfoForm = false;
                this.classInfoQuery("QUERY");
            },

            classInfoQuery(action) {
                queryClassInfo(this.classNumber,this.className,this.classSpecialFieldNumber,this.classBirthDate,this.page).then(res => {
                    //this.classInfoList.push(...res.data)
                    this.classInfoList = this.classInfoList.concat(res.data.list)
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

            classInfoDetailClick(classNumber) {
                this.$router.push('/classInfo/detail/' + classNumber); //跳转到班级详情页
            },
            classInfoEditClick(classNumber) {
                this.$router.push('/classInfo/modify/' + classNumber); //跳转到班级修改页
            },
            classInfoDeleteClick(classNumber) {
                this.$dialog.confirm({
                    title: '删除班级:' + classNumber,
                    message: '确定删除吗？'
                }).then(() => {
                    deleteClassInfo(classNumber).then(res => {
                        console.log(res)
                        this.classInfoList.forEach(function(classInfo, index, arr) {
                            if(classInfo.classNumber == classNumber) arr.splice(index, 1);
                        });
                    });
                });
            },

            pullingDown() {  //下拉刷新
                this.page = 1;
                this.classInfoList = [];
                this.pullFlag = false;
                this.classInfoQuery("DOWN");
            },

            pullingUp() {  //上拉加载
                console.log("上拉加载")
                this.page = this.page + 1;
                this.$refs.scroll.finishPullUp();
                this.classInfoQuery("UP");
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
    @import "ClassInfoList.css";
    #classInfo {
        height: 100vh;
        width: 100vw;
        position: relative;
    }
</style>
