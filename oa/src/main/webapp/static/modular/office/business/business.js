/**
 * 系统管理--消息管理
 */
var Business = {
    id: "BusinessTable",    //表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Business.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '客户/供应商', field: 'customer', align: 'center', valign: 'middle', sortable: true},
        {title: '目的城市', field: 'city', align: 'center', valign: 'middle', sortable: true},
        {title: '事由', field: 'reason', align: 'center', valign: 'middle', sortable: true},
        {title: '开始时间', field: 'startTime', align: 'center', valign: 'middle', sortable: true},
        {title: '结束时间', field: 'endTime', align: 'center', valign: 'middle', sortable: true},
        {title: '发布者', field: 'userName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
    ];
};

/**
 * 检查是否选中
 */
Business.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Business.seItem = selected[0];
        return true;
    }
};

/**
 * 点击查询按钮
 */
Business.search = function () {
    var queryData = {};
    queryData['title'] = $("#title").val().trim();
    queryData['startTime'] = $("#startTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    Business.table.refresh({query: queryData});
};

/**
 * 查询重置
 */
Business.resetSearch = function () {
    $("#title").val("");
    $("#startTime").val("");
    $("#endTime").val("");
    Business.search();
}

/**
 * 导出excel按钮
 */
Business.exportExcelAll = function () {
    var checkRows = table.checkStatus(Business.tableId);
    if (checkRows.data.length === 0) {
        HuiShu.error("请选择要导出的数据");
    } else {
        table.exportFile(tableResult.config.id, checkRows.data, 'xls');
    }
};


/**
 * 查询表单提交参数对象
 * @returns {{}}
 */
Business.formParams = function() {
    var queryData = {};
    queryData['title'] = $("#title").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    return queryData;
}

/**
 * 点击添加出差
 */
Business.openAddbusiness = function () {
    var index = layer.open({
        type: 2,
        title: '添加出差',
        area: ['800px', '620px'], //宽高
        fix: false, //不固定
        content: HuiShu.ctxPath + '/business/business_add',
    });
    this.layerIndex = index;
};

/**
 * 打开查看出差详情
 */
Business.onEditbusiness = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '出差详情',
            area: ['800px', '620px'], //宽高
            fix: false, //不固定
            content: HuiShu.ctxPath + '/business/business_update/' + Business.seItem.id
        });
        this.layerIndex = index;
    }
};


/**
 * 删除出差
 */
Business.onDeletebusiness = function () {
    if (this.check()) {

        var operation = function(){
            var ajax = new $ax(HuiShu.ctxPath + "/business/delete", function (data) {
                HuiShu.success("删除成功!");
                Business.table.refresh();
            }, function (data) {
                HuiShu.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", Business.seItem.id);
            ajax.start();
        };

        HuiShu.confirm("是否删除出差 " + Business.seItem.title + "?",  operation);
    }
};



//渲染时间选择框
laydate.render({
    elem: '#startTime'
});

//渲染时间选择框
laydate.render({
    elem: '#endTime'
});


/**
 * 导出
 */
Business.exportExcelAll = function () {
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    var condition = $("#title").val();

    $.ajax({
        url: HuiShu.ctxPath + '/business/getCount',
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
                window.location.href = HuiShu.ctxPath + "/business/exportAll?startTime=" + startTime + "&endTime=" + endTime
                    +  "&condition=" + condition ;

                /*定时器判断导出进度是否完成*/
                var timer = setInterval(function () {
                    $.ajax({
                        url: HuiShu.ctxPath + "/business/isExport",
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
    var defaultColunms = Business.initColumn();
    var table = new BSTable(Business.id, "/business/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    Business.table = table;
});

