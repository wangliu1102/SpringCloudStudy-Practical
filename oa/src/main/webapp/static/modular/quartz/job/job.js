var SysJob = {
    id: "sysJobTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SysJob.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {
            title: '任务编号', field: 'id', align: 'center', valign: 'middle', width: '100px', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '任务名称', field: 'jobName', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '任务组名', field: 'jobGroup', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '方法名称', field: 'methodName', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '方法参数', field: 'methodParams', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '执行表达式', field: 'cronExpression', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '任务状态', field: 'status', align: 'center', visible: false, valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '任务状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '是否并发执行', field: 'concurrentName', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '执行策略', field: 'misfirePolicyName', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        }
        ,
        {
            title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        }
        ,
        {
            title: '备注', field: 'remark', align: 'center', width: '300px', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        }

    ];
};


/**
 * 检查是否选中
 */
SysJob.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    } else {
        SysJob.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
SysJob.openAddSysJob = function () {
    var index = layer.open({
        type: 2,
        title: '添加定时任务',
        area: ['800px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: HuiShu.ctxPath + '/quartz/job/job_add'
    });
    this.layerIndex = index;

};

/**
 * 点击编辑
 */
SysJob.openEditSysJob = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '修改定时任务',
            area: ['800px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/quartz/job/job_edit/' + SysJob.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 点击启用
 */
SysJob.enableJob = function () {
    if (this.check()) {
        if (this.seItem.status == "1") { // 暂停状态
            var operation = function () {
                var jobId = SysJob.seItem.id;
                var ajax = new $ax(HuiShu.ctxPath + "/quartz/job/changeStatus", function (data) {
                    HuiShu.success(data.message);
                    SysJob.table.refresh();
                }, function (data) {
                    HuiShu.error(data.message);
                });
                ajax.set("id", jobId);
                ajax.set("status", "0");
                ajax.start();
            };

            HuiShu.confirm("确认要启用该定时任务吗？", operation);
        } else {
            layer.alert("该定时任务处于启用状态");
        }
    }
};

/**
 * 点击暂停
 */
SysJob.pauseJob = function () {
    if (this.check()) {
        if (this.seItem.status == "0") { // 启用状态
            var operation = function () {
                var jobId = SysJob.seItem.id;
                var ajax = new $ax(HuiShu.ctxPath + "/quartz/job/changeStatus", function (data) {
                    HuiShu.success(data.message);
                    SysJob.table.refresh();
                }, function (data) {
                    HuiShu.error(data.message);
                });
                ajax.set("id", jobId);
                ajax.set("status", "1");
                ajax.start();
            };

            HuiShu.confirm("确认要暂停该定时任务吗？", operation);
        } else {
            layer.alert("该定时任务处于暂停状态");
        }
    }
};

/**
 * 点击执行
 */
SysJob.runJob = function () {
    if (this.check()) {
        var operation = function () {
            var jobId = SysJob.seItem.id;
            var ajax = new $ax(HuiShu.ctxPath + "/quartz/job/run", function (data) {
                HuiShu.success(data.message);
                SysJob.table.refresh();
            }, function (data) {
                HuiShu.error(data.message);
            });
            ajax.set("id", jobId);
            ajax.start();
        }
        HuiShu.confirm("确认要立即执行一次该定时任务吗？", operation);
    }
};


SysJob.resetSearch = function () {

    $("#jobName").val("");
    $("#methodName").val("");
    $("#status").val("");
    SysJob.search();
}
/**
 * 查询列表
 */
SysJob.search = function () {

    var queryData = {};

    queryData['jobName'] = $("#jobName").val();
    queryData['methodName'] = $("#methodName").val();
    queryData['status'] = $("#status").val();

    SysJob.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SysJob.initColumn();
    var table = new BSTable(SysJob.id, "/quartz/job/list", defaultColunms);
    table.setPaginationType("client");
    SysJob.table = table.init();
});
