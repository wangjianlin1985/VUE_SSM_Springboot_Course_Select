<template>
    <div id="student">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>添加学生</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <van-form @submit="onSubmit">
                <van-field clearable name="student.user_name" v-model="user_name" label="手机号：" placeholder="请输入手机号" :rules="[{ required: true, message: '请填写手机号' }]" />
                <van-field autosize clearable name="student.studentNumber" v-model="studentNumber" label="学号：" placeholder="请输入学号" :rules="[{ required: true, message: '请填写学号' }]" />
                <van-field autosize clearable name="student.password" v-model="password" label="登录密码：" placeholder="请输入登录密码" :rules="[{ required: true, message: '请填写登录密码' }]" />
                <van-field autosize clearable name="student.studentName" v-model="studentName" label="姓名：" placeholder="请输入姓名" :rules="[{ required: true, message: '请填写姓名' }]" />
                <van-field autosize clearable name="student.studentSex" v-model="studentSex" label="性别：" placeholder="请输入性别" :rules="[{ required: true, message: '请填写性别' }]" />
                <van-field v-show="false" clearable name="student.studentClassNumber.classNumber" v-model="studentClassNumber_classNumber" label="所在班级：" />
                <van-field readonly clickable label="所在班级" :value="studentClassNumber_className"  right-icon="arrow-down" placeholder="选择所在班级" @click="showStudentClassNumberPicker = true" />
                <van-picker v-show="showStudentClassNumberPicker" show-toolbar :columns="studentClassNumberColumns" :default-index="1" @cancel="showStudentClassNumberPicker = false" @confirm="onConfirmStudentClassNumber" />
                <van-field readonly name="student.studentBirthday" label="出生日期" :value="studentBirthday"   placeholder="选择出生日期" @click="showStudentBirthdayPicker = true" :rules="[{ required: true, message: '出生日期' }]">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearStudentBirthday">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showStudentBirthdayPicker"  v-model="picker_studentBirthday" type="date" @cancel="showStudentBirthdayPicker = false" title="选择出生日期" @confirm="onConfirmStudentBirthday" />
                <van-field autosize clearable name="student.studentState" v-model="studentState" label="政治面貌：" placeholder="请输入政治面貌" :rules="[{ required: true, message: '请填写政治面貌' }]" />
                <van-field  v-show="false" clearable name="student.studentPhoto" v-model="studentPhoto" label="学生照片："  :rules="[{ required: true, message: '学生照片地址不能为空' }]" />
                <div class="van-cell van-field" style="pointer-events: auto;">
                    <div class="van-cell__title van-field__label"><span>学生照片：</span></div>
                    <div class="van-cell__value van-field__value">
                        <div class="van-field__body">
                            <!-- 图片上传 -->
                            <img-upload name="studentPhoto" @uploadFinished="studentPhotoUpload"></img-upload>
                        </div>
                    </div>
                </div>
                <van-field autosize clearable name="student.studentTelephone" v-model="studentTelephone" label="联系电话：" placeholder="请输入联系电话" :rules="[{ required: true, message: '请填写联系电话' }]" />
                <van-field autosize clearable name="student.studentEmail" v-model="studentEmail" label="学生邮箱：" placeholder="请输入学生邮箱" :rules="[{ required: true, message: '请填写学生邮箱' }]" />
                <van-field autosize clearable name="student.studentQQ" v-model="studentQQ" label="联系qq：" placeholder="请输入联系qq" :rules="[{ required: true, message: '请填写联系qq' }]" />
                <van-field autosize clearable name="student.studentAddress" v-model="studentAddress" label="家庭地址：" placeholder="请输入家庭地址" :rules="[{ required: true, message: '请填写家庭地址' }]" />
                <van-field autosize clearable name="student.studentMemo" v-model="studentMemo" label="附加信息：" placeholder="请输入附加信息" :rules="[{ required: true, message: '请填写附加信息' }]" />
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
    import ImgUpload from '../../components/content/ImgUpload'
    import { queryAllClassInfo } from '../../network/ClassInfo'
    import { addStudent} from "../../network/Student";

    export default {
        name: "StudentAdd",
        components: {
            NavBar,
            Scroll,
            ImgUpload,
        },
        data() {
            return {
                user_name: "",  //手机号
                studentNumber: "", //学号
                password: "", //登录密码
                studentName: "", //姓名
                studentSex: "", //性别
                studentClassNumberList: [], //所在班级列表
                studentClassNumber_classNumber: '', //班级编号
                studentClassNumber_className: "", //班级名称
                showStudentClassNumberPicker: false, //是否显示所在班级Picker
                studentClassNumberColumns: [], //所在班级Picker的选项
                showStudentBirthdayPicker:false, //是否显示出生日期Picker
                studentBirthday: '', //出生日期
                picker_studentBirthday: new Date(), //出生日期Picker的日期
                studentState: "", //政治面貌
                studentPhoto: 'upload/NoImage.jpg', //学生照片路径
                studentTelephone: "", //联系电话
                studentEmail: "", //学生邮箱
                studentQQ: "", //联系qq
                studentAddress: "", //家庭地址
                studentMemo: "", //附加信息
            }
        },

        mounted() {

        },

        created() {
            /*查询所有所在班级初始化下拉Picker*/
            queryAllClassInfo().then(res => {
                this.studentClassNumberList = res.data;
                for(var i=0;i<this.studentClassNumberList.length;i++) {
                    this.studentClassNumberColumns.push(this.studentClassNumberList[i].className)
                }
                if(this.studentClassNumberList.length > 0) {
                    var firstStudentClassNumber = this.studentClassNumberList[0]; //默认选中第一个选项
                    this.studentClassNumber_className = firstStudentClassNumber.className;
                    this.studentClassNumber_classNumber = firstStudentClassNumber.classNumber;
                }
            });

            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
            back() {
                this.$router.go(-1);
            },

            onConfirmStudentClassNumber(value,index) {
                this.studentClassNumber_className = value; //选中某个所在班级后响应
                this.studentClassNumber_classNumber = this.studentClassNumberList[index].classNumber;
                this.showStudentClassNumberPicker = false;
            },
            onSubmit(student) {  //向后台请求添加学生
                addStudent(student).then(res => {
                    console.log(res)
                    this.$notify.success('添加成功~')
                    setTimeout(()=>{
                        this.$router.go(-1);
                    },200)
                }).catch(error => { this.$notify.error(error)})
            },
            onConfirmStudentBirthday(value) {  //选中某个出生日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.studentBirthday = year + "-" +  month + "-" + day ;
                this.showStudentBirthdayPicker = false;
            },
            clearStudentBirthday() { //清空出生日期
                this.studentBirthday='';
                this.showStudentBirthdayPicker=false;
            },
            studentPhotoUpload(name,data) {  //上传学生照片后的回调方法
                if(name == "studentPhoto") {
                    this.studentPhoto = data;
                }
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

    .van-dropdown-menu {  width: 100%; }

</style>
