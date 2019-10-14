/**
 * 周报管理初始化
 */
var Weekly = {
    id: "WeeklyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Weekly.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', align: 'center', valign: 'middle',width:'50px' ,visible:false},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '内容', field: 'content', align: 'center', valign: 'middle', sortable: true},
        {title: '创建人', field: 'createBy', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true}];
};

/**
 * 检查是否选中
 */
Weekly.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Weekly.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加周报
 */
Weekly.openAddDept = function () {
    var index = layer.open({
        type: 2,
        title: '添加周报',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: HuiShu.ctxPath + '/weekly/weekly_add'
    });
    this.layerIndex = index;
};

/**
 * 打开修改周报详情
 */
Weekly.openDeptDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '周报详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/weekly/weekly_update/' + Weekly.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 打开查看周报详情
 */
Weekly.openWeeklyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '周报详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: HuiShu.ctxPath + '/weekly/weekly_detail/' + Weekly.seItem.id
        });
        this.layerIndex = index;
    }
};
/**
 * 删除周报
 */
Weekly.delete = function () {
    if (this.check()) {
        var operation = function(){
            var ajax = new $ax(HuiShu.ctxPath + "/weekly/delete", function () {
                HuiShu.success("删除成功!");
                Weekly.table.refresh();
            }, function (data) {
                HuiShu.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id",Weekly.seItem.id);
            ajax.start();
        };

        HuiShu.confirm("是否刪除"+Weekly.seItem.title, operation);
    }
};

/**
 * 查询周报列表
 */
Weekly.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val().trim();
    queryData['startTime'] = $("#startTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    Weekly.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Weekly.initColumn();
    var table = new BSTable(Weekly.id, "/weekly/list", defaultColunms);
    table.setPaginationType("server");
    table.init();
    Weekly.table = table;
});
