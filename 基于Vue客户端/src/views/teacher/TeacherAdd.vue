<template>
    <div id="teacher">
        <nav-bar class="nav">
            <template v-slot:left><van-icon name="arrow-left" size="25"  @click.stop="back" /></template>
            <template v-slot:center>添加教师</template>
            <template v-slot:right></template>
        </nav-bar>

        <scroll class="content" top="1" bottom="50" ref="scroll" style="padding: 0px 5px">
            <van-form @submit="onSubmit">
                <van-field clearable name="teacher.teacherNumber" v-model="teacherNumber" label="教师编号：" placeholder="请输入教师编号" :rules="[{ required: true, message: '请填写教师编号' }]" />
                <van-field autosize clearable name="teacher.teacherName" v-model="teacherName" label="教师姓名：" placeholder="请输入教师姓名" :rules="[{ required: true, message: '请填写教师姓名' }]" />
                <van-field autosize clearable name="teacher.teacherSex" v-model="teacherSex" label="性别：" placeholder="请输入性别" :rules="[{ required: true, message: '请填写性别' }]" />
                <van-field readonly name="teacher.teacherBirthday" label="出生日期" :value="teacherBirthday"   placeholder="选择出生日期" @click="showTeacherBirthdayPicker = true" :rules="[{ required: true, message: '出生日期' }]">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearTeacherBirthday">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showTeacherBirthdayPicker"  v-model="picker_teacherBirthday" type="date" @cancel="showTeacherBirthdayPicker = false" title="选择出生日期" @confirm="onConfirmTeacherBirthday" />
                <van-field readonly name="teacher.teacherArriveDate" label="入职日期" :value="teacherArriveDate"   placeholder="选择入职日期" @click="showTeacherArriveDatePicker = true" :rules="[{ required: true, message: '入职日期' }]">
                    <template #button>
                        <van-button size="small" type="warning" @click.stop="clearTeacherArriveDate">清除</van-button>
                    </template>
                </van-field>
                <van-datetime-picker v-if="showTeacherArriveDatePicker"  v-model="picker_teacherArriveDate" type="date" @cancel="showTeacherArriveDatePicker = false" title="选择入职日期" @confirm="onConfirmTeacherArriveDate" />
                <van-field autosize clearable name="teacher.teacherCardNumber" v-model="teacherCardNumber" label="身份证号：" placeholder="请输入身份证号" :rules="[{ required: true, message: '请填写身份证号' }]" />
                <van-field autosize clearable name="teacher.teacherPhone" v-model="teacherPhone" label="联系电话：" placeholder="请输入联系电话" :rules="[{ required: true, message: '请填写联系电话' }]" />
                <van-field  v-show="false" clearable name="teacher.teacherPhoto" v-model="teacherPhoto" label="教师照片："  :rules="[{ required: true, message: '教师照片地址不能为空' }]" />
                <div class="van-cell van-field" style="pointer-events: auto;">
                    <div class="van-cell__title van-field__label"><span>教师照片：</span></div>
                    <div class="van-cell__value van-field__value">
                        <div class="van-field__body">
                            <!-- 图片上传 -->
                            <img-upload name="teacherPhoto" @uploadFinished="teacherPhotoUpload"></img-upload>
                        </div>
                    </div>
                </div>
                <van-field autosize clearable name="teacher.teacherAddress" v-model="teacherAddress" label="家庭地址：" placeholder="请输入家庭地址" :rules="[{ required: true, message: '请填写家庭地址' }]" />
                <div class="van-cell van-field" style="pointer-events: auto;">
                    <div class="van-cell__title van-field__label"><span>教师简介：</span></div>
                    <div class="van-cell__value van-field__value">
                        <div class="van-field__body"></div>
                    </div>
                </div>
                <van-field  v-show="false" name="teacher.teacherMemo" v-model="teacherMemo" rows="5" label="教师简介" type="textarea" maxlength="8000"  placeholder="请输入教师简介" show-word-limit />
                <VueUeditorWrap  v-model="teacherMemo" :config="editorConfig"  @ready="teacherMemoEditorReady" />
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
    import VueUeditorWrap from 'vue-ueditor-wrap'
    import ImgUpload from '../../components/content/ImgUpload'
    import { addTeacher} from "../../network/Teacher";

    export default {
        name: "TeacherAdd",
        components: {
            NavBar,
            Scroll,
            ImgUpload,
            VueUeditorWrap
        },
        data() {
            return {
                teacherNumber: "",  //教师编号
                teacherName: "", //教师姓名
                teacherSex: "", //性别
                showTeacherBirthdayPicker:false, //是否显示出生日期Picker
                teacherBirthday: '', //出生日期
                picker_teacherBirthday: new Date(), //出生日期Picker的日期
                showTeacherArriveDatePicker:false, //是否显示入职日期Picker
                teacherArriveDate: '', //入职日期
                picker_teacherArriveDate: new Date(), //入职日期Picker的日期
                teacherCardNumber: "", //身份证号
                teacherPhone: "", //联系电话
                teacherPhoto: 'upload/NoImage.jpg', //教师照片路径
                teacherAddress: "", //家庭地址
                teacherMemo: "", //教师简介
                // 简单配置
                editorConfig: {
                    // 编辑器不自动被内容撑高
                    autoHeightEnabled: false,
                    // 初始容器高度
                    initialFrameHeight: 280,
                    // 初始容器宽度
                    initialFrameWidth: '100%',
                    // 上传文件接口
                    //serverUrl: 'http://localhost:8080/JavaWebProject/ueditor1_4_3/jsp/controller.jsp',
                }
            }
        },

        mounted() {

        },

        created() {
            document.getElementById("tabbar").style.display = "flex";
        },

        methods: {
            teacherMemoEditorReady(editorInstance) {
                // 这里可以拿到ueditor的实例，比如editorInstance.getContent()就可以拿到编辑器的html内容
                //this.evtHub.$emit('editor.data', editorInstance);
                console.log("准备编辑器ok",editorInstance);
            },
            back() {
                this.$router.go(-1);
            },

            onSubmit(teacher) {  //向后台请求添加教师
                addTeacher(teacher).then(res => {
                    console.log(res)
                    this.$notify.success('添加成功~')
                    setTimeout(()=>{
                        this.$router.go(-1);
                    },200)
                }).catch(error => { this.$notify.error(error)})
            },
            onConfirmTeacherBirthday(value) {  //选中某个出生日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.teacherBirthday = year + "-" +  month + "-" + day ;
                this.showTeacherBirthdayPicker = false;
            },
            clearTeacherBirthday() { //清空出生日期
                this.teacherBirthday='';
                this.showTeacherBirthdayPicker=false;
            },
            onConfirmTeacherArriveDate(value) {  //选中某个入职日期后的响应
                var year = value.getFullYear();
                var month = (value.getMonth()+1)<10?'0'+ (value.getMonth()+1): (value.getMonth()+1);
                var day = value.getDate()<10?'0'+value.getDate():value.getDate();
                this.teacherArriveDate = year + "-" +  month + "-" + day ;
                this.showTeacherArriveDatePicker = false;
            },
            clearTeacherArriveDate() { //清空入职日期
                this.teacherArriveDate='';
                this.showTeacherArriveDatePicker=false;
            },
            teacherPhotoUpload(name,data) {  //上传教师照片后的回调方法
                if(name == "teacherPhoto") {
                    this.teacherPhoto = data;
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

    .van-dropdown-menu {  width: 100%; }

</style>
