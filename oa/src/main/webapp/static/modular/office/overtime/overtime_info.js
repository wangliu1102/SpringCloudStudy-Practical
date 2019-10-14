/**
 * 初始化加班详情对话框
 */
var OverTimeInfoDlg = {
	overtimeInfoData : {},
	validateFields : {

		title : {
			validators : {
				notEmpty : {
					message : '标题不能为空'
				}
			}
		},
		type : {
			validators : {
				notEmpty : {
					message : '类型不能为空'
				}
			}
		},
		content : {
			validators : {
				notEmpty : {
					message : '事由不能为空'
				}
			}
		},
		startTime : {
			validators : {
				notEmpty : {
					message : '起始时间不能为空'
				}
			}
		},
		endTime : {
			validators : {
				notEmpty : {
					message : '结束时间不能为空'
				}
			}
		}
	}
};

/**
 * 清除数据
 */
OverTimeInfoDlg.clearData = function() {
	this.overtimeInfoData = {};
}

/**
 * 设置对话框中的数据
 * 
 * @param key
 *            数据的名称
 * @param val
 *            数据的具体值
 */
OverTimeInfoDlg.set = function(key, val) {
	this.overtimeInfoData[key] = (typeof value == "undefined") ? $("#" + key)
			.val() : value;
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
OverTimeInfoDlg.get = function(key) {
	return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
OverTimeInfoDlg.close = function() {
	parent.layer.close(window.parent.OverTime.layerIndex);
}

/**
 * 收集数据
 */
OverTimeInfoDlg.collectData = function() {
	this.set('id').set('title').set('type').set('content').set('startTime')
			.set('endTime').set('days').set('createTime');
}

/**
 * 验证数据是否为空
 */
OverTimeInfoDlg.validate = function() {
	$('#overtimeInfoForm').data("bootstrapValidator").resetForm();
	$('#overtimeInfoForm').bootstrapValidator('validate');
	return $("#overtimeInfoForm").data('bootstrapValidator').isValid();
}

/**
 * 提交添加加班
 */
OverTimeInfoDlg.addSubmit = function() {
	this.clearData();
	this.collectData();
	if (!this.validate()) {
		return;
	}
/*	if (this.overtimeData.endTime <= this.overtimeData.startTime) {
		HuiShu.error("终止时间必须大于起始时间");
		 return;
	}*/

// 提交信息
	var ajax = new $ax(HuiShu.ctxPath + "/overtime/add", function(data) {
		HuiShu.success("添加成功!");
		window.parent.OverTime.table.refresh();
		OverTimeInfoDlg.close();
	}, function(data) {
		HuiShu.error("添加失败!" + data.responseJSON.message + "!");
	});
	ajax.set(this.overtimeInfoData);
	ajax.start();

}

/**
 * 提交修改
 */
OverTimeInfoDlg.editSubmit = function() {

	this.clearData();
	this.collectData();

	if (!this.validate()) {
		return;
	}
	
// 提交信息
	var ajax = new $ax(HuiShu.ctxPath + "/overtime/update", function(data) {
		HuiShu.success("修改成功!");
		window.parent.OverTime.table.refresh();
		OverTimeInfoDlg.close();
	}, function(data) {
		HuiShu.error("修改失败!" + data.responseJSON.message + "!");
	});
	ajax.set(this.overtimeInfoData);
	ajax.start();
}

$(function() {
	HuiShu.initValidator("overtimeInfoForm", OverTimeInfoDlg.validateFields);
	laydate.render({
		elem : '#startTime',
		trigger : 'click',
		type : 'datetime'
	})
	laydate.render({
		elem : '#endTime',
		trigger : 'click',
		type : 'datetime'
	})
});
