/**
 * 加班管理初始化
 */
var OverTime = {
    id: "OverTimeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
OverTime.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle', width: '50px', visible: false},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '加班类型', field: 'type', align: 'center', valign: 'middle', sortable: true},
        {title: '加班内容', field: 'content', align: 'center', valign: 'middle', sortable: true},
        {title: '开始时间', field: 'startTime', align: 'center', valign: 'middle', sortable: true},
        {title: '结束时间', field: 'endTime', align: 'center', valign: 'middle', sortable: true},
        {title: '加班时长', field: 'hours', align: 'center', valign: 'middle', sortable: true},
        {title: '创建人', field: 'createBy', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
OverTime.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    } else {
        OverTime.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加加班
 */
OverTime.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加加班',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: HuiShu.ctxPath + '/overtime/overtime_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看加班详情
 */
OverTime.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '加班详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/overtime/overtime_update/' + OverTime.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除加班
 */
OverTime.delete = function () {
    if (this.check()) {
        var operation = function () {
            var ajax = new $ax(HuiShu.ctxPath + "/overtime/delete", function () {
                HuiShu.success("删除成功!");
                OverTime.table.refresh();
            }, function (data) {
                HuiShu.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", OverTime.seItem.id);
            ajax.start();
        };

        HuiShu.confirm("是否刪除选中加班记录？", operation);
    }
};

/**
 * 查询加班列表
 */
OverTime.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val().trim();
    queryData['startTime'] = $("#startTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    OverTime.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = OverTime.initColumn();
    var table = new BSTable(OverTime.id, "/overtime/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    OverTime.table = table;
});
