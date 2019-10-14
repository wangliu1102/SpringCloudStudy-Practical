/**
 * 初始化请假详情对话框
 */
var WeeklyInfoDlg = {
    WeeklyInfoData: {},
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
                    message: '内容不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
WeeklyInfoDlg.clearData = function () {
    this.WeeklyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WeeklyInfoDlg.set = function (key, val) {
    this.WeeklyInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WeeklyInfoDlg.get = function (key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
WeeklyInfoDlg.close = function () {
    parent.layer.close(window.parent.Weekly.layerIndex);
}

/**
 * 收集数据
 */
WeeklyInfoDlg.collectData = function () {
    this.WeeklyInfoData['content'] = $("#content").val();
    this.set('id').set('title');
}

/**
 * 验证数据是否为空
 */
WeeklyInfoDlg.validate = function () {
    $('#weeklyInfoForm').data("bootstrapValidator").resetForm();
    $('#weeklyInfoForm').bootstrapValidator('validate');
    return $("#weeklyInfoForm").data('bootstrapValidator').isValid();
}
/**
 * 提交添加周报
 */
WeeklyInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(HuiShu.ctxPath + "/weekly/add", function (data) {
        HuiShu.success("添加成功!");
        window.parent.Weekly.table.refresh();
        WeeklyInfoDlg.close();
    }, function (data) {
        HuiShu.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.WeeklyInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
WeeklyInfoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //提交信息
    var ajax = new $ax(HuiShu.ctxPath + "/weekly/update", function (data) {
        HuiShu.success("修改成功!");
        window.parent.Weekly.table.refresh();
        WeeklyInfoDlg.close();
    }, function (data) {
        HuiShu.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.WeeklyInfoData);
    ajax.start();
}

$(function () {
    HuiShu.initValidator("weeklyInfoForm", WeeklyInfoDlg.validateFields);
});
