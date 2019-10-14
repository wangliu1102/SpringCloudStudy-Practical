/**
 * 请假管理初始化
 */
var LeaveWait = {
    id: "LeaveWaitTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
LeaveWait.initColumn = function () {
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
LeaveWait.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    }else{
        LeaveWait.seItem = selected[0];
        return true;
    }
};

/**
 * 关闭此对话框
 */
LeaveWait.close = function() {
    parent.layer.close(window.parent.LeaveWait.layerIndex);
}



/**
 * 打开查看请假详情
 */
LeaveWait.openLeaveDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '请假详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/leaveWait/leave_detail/' + LeaveWait.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 批准用户请假
 * @param userId
 */
LeaveWait.agreeLeave = function () {
    if (this.check()) {
        var leaveId = this.seItem.id;
        var ajax = new $ax(HuiShu.ctxPath + "/leaveWait/agree", function (data) {
            HuiShu.success("批准成功!");
            LeaveWait.table.refresh();
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
LeaveWait.unagreeLeave = function () {
    if (this.check()) {
        var leaveId = this.seItem.id;
        var ajax = new $ax(HuiShu.ctxPath + "/leaveWait/unagree", function (data) {
            HuiShu.success("驳回成功!");
            LeaveWait.table.refresh();
        }, function (data) {
        	  HuiShu.error("驳回失败!" + data.responseJSON.message + "!");
        });
        ajax.set("leaveId", leaveId);
        ajax.start();
    }
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
LeaveWait.search = function () {
    var queryData = {};
    queryData['content'] = $("#content").val().trim();
    queryData['startTime'] = $("#startTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    LeaveWait.table.refresh({query: queryData});
};



$(function () {
    var defaultColunms = LeaveWait.initColumn();
    var table = new BSTable(LeaveWait.id, "/leaveWait/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    LeaveWait.table = table;
});

















