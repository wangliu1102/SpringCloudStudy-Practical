/**
 * 报销管理
 */
var Expense = {
    id: "expenseTable",//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Expense.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'id', field: 'id', visible: false, align: 'center', valign: 'middle'},
        {title: '标题', field: 'title', align: 'center', valign: 'middle', sortable: true},
        {title: '金额(元)', field: 'money', align: 'center', valign: 'middle', sortable: true},
        {title: '备注', field: 'comment', align: 'center', valign: 'middle', sortable: true},
        {title: '创建人', field: 'createName', align: 'center', valign: 'middle', sortable: true},
        {title: '创建时间', field: 'createTime', align: 'center', valign: 'middle', sortable: true},
        {title: '修改时间', field: 'updateTime', align: 'center', valign: 'middle', sortable: true},
        {
            title: '操作',
            formatter: function (value, row, index) {
                return "<a style='color: blue;' href='javascript:void(0)' onclick='Expense.downloadFile(" + row.id + ")' >下载</a>";
            },
            align: 'center', valign: 'middle', sortable: false
        }
    ];
    return columns;
};

/**
 * 检查是否选中
 */
Expense.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        HuiShu.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Expense.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加报销
 */
Expense.openAddExpense = function () {
    var index = layer.open({
        type: 2,
        title: '添加报销',
        area: ['800px', '400px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: HuiShu.ctxPath + '/expense/expense_add'
    });
    this.layerIndex = index;
};


Expense.openChangeExpense = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '报销详情',
            area: ['800px', '620px'], //宽高
            fix: false, //不固定
            content: HuiShu.ctxPath + '/expense/expense_update/' + Expense.seItem.id
        });
        this.layerIndex = index;
    }
};
/**
 * 删除报销
 */
Expense.delExpense = function () {
    if (this.check()) {
        var operation = function () {
            var expenseId = Expense.seItem.id;
            var ajax = new $ax(HuiShu.ctxPath + "/expense/delete", function () {
                HuiShu.success("删除成功!");
                Expense.table.refresh();
            }, function (data) {
                HuiShu.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("expenseId", expenseId);
            ajax.start();
        };
        HuiShu.confirm("是否删除该报销?", operation);
    }
};

Expense.search = function () {
    var queryData = {};
    queryData['title'] = $("#title").val().trim();
    queryData['beginTime'] = $("#beginTime").val().trim();
    queryData['endTime'] = $("#endTime").val().trim();
    Expense.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Expense.initColumn();
    var table = new BSTable("expenseTable", "/expense/list", defaultColunms);
    table.setPaginationType("client");
    Expense.table = table.init();
});

/**
 * 下载附件
 */
Expense.downloadFile = function (expenseId) {
    var ajax = new $ax(HuiShu.ctxPath + "/expense/downloadFile", function (data) {
            window.location.href = HuiShu.ctxPath + data;
    }, function (data) {
        HuiShu.error("下载失败," + data.responseJSON.message);
    });
    ajax.set("expenseId", expenseId);
    ajax.start();

};
