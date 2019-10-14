/**
 * 初始化请假详情对话框
 */
var LeaveInfoDlg = {
    leaveInfoData : {},
    validateFields: {

        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        },
        content: {
            validators: {
                notEmpty: {
                    message: '事由不能为空'
                }
            }
        },
        leaveType:{
            validators: {
                notEmpty: {
                    message: '请假类型不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
LeaveInfoDlg.clearData = function() {
    this.leaveInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LeaveInfoDlg.set = function(key, val) {
    this.leaveInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LeaveInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LeaveInfoDlg.close = function() {
    parent.layer.close(window.parent.Leave.layerIndex);
}

/**
 * 收集数据
 */
LeaveInfoDlg.collectData = function() {
    this.set('id').set('title').set('content').set('startTime').set('endTime').set('days').set('createTime').set('leaveType').set('state');
}

/**
 * 验证数据是否为空
 */
LeaveInfoDlg.validate = function () {
    $('#leaveInfoForm').data("bootstrapValidator").resetForm();
    $('#leaveInfoForm').bootstrapValidator('validate');
    return $("#leaveInfoForm").data('bootstrapValidator').isValid();
}


/**
 * 提交添加请假
 */
LeaveInfoDlg.addSubmit = function() {
    this.clearData();
    this.collectData();
     if (!this.validate()) {
         return;
     }
    var leave = LeaveInfoDlg.leaveInfoData;
    if (leave.startTime == "" || leave.startTime == null) {
        HuiShu.alert("开始时间不能为空！");
        return;
    }
    if (leave.endTime == "" || leave.endTime == null) {
        HuiShu.alert("结束时间不能为空！");
        return;
    }
    // if (this.leaveData.endTime <= this.leaveData.startTime) {
    //     HuiShu.error("终止时间必须大于起始时间");
    //     // return;
    // } else {
    //
    // }
    //提交信息
    var ajax = new $ax(HuiShu.ctxPath + "/leave/add", function(data){
        HuiShu.success("添加成功!");
        window.parent.Leave.table.refresh();
        LeaveInfoDlg.close();
    },function(data){
        HuiShu.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.leaveInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
LeaveInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

     if (!this.validate()) {
        return;
     }
    var leave = LeaveInfoDlg.leaveInfoData;
    if (leave.startTime == "" || leave.startTime == null) {
        HuiShu.alert("开始时间不能为空！");
        return;
    }
    if (leave.endTime == "" || leave.endTime == null) {
        HuiShu.alert("结束时间不能为空！");
        return;
    }
    // } else if(this.get('email') != "" && !SupplierInfoDlg.testEmail(this.get('email'))){
    //     HuiShu.alert("请输入正确的邮箱格式！");
    //     return false ;
    // }

    //提交信息
    var ajax = new $ax(HuiShu.ctxPath + "/leave/update", function(data){
        HuiShu.success("修改成功!");
        window.parent.Leave.table.refresh();
        LeaveInfoDlg.close();
    },function(data){
        HuiShu.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.leaveInfoData);
    ajax.start();
}
$(function () {
    HuiShu.initValidator("leaveInfoForm", LeaveInfoDlg.validateFields);

});

// RolInfoDlg.editSubmit = function () {
//
//     this.clearData();
//     this.collectData();
//
//     if (!this.validate()) {
//         return;
//     }
//
//     //提交信息
//     var ajax = new $ax(HuiShu.ctxPath + "/role/edit", function (data) {
//         HuiShu.success("修改成功!");
//         window.parent.Role.table.refresh();
//         RolInfoDlg.close();
//     }, function (data) {
//         HuiShu.error("修改失败!" + data.responseJSON.message + "!");
//     });
//     ajax.set(this.roleInfoData);
//     ajax.start();
// };


// function onBodyDown(event) {
//     if (!(event.target.id == "menuBtn" || event.target.id == "parentDeptMenu" || $(
//             event.target).parents("#parentDeptMenu").length > 0)) {
//         LeaveInfoDlg.hideDeptSelectTree();
//     }
// }

$(function() {
    HuiShu.initValidator("leaveInfoForm", LeaveInfoDlg.validateFields);
    laydate.render({
        elem:'#startTime',
        trigger: 'click',
        type : 'datetime'
    })
    laydate.render({
        elem:'#endTime',
        trigger: 'click',
        type : 'datetime'
    })

});
