<template>
    <div id="classInfo">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>编辑班级</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <van-form @submit="onSubmit">
                <van-field readonly name="classInfo.classNumber" v-model="classNumber" label="班级编号：" placeholder="请输入班级编号" :rules="[{ required: true, message: '请填写班级编号' }]" />
                <van-field autosize clearable name="classInfo.className" v-model="className" label="班级名称：" placeholder="请输入班级名称" :rules="[{ required: true, message: '请填写班级名称' }]" />
                <van-field autosize clearable name="classInfo.classSpecialFieldNumber" v-model="classSpecialFieldNumber" label="所属专业：" placeholder="请输入所属专业" :rules="[{ required: true, message: '请填写所属专业' }]" />
                <van-field readonly name="classInfo.classBirthDate" label="成立日期" :value="classBirthDate"   placeholder="选择成立日期" @click="showClassBirthDatePicker = true" :rules="[{ required: true, message: '成立日期' }]">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearClassBirthDate">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showClassBirthDatePicker"  v-model="picker_classBirthDate" type="date" @cancel="showClassBirthDatePicker = false" title="选择成立日期" @confirm="onConfirmClassBirthDate" />
                <van-field autosize clearable name="classInfo.classTeacherCharge" v-model="classTeacherCharge" label="班主任：" placeholder="请输入班主任" :rules="[{ required: true, message: '请填写班主任' }]" />
                <van-field autosize clearable name="classInfo.classTelephone" v-model="classTelephone" label="联系电话：" placeholder="请输入联系电话" :rules="[{ required: true, message: '请填写联系电话' }]" />
                <van-field autosize clearable name="classInfo.classMemo" v-model="classMemo" label="附加信息：" placeholder="请输入附加信息" :rules="[{ required: true, message: '请填写附加信息' }]" />
                <div style="margin: 16px;">
                    <van-button round block type="info" native-type="submit">更新</van-button>
                </div>
            </van-form>
        </scroll>
    </div>
</template>

<script>
    import NavBar from '../../components/common/navbar/NavBar'
    import Scroll from '../../components/common/scroll/Scroll'
    import { updateClassInfo} from "../../network/ClassInfo";
    import { getClassInfo } from "../../network/ClassInfo";
    export default {
        name: "ClassInfoAdd",
        components: {
            NavBar,
            Scroll,
        },
        data() {
            return {
                classNumber: "",  //班级编号
                className: "", //班级名称
                classSpecialFieldNumber: "", //所属专业
                showClassBirthDatePicker:false, //是否显示成立日期Picker
                classBirthDate: '', //成立日期
                picker_classBirthDate: new Date(), //成立日期Picker的日期
                classTeacherCharge: "", //班主任
                classTelephone: "", //联系电话
                classMemo: "", //附加信息
            }
        },

        mounted() {

        },

        created() {
            this.classNumber = this.$route.params.classNumber;  //接收班级编号参数
            //向后端请求单条班级记录
            getClassInfo(this.classNumber).then(res => {
                var classInfo = res.data;
                this.classNumber = classInfo.classNumber; //班级编号
                this.className = classInfo.className;  //班级名称
                this.classSpecialFieldNumber = classInfo.classSpecialFieldNumber;  //所属专业
                this.classBirthDate = classInfo.classBirthDate; //成立日期
                this.picker_classBirthDate = new Date(this.classBirthDate.replace(/-/g, '/'))
                this.classTeacherCharge = classInfo.classTeacherCharge;  //班主任
                this.classTelephone = classInfo.classTelephone;  //联系电话
                this.classMemo = classInfo.classMemo;  //附加信息
            })
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
            back() {
                this.$router.go(-1);
            },

            onSubmit(classInfo) {  //向后台请求更新班级
                updateClassInfo(classInfo).then(res => {
                    console.log(res)
                    this.$notify.success('更新成功~')
                }).catch(error => { this.$notify.error(error)})
            },
            onConfirmClassBirthDate(value) {  //选中某个成立日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.classBirthDate = year + "-" +  month + "-" + day ;
                this.showClassBirthDatePicker = false;
            },
            clearClassBirthDate() { //清空成立日期
                this.classBirthDate='';
                this.showClassBirthDatePicker=false;
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

    .van-dropdown-menu {  width: 100%; }

    .avatar {
        width: 100px;
        height: 100px;
        background-color: #f3f3f3;
        float: left;
        top: 5px;
        right: 5px;
        border-radius: 3px;
       margin-bottom: 26px;
    }
</style>

