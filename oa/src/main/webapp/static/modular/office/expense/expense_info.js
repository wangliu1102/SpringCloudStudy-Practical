/**
 * 报销详情对话框（可用于添加和修改对话框）
 */
var ExpenseInfoDlg = {
    expenseInfoData: {},
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        },
        money: {
            validators: {
                notEmpty: {
                    message: '金额不能为空'
                }
            }
        },
        fileName: {
            validators: {
                notEmpty: {
                    message: '附件不能为空'
                }
            }
        }
    }
};

/**
 * 清除数据
 */
ExpenseInfoDlg.clearData = function () {
    this.expenseInfoData = {};
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ExpenseInfoDlg.set = function (key, value) {
    if (typeof value == "undefined") {
        if (typeof $("#" + key).val() == "undefined") {
            var str = "";
            var ids = "";
            $("input[name='" + key + "']:checkbox").each(function () {
                if (true == $(this).is(':checked')) {
                    str += $(this).val() + ",";
                }
            });
            if (str) {
                if (str.substr(str.length - 1) == ',') {
                    ids = str.substr(0, str.length - 1);
                }
            } else {
                $("input[name='" + key + "']:radio").each(function () {
                    if (true == $(this).is(':checked')) {
                        ids = $(this).val()
                    }
                });
            }
            this.expenseInfoData[key] = ids;
        } else {
            this.expenseInfoData[key] = $("#" + key).val();
        }
    }
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ExpenseInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
ExpenseInfoDlg.close = function () {
    parent.layer.close(window.parent.Expense.layerIndex);
};

/**
 * 收集数据
 */
ExpenseInfoDlg.collectData = function () {
    this.set('id').set('title').set('money').set('comment');
};

/**
 * 验证数据是否为空
 */
ExpenseInfoDlg.validate = function () {
    $('#expenseInfoForm').data("bootstrapValidator").resetForm();
    $('#expenseInfoForm').bootstrapValidator('validate');
    return $("#expenseInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加报销
 */
ExpenseInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    //TODO 检验金额
    var formData = new FormData();
    if (uploadFile != null) {
        formData.append('file', uploadFile);
    }
    formData.append('id', this.get('id'));
    formData.append('title', this.get('title'));
    formData.append('money', this.get('money'));
    formData.append('comment', this.get('comment'));
    //加载层
    var index = layer.load(0, {shade: [0.1, '#f5f5f5']}); //0代表加载的风格，支持0-2
    $.ajax({
        url: HuiShu.ctxPath + "/expense/add",
        type: "POST",
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        timeout: 5000,
        success: function (data) {
            layer.close(index);
            HuiShu.success("添加成功!");
            window.parent.Expense.table.refresh();
            ExpenseInfoDlg.close();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.close(index);
            if (textStatus == 'error') {
                HuiShu.error("添加失败，" + XMLHttpRequest.responseJSON.message + "!");
            } else if (textStatus == 'timeout') {
                HuiShu.info("网络异常，附件上传失败，请重新上传!");
                window.parent.Expense.table.refresh();
                ExpenseInfoDlg.close();
            }
        }
    });
}

/**
 * 提交修改报销
 */
ExpenseInfoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    var formData = new FormData();
    if (uploadFile != null) {
        formData.append('file', uploadFile);
    }
    formData.append('id', this.get('id'));
    formData.append('title', this.get('title'));
    formData.append('money', this.get('money'));
    formData.append('comment', this.get('comment'));

    //加载层
    var index = layer.load(0, {shade: [0.1, '#f5f5f5']}); //0代表加载的风格，支持0-2
    $.ajax({
        url: HuiShu.ctxPath + "/expense/update",
        type: "POST",
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        timeout: 5000,
        success: function (data) {
            layer.close(index);
            HuiShu.success("修改成功!");
            window.parent.Expense.table.refresh();
            ExpenseInfoDlg.close();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.close(index);
            if (textStatus == 'error') {
                HuiShu.error("修改失败，" + XMLHttpRequest.responseJSON.message + "!");
            } else if (textStatus == 'timeout') {
                HuiShu.info("网络异常，附件上传失败，请重新上传!");
                window.parent.Expense.table.refresh();
                ExpenseInfoDlg.close();
            }
        }
    });
}

$(function () {
    HuiShu.initValidator("expenseInfoForm", ExpenseInfoDlg.validateFields);
});

var uploadFile = null;

function changeFile(event, src) {
    var files = event.target.files, file;
    if (files && files.length > 0) {
        // 获取目前上传的文件
        file = files[0];// 文件大小校验的动作
        if (file.size > 1024 * 1024 * 10) {
            HuiShu.info('文件大小不能超过10MB!');
            $("#file").val("");
            return false;
        }
    }
    var suffix = src.substr(src.lastIndexOf('.') + 1);
    if (suffix != "xlsx" && suffix != "xls") {
        HuiShu.alert("文件格式必须是.xlsx或者.xls");
        return false;
    }
    var fileName = src.split("\\");
    var fName = fileName[fileName.length - 1];
    fName = fName.replaceAll("&", " ").replaceAll("#", " ");
    var reg = /[`%^()';{}=]/;
    if (fName != null && fName != "") {
        if (reg.test(fName)) {
            HuiShu.info("上传文件的名称中含有 ` % ^ ( ) ' ; { } = 等非法字符,请修改文件名重新上传");
            $("#file").val("");
        } else {
            uploadFile = $('#file')[0].files[0];
            $("#fileName").val(fName);
        }
    }
}

ExpenseInfoDlg.uploadFile = function () {
    $("#file").click();
};