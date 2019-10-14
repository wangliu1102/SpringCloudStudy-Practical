/**
 * 参数设置初始化
 */
var Config = {
    id: "ConfigTable",	//表格id
    seItem: null,		//选中的条目
    seItemList: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格
 */
Config.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '参数主键', field: 'id', align: 'center', valign: 'middle', width: '150px', visible: false},
        {title: '参数名称', field: 'configName', align: 'center', valign: 'middle', sortable: true},
        {title: '参数键名', field: 'configKey', align: 'center', valign: 'middle', sortable: true},
        {title: '参数键值', field: 'configValue', align: 'center', valign: 'middle', sortable: true},
        {title: '系统内置', field: 'configType', align: 'center', valign: 'middle', sortable: true},
        {title: '备注', field: 'remark', align: 'center', valign: 'middle', sortable: true},
        {title: '创建者', field: 'createBy', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true}]
};

/**
 * 检查是否选中
 */
Config.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    } else if (selected.length == 1) {
    	Config.seItemList = selected;
    	Config.seItem = selected[0];
        return true;
    } else {
    	Config.seItemList = selected;
        return true;
    }
};

/**
 * 点击添加参数
 */
Config.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加加班',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: HuiShu.ctxPath + '/config/config_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看参数详情
 */
Config.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '加班详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/config/config_update/' + Config.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除参数
 */
Config.delete = function () {
    if (this.check()) {
        var operation = function () {
            var ajax = new $ax(HuiShu.ctxPath + "/config/delete", function () {
                HuiShu.success("删除成功!");
                Config.table.refresh();
            }, function (data) {
                HuiShu.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", Config.seItem.id);
            ajax.start();
        };

        HuiShu.confirm("是否刪除选中加班记录？", operation);
    }
};

/**
 * 查询参数列表
 */
Config.search = function () {
    var queryData = {};
    queryData['configName'] = $("#configName").val().trim();
    queryData['configKey'] = $("#configKey").val().trim();
    queryData['configType'] = $("#configType").val().trim();
    queryData['startTime'] = $("#startTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    Config.table.refresh({query: queryData});
};

/**
 * 重置查询参数列表
 */
Config.resetSearch = function () {

    $("#configName").val("");
    $("#configKey").val("");
    $("#configType").val("");
    $("#beginTime").val("");
    $("#startTime").val("");
    $("#endTime").val("");
    Config.search();
};

$(function () {
    var defaultColunms = Config.initColumn();
    var table = new BSTable(Config.id, "/config/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    Config.table = table;
});