var SysJobInfo = {
    SysJobInfoData: {},
    validateFields: {
        jobName: {
            validators: {
                notEmpty: {
                    message: '任务名称不能为空'
                }
            }
        },
        methodName: {
            validators: {
                notEmpty: {
                    message: '方法名称不能为空'
                }
            }
        },
        cronExpression: {
            validators: {
                notEmpty: {
                    message: 'cron表达式不能为空'
                },
                remote: { // server result:{"valid",true or false}
                    url: HuiShu.ctxPath + "/quartz/job/checkCronExpressionIsValid",
                    message: 'cron表达式不正确',
                    type: "post",
                    dataType: "json",
                    // data: { // 自定义提交数据，默认值提交当前input value
                    //     "cronExpression": function() {
                    //         return SysJobInfo.get("cronExpression");
                    //     }
                    // },
                    // delay : 500, // /每输入一个字符，就发ajax请求，服务器压力还是太大，设置xxx秒发送一次ajax（默认输入一个字符，提交一次，服务器压力太大）
                }
            }
        },
        jobGroup: {
            validators: {
                notEmpty: {
                    message: '任务组名不能为空'
                }
            }
        }

    },
};

/**
 * 清除数据
 */
SysJobInfo.clearData = function () {
    SysJobInfo.SysJobInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SysJobInfo.set = function (key, value) {
    SysJobInfo.SysJobInfoData[key] = (typeof value == "undefined") ? $.trim($("#" + key).val()) : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SysJobInfo.get = function (key) {
    return $.trim($("#" + key).val());
}

/**
 * 关闭此对话框
 */
SysJobInfo.close = function () {
    parent.layer.close(window.parent.SysJob.layerIndex);
}


/**
 * 收集数据
 */
SysJobInfo.collectData = function () {
    this.set('id').set('jobName').set('methodName').set('cronExpression')
        .set('misfirePolicy').set('status').set('jobGroup').set('methodParams').set('remark').set('concurrent');
}

/**
 * 验证数据是否为空
 */
SysJobInfo.validate = function () {
    $('#SysJobInfoForm').data("bootstrapValidator").resetForm();
    $('#SysJobInfoForm').bootstrapValidator('validate');
    return $("#SysJobInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加
 */
SysJobInfo.addSubmit = function () {

    var flag = this.validate();
    console.log(flag);

    setTimeout(function () {
        flag = $('#SysJobInfoForm').data("bootstrapValidator").isValid();
        console.log(flag);

        SysJobInfo.clearData();
        SysJobInfo.collectData();

        if (!flag){
            return;
        }
        //提交信息
        var ajax = new $ax(HuiShu.ctxPath + "/quartz/job/add", function (data) {
            HuiShu.success("添加成功!");
            window.parent.SysJob.table.refresh();
            SysJobInfo.close();
        }, function (data) {
            HuiShu.error("添加失败!" + data.responseJSON.message + "!");
        });
        ajax.set(SysJobInfo.SysJobInfoData);
        ajax.start();
    },500);
}

/**
 * 提交修改
 */
SysJobInfo.editSubmit = function () {

    var flag = this.validate();
    console.log(flag);

    setTimeout(function () {
        flag = $('#SysJobInfoForm').data("bootstrapValidator").isValid();
        console.log(flag);

        SysJobInfo.clearData();
        SysJobInfo.collectData();

        if (!flag){
            return;
        }
        //提交信息
        var ajax = new $ax(HuiShu.ctxPath + "/quartz/job/edit", function (data) {
            HuiShu.success("修改成功!");
            window.parent.SysJob.table.refresh();
            SysJobInfo.close();
        }, function (data) {
            HuiShu.error("修改失败!" + data.responseJSON.message + "!");
        });
        ajax.set(SysJobInfo.SysJobInfoData);
        ajax.start();
    },500);


}

$(function () {
    HuiShu.initValidator("SysJobInfoForm", SysJobInfo.validateFields);
});
