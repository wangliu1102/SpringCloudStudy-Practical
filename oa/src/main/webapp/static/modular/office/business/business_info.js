/**
 * 初始化出差管理对话框
 */
var BusinessInfoDlg = {
    count: $("#itemSize").val(),
    businessInfoData: {},
    mutiString: '',		//拼接字符串内容(拼接房屋条目)
    itemTemplate: $("#itemTemplate").html(),
    validateFields: {
        title: {
            validators: {
                notEmpty: {
                    message: '标题不能为空'
                }
            }
        },
        city: {
            validators: {
                notEmpty: {
                    message: '目的城市不能为空'
                }
            }
        },
        customer: {
            validators: {
                notEmpty: {
                    message: '客户/供应商不能为空'
                }
            }
        },
    }
};

/**
 * 清除数据
 */
BusinessInfoDlg.clearData = function () {
    this.businessInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BusinessInfoDlg.set = function (key, val) {
    this.businessInfoData[key] = (typeof val == "undefined") ? $.trim($("#" + key).val()) : value;
    return this;
}

/**
 * 关闭此对话框
 */
BusinessInfoDlg.close = function () {
    parent.layer.close(window.parent.Business.layerIndex);
}

/**
 * 收集数据
 */
BusinessInfoDlg.collectData = function () {
    this.set('id').set('title').set('startTime').set('endTime').set('customer').set('city').set('reason');
}
/**
 * 获取对话框中的数据
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BusinessInfoDlg.get = function (key) {
    return $.trim($("#" + key).val());
}

/**
 * 验证数据是否为空
 */
BusinessInfoDlg.validate = function () {
    // $('#businessInfoForm').data("bootstrapValidator").resetForm();
    $('#businessInfoForm').bootstrapValidator('validate');
    return $("#businessInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加客户
 */
BusinessInfoDlg.addSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    var business = BusinessInfoDlg.businessInfoData;
    if (business.startTime == "" || business.startTime == null) {
        HuiShu.alert("开始时间不能为空！");
        return;
    }
    if (business.endTime == "" || business.endTime == null) {
        HuiShu.alert("结束时间不能为空！");
        return;
    }
    //提交信息
    var ajax = new $ax(HuiShu.ctxPath + "/business/add", function (data) {
        HuiShu.success("添加成功!");
        window.parent.Business.table.refresh();
        BusinessInfoDlg.close();
    }, function (data) {
        HuiShu.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(BusinessInfoDlg.businessInfoData);
    ajax.start();
}

/**
 * 提交修改客户
 */
BusinessInfoDlg.editSubmit = function () {
    this.clearData();
    this.collectData();
    if (!this.validate()) {
        return;
    }
    var business = BusinessInfoDlg.businessInfoData;
    if (business.startTime == "" || business.startTime == null) {
        HuiShu.alert("开始时间不能为空！");
        return;
    }
    if (business.endTime == "" || business.endTime == null) {
        HuiShu.alert("结束时间不能为空！");
        return;
    }
    //提交信息
    var ajax = new $ax(HuiShu.ctxPath + "/business/update", function (data) {
        HuiShu.success("修改成功!");
        window.parent.Business.table.refresh();
        BusinessInfoDlg.close();
    }, function (data) {
        HuiShu.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(BusinessInfoDlg.businessInfoData);
    ajax.start();
}

$(function () {
    HuiShu.initValidator("businessInfoForm", BusinessInfoDlg.validateFields);

});

