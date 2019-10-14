/**
 * 请假管理初始化
 */
var Leave = {
    id: "LeaveTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Leave.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle',width:'50px' ,visible:false},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '事由', field: 'content', align: 'center', valign: 'middle', sortable: true},
        {title: '开始时间', field: 'startTime', align: 'center', valign: 'middle', sortable: true},
        {title: '结束时间', field: 'endTime', align: 'center', valign: 'middle', sortable: true},
        {title: '请假类型', field: 'leaveType', align: 'center', valign: 'middle', sortable: true},
        {title: '天数', field: 'days', align: 'center', valign: 'middle', sortable: true},
        {title: '创建人', field: 'createBy', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
        {title: '申请状态', field: 'status', align: 'center', valign: 'middle', sortable: true},
        
        ];
        
        
};


/**
 * 检查是否选中
 */
Leave.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Leave.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加请假
 */
Leave.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加请假',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: HuiShu.ctxPath + '/leave/leave_add'
    });
    this.layerIndex = index;
};

/**
 * 打开修改请假
 */
Leave.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '请假详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/leave/leave_update/' + Leave.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 打开待审批页面
 */
Leave.openLeaveWait = function(){
	window.open("/leaveWait")
}


/**
 * 删除请假
 */
Leave.delete = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(HuiShu.ctxPath + "/leave/delete", function () {
                HuiShu.success("删除成功!");
                Leave.table.refresh();
            }, function (data) {
                HuiShu.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("leaveId",Leave.seItem.id);
            ajax.start();
        };

        HuiShu.confirm("是否刪除"+Leave.seItem.title, operation);
    }
};


/**
 * 批准用户请假
 * @param userId
 */
Leave.agreeLeave = function () {
    if (this.check()) {
        var leaveId = this.seItem.id;
        var ajax = new $ax(HuiShu.ctxPath + "/leave/agree", function (data) {
            HuiShu.success("批准成功!");
            Leave.table.refresh();
        }, function (data) {
            HuiShu.error("批准失败!" + data.responseJSON.message + "!");
        });
        ajax.set("leaveId", leaveId);
        ajax.start();
    }
};

/**
 * 驳回用户请假
 * @param userId
 */
Leave.unagreeLeave = function () {
    if (this.check()) {
        var leaveId = this.seItem.id;
        var ajax = new $ax(HuiShu.ctxPath + "/leave/unagree", function (data) {
            HuiShu.success("驳回请假成功!");
            Leave.table.refresh();
        }, function (data) {
            HuiShu.error("驳回请假失败!");
        });
        ajax.set("leaveId", leaveId);
        ajax.start();
    }
};



/**
 * 导出excel
 */
Leave.exportExcel = function () {
    $.ajax({
        url: HuiShu.ctxPath + '/leave/exportExcel',
        type: "POST",
        dataType: "json",
        success: function (data) {
            if (data.code == WEB_STATUS.SUCCESS) {
                window.location.href = HuiShu.ctxPath + "fileDownload?fileName=" + encodeURI(data.data) + "&delete=" + true;
            } else {
                HuiShu.error(data.message);
            }
        },
        error: function (e) {
            console.log(e.responseText);
        }
    });
};

// Procurement.search = function () {
//     var queryData = {};
//     queryData['typeName'] = $("#typeName").val().trim();
//     queryData['supplier'] = $("#supplier").val().trim();
//     queryData['status'] = $("#status").val().trim();
//     Procurement.table.refresh({query: queryData});
// };
//
// $(function () {
//     var defaultColunms = Procurement.initColumn();
//     var table = new BSTable(Procurement.id, "/procurement/list", defaultColunms);
//     table.setPaginationType("server");
//     table.init();
//     Procurement.table = table;
// });

// /**
//  * 点击查询按钮
//  */
// Leave.search = function () {
//     Leave.table.refresh({query: Leave.formParams()});
// };
//
// Leave.resetSearch = function () {
//     $("#condition").val("");
//     $("#startTime").val("");
//     $("#endTime").val("");
//     Leave.search();
// }
/**
 * 查询请假列表
 */
Leave.search = function () {
    var queryData = {};
    queryData['content'] = $("#content").val().trim();
    queryData['startTime'] = $("#startTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    Leave.table.refresh({query: queryData});
};



$(function () {
    var defaultColunms = Leave.initColumn();
    var table = new BSTable("LeaveTable", "/leave/list", defaultColunms);
    table.setPaginationType("server");
    Leave.table =  table.init();
});

















