var SysJobLog = {
    id: "sysJobLogTable",	//表格id
    seItem: null,		//选中的条目
    seItemList: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SysJobLog.initColumn = function () {
    return [
        {field: 'selectItem', checkbox: true},
        {
            title: '日志编号', field: 'id', align: 'center', valign: 'middle', width: '100px', sortable: true,
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
            title: '日志信息', field: 'jobMessage', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '执行状态', field: 'statusName', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        },
        {
            title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        }
        ,
        {
            title: '异常信息', field: 'exceptionInfo', align: 'center', width: '300px', valign: 'middle', sortable: true,
            formatter: function (value, row, index) {
                return setTitle(value, row, index);
            }
        }

    ];
};


/**
 * 检查是否选中
 */
SysJobLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    } else if (selected.length == 1) {
        SysJobLog.seItemList = selected;
        SysJobLog.seItem = selected[0];
        return true;
    } else {
        SysJobLog.seItemList = selected;
        return true;
    }
};

/**
 * 点击删除
 */
SysJobLog.delete = function () {
    if (this.check()) {

        var operation = function () {
            var selectedList = SysJobLog.seItemList;
            var idList = "";
            for (var i = 0; i < selectedList.length; i++) {
                if (i == selectedList.length - 1) {
                    idList += selectedList[i].id;
                } else {
                    idList += selectedList[i].id + ",";
                }
            }

            $.ajax({
                url: HuiShu.ctxPath + "/quartz/jobLog/remove",
                type: "POST",
                dataType: "json",
                // traditional: true,//加上这个属性，后台用 String[] 之类的参数就可以接收到了
                data: {
                    ids: idList
                },
                async: false,
                success: function (data) {
                    HuiShu.success("删除成功!");
                    SysJobLog.table.refresh();
                },
                error: function (data) {
                    HuiShu.error("删除失败!" + data.responseJSON.message + "!");
                }
            });
        };

        HuiShu.confirm("确认要刪除选中的调度日志吗?", operation);

    }
};

/**
 * 点击清空
 */
SysJobLog.trashLog = function () {
    var operation = function () {
        var ajax = new $ax(HuiShu.ctxPath + "/quartz/jobLog/clean", function (data) {
            HuiShu.success("清空成功!");
            SysJobLog.table.refresh();
        }, function (data) {
            HuiShu.error("清空失败!");
        });
        ajax.start();
    };

    HuiShu.confirm("确认要清空所有调度日志吗？", operation);

};

/**
 * 点击查看详情
 */
SysJobLog.detail = function () {

};


SysJobLog.resetSearch = function () {

    $("#jobName").val("");
    $("#methodName").val("");
    $("#status").val("");
    $("#beginTime").val("");
    $("#endTime").val("");
    SysJobLog.search();
}
/**
 * 查询列表
 */
SysJobLog.search = function () {

    var queryData = {};

    queryData['jobName'] = $("#jobName").val();
    queryData['methodName'] = $("#methodName").val();
    queryData['status'] = $("#status").val();
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();

    SysJobLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SysJobLog.initColumn();
    var table = new BSTable(SysJobLog.id, "/quartz/jobLog/list", defaultColunms);
    table.setPaginationType("client");
    SysJobLog.table = table.init();
});
