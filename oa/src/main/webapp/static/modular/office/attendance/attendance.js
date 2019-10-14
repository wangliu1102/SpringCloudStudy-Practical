/**
 * 考勤明细
 */
var Attendance = {
    id: "attendanceTable",  //表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Attendance.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '星期', field: 'week', align: 'center', valign: 'middle', sortable: true},
        {title: '状态', field: 'stateName', align: 'center', valign: 'middle', sortable: true},
        {title: '签到时间', field: 'signinTime', align: 'center', valign: 'middle', sortable: true},
        {title: '签退时间', field: 'signoutTime', align: 'center', valign: 'middle', sortable: true},
        {title: 'IP地址', field: 'ip', align: 'center', valign: 'middle', sortable: true},
        {title: '姓名', field: 'userName', align: 'center', valign: 'middle', sortable: true}
    ];
};

/**
 * 点击查询按钮
 */
Attendance.search = function () {
	  var queryData = {};
	    queryData['name'] = $("#name").val();
	    queryData['startTime'] = $("#startTime").val();
	    queryData['endTime'] = $("#endTime").val();
	   Attendance.table.refresh({query: queryData});
 };
/**
 * 点击重置按钮
 */
Attendance.resetSearch = function () {
     $("#name").val("");
     $("#startTime").val("");
     $("#endTime").val("");
    Attendance.search();
 }

/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Attendance.formParams = function() {
    var queryData = {};
    queryData['name'] = $("#name").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    return queryData;
}

/**
 * 导出
 */
Attendance.exportExcelAll = function () {
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var condition = $("#name").val();

    $.ajax({
        url: HuiShu.ctxPath + '/signIn/getCount',
        type: "GET",
        dataType: "json",
        page: true,
        data: {
            startTime: startTime,
            endTime: endTime,
            condition: condition
        },
        success: function (data) {
            if (data == 0) {
                layer.alert("无数据不导出！");
                return;
            } else {
                var index = layer.load(0, {shade: [0.3, '#f5f5f5']}); //0代表加载的风格，支持0-2
                window.location.href = HuiShu.ctxPath + "/signIn/exportAll?startTime=" + startTime + "&endTime=" + endTime
                    +  "&condition=" + condition ;

                /*定时器判断导出进度是否完成*/
                var timer = setInterval(function () {
                    $.ajax({
                        url: HuiShu.ctxPath + "/signIn/isExport",
                        type: "GET",
                        dataType: "json",
                        data: {},
                        success: function (data) {
                            if (data.resultCode == 0) {
                                layer.close(index);
                                clearInterval(timer);
                            }
                        },
                        error: function (e) {
                            console.log(e.responseText);
                        }
                    });
                }, 1000);
            }
        },
        error: function (e) {
            console.log(e.responseText);
        }
    });
}

$(function () {
    var defaultColunms = Attendance.initColumn();
    var table = new BSTable(Attendance.id, "/signIn/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    Attendance.table = table;
 });
