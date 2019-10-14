var ConfigInfoDlg = {
	overtimeInfoData : {},
	validateFields : {

		configName : {
			validators : {
				notEmpty : {
					message : '名称不能为空'
				}
			}
		},
		configKey : {
			validators : {
				notEmpty : {
					message : '键名不能为空'
				}
			}
		},
		configValue : {
			validators : {
				notEmpty : {
					message : '键值不能为空'
				}
			}
		},
		configType : {
			validators : {
				notEmpty : {
					message : '请选择'
				}
			}
		},
		remark : {
			validators : {
				notEmpty : {
					message : '备注不能为空'
				}
			}
		}
	}
};

/**
 * 清除数据
 */
ConfigInfoDlg.clearData = function() {
	this.configInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ConfigInfoDlg.set = function(key, val) {
    this.configInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
}

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
ConfigInfoDlg.get = function(key) {
	return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ConfigInfoDlg.close = function() {
	parent.layer.close(window.parent.Config.layerIndex);
}

/**
 * 收集数据
 */
ConfigInfoDlg.collectData = function() {
	this.set('id').set('configName').set('configKey').set('configValue').set('configType')
			.set('remark').set('createBy').set('createTime');
}

/**
 * 验证数据是否为空
 */
ConfigInfoDlg.validate = function() {
	$('#configInfoForm').data("bootstrapValidator").resetForm();
	$('#configInfoForm').bootstrapValidator('validate');
	return $("#configInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加参数
 */
ConfigInfoDlg.addSubmit = function() {
	this.clearData();
	this.collectData();
	if (!this.validate()) {
		return;
	}

// 提交信息
	var ajax = new $ax(HuiShu.ctxPath + "/config/add", function(data) {
		HuiShu.success("添加成功!");
		window.parent.Config.table.refresh();
		ConfigInfoDlg.close();
	}, function(data) {
		HuiShu.error("添加失败!" + data.responseJSON.message + "!");
	});
	ajax.set(this.configInfoData);
	ajax.start();

}

/**
 * 提交修改
 */
ConfigInfoDlg.editSubmit = function() {

	this.clearData();
	this.collectData();

	if (!this.validate()) {
		return;
	}
	
// 提交信息
	var ajax = new $ax(HuiShu.ctxPath + "/config/update", function(data) {
		HuiShu.success("修改成功!");
		window.parent.Config.table.refresh();
		ConfigInfoDlg.close();
	}, function(data) {
		HuiShu.error("修改失败!" + data.responseJSON.message + "!");
	});
	ajax.set(this.configInfoData);
	ajax.start();
}

$(function() {
	HuiShu.initValidator("configInfoForm", ConfigInfoDlg.validateFields);

});