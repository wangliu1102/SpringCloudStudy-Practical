/**
 * 签到签退
 */
var SignIn = {
    tableId: "logTable",   //表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 点击查询按钮
 */
SignIn.search = function () {
    var queryData = {};
    queryData['beginTime'] = $("#beginTime").val();
    queryData['endTime'] = $("#endTime").val();
    queryData['logName'] = $("#logName").val();
    queryData['logType'] = $("#logType").val();
    table.reload(SignIn.tableId, {where: queryData});
};

/**
 * 导出excel按钮
 */
SignIn.exportExcel = function () {
    var checkRows = table.checkStatus(SignIn.tableId);
    if (checkRows.data.length === 0) {
        HuiShu.error("请选择要导出的数据");
    } else {
        table.exportFile(tableResult.config.id, checkRows.data, 'xls');
    }
};

//渲染时间选择框
laydate.render({
    elem: '#beginTime'
});

//渲染时间选择框
laydate.render({
    elem: '#endTime'
});

// 签到按钮点击事件
$('#signIn').click(function () {
    SignIn.signInClick();

});

/**
 * 签到
 */
SignIn.signInClick = function () {
    var ajax = new $ax(HuiShu.ctxPath + "/signIn/signForIn/", function (data) {
        HuiShu.success(data.data.message);
        setTimeout(location.reload(), 3000);
    }, function (data) {
        HuiShu.error("打卡异常!");
    });
    ajax.start();
};

// 签退按钮点击事件
$('#signOut').click(function () {
    SignIn.signOutClick();
});

/**
 * 签退
 */
SignIn.signOutClick = function () {

   var operation= function () {
    var ajax = new $ax(HuiShu.ctxPath + "/signIn/signForOut/", function (data) {
        HuiShu.success(data.data.message);
        setTimeout(reload(), 3000);
    }, function (data) {
        HuiShu.error("打卡异常!");
    });
    ajax.start();
   }

    //标准下班时间 17:30
    var myDate = new Date();
    myDate.setHours(17,30,0);
    //当前时间
    var nowDate=new Date();
    if(nowDate<myDate){
        HuiShu.confirm("未到下班时间，是否确认签退" , operation);
    }else {
        operation();
    }
};

function reload(){
    location.reload();
}


$(function () {
    $("#lated").hide();
    var late = $("#late").val();
    if(late=="LATE"){
        $("#lated").show();
    }
});




