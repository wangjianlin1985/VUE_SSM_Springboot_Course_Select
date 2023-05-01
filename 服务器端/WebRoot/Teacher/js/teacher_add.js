$(function () {
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	UE.delEditor('teacher_teacherMemo');
	var teacher_teacherMemo_editor = UE.getEditor('teacher_teacherMemo'); //教师简介编辑框
	$("#teacher_teacherNumber").validatebox({
		required : true, 
		missingMessage : '请输入教师编号',
	});

	$("#teacher_teacherName").validatebox({
		required : true, 
		missingMessage : '请输入教师姓名',
	});

	$("#teacher_teacherSex").validatebox({
		required : true, 
		missingMessage : '请输入性别',
	});

	$("#teacher_teacherBirthday").datebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	$("#teacher_teacherArriveDate").datebox({
	    required : true, 
	    showSeconds: true,
	    editable: false
	});

	//单击添加按钮
	$("#teacherAddButton").click(function () {
		if(teacher_teacherMemo_editor.getContent() == "") {
			alert("请输入教师简介");
			return;
		}
		//验证表单 
		if(!$("#teacherAddForm").form("validate")) {
			$.messager.alert("错误提示","你输入的信息还有错误！","warning");
			$(".messager-window").css("z-index",10000);
		} else {
			$("#teacherAddForm").form({
			    url:"Teacher/add",
			    onSubmit: function(){
					if($("#teacherAddForm").form("validate"))  { 
	                	$.messager.progress({
							text : "正在提交数据中...",
						}); 
	                	return true;
	                } else {
	                    return false;
	                }
			    },
			    success:function(data){
			    	$.messager.progress("close");
                    //此处data={"Success":true}是字符串
                	var obj = jQuery.parseJSON(data); 
                    if(obj.success){ 
                        $.messager.alert("消息","保存成功！");
                        $(".messager-window").css("z-index",10000);
                        $("#teacherAddForm").form("clear");
                        teacher_teacherMemo_editor.setContent("");
                    }else{
                        $.messager.alert("消息",obj.message);
                        $(".messager-window").css("z-index",10000);
                    }
			    }
			});
			//提交表单
			$("#teacherAddForm").submit();
		}
	});

	//单击清空按钮
	$("#teacherClearButton").click(function () { 
		$("#teacherAddForm").form("clear"); 
	});
});
