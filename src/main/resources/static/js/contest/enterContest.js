/**
 * 模块JavaScript
 */
var enterContestPage = {
    data:{
        pageNum: 0,
        pageSize: 0,
        totalPageNum: 0,
        totalPageSize: 0,
        subjectName: "",
        contests: [],
    },
    init: function (pageNum, pageSize, totalPageNum, totalPageSize, subjectName, contests) {
        enterContestPage.data.pageNum = pageNum;
        enterContestPage.data.pageSize = pageSize;
        enterContestPage.data.totalPageNum = totalPageNum;
        enterContestPage.data.totalPageSize = totalPageSize;
        enterContestPage.data.subjectName = subjectName;
        enterContestPage.data.contests = contests;
        //分页初始化
        enterContestPage.subPageMenuInit();

        /**
         TODO::代码规范,折叠菜单效果
         */
        $('.ui.accordion').accordion(
            {
                exclusive: true,/*不可打开多节*/
            }
        );

    },
    firstPage: function () {
        window.location.href = app.URL.enterContestUrl() + '?page=1';
    },
    prevPage: function () {
        window.location.href = app.URL.enterContestUrl() + '?page=' + (pageNum-1);
    },
    targetPage: function (page) {
        window.location.href = app.URL.enterContestUrl() + '?page=' + page;
    },
    nextPage: function () {
        window.location.href = app.URL.enterContestUrl() + '?page=' + (pageNum+1);
    },
    lastPage: function () {
        window.location.href = app.URL.enterContestUrl() + '?page=' + enterContestPage.data.totalPageNum;
    },
    subPageMenuInit: function(){
        var subPageStr = '';
        if (enterContestPage.data.pageNum == 1) {
            subPageStr += '<a class="item disabled">首页</a>';
            subPageStr += '<a class="item disabled">上一页</a>';
        } else {
            subPageStr += '<a onclick="enterContestPage.firstPage()" class="item">首页</a>';
            subPageStr += '<a onclick="enterContestPage.prevPage()" class="item">上一页</a>';
        }
        var startPage = (enterContestPage.data.pageNum-4 > 0 ? enterContestPage.data.pageNum-4 : 1);
        var endPage = (startPage+7 > enterContestPage.data.totalPageNum ? enterContestPage.data.totalPageNum : startPage+7);
        console.log('startPage = ' + startPage);
        console.log('endPage = ' + endPage);
        console.log('pageNum = ' + enterContestPage.data.pageNum);
        console.log('totalPageNum = ' + enterContestPage.data.totalPageNum);
        for (var i = startPage; i <= endPage; i++) {
            if (i == enterContestPage.data.pageNum) {
                subPageStr += '<a onclick="enterContestPage.targetPage('+i+')" class="active item">'+i+'</a>';
            } else {
                subPageStr += '<a onclick="enterContestPage.targetPage('+i+')" class="item">'+i+'</a>'
            }
        }
        if (enterContestPage.data.pageNum == enterContestPage.data.totalPageNum) {
            subPageStr += '<a class="item disabled">下一页</a>';
            subPageStr += '<a class="item disabled">末页</a>';
        } else {
            subPageStr += '<a onclick="enterContestPage.nextPage()" class="item">下一页</a>';
            subPageStr += '<a onclick="enterContestPage.lastPage()" class="item">末页</a>';
        }
        $('#subPageMenu').html(subPageStr);
    },


};


/**
 * 加入课程
 */
joinContest = function (contestId, studentId) {
    if(confirm("确认要加入该课程吗")){
        $.ajax({
            url: "/mycontest/api/join",
            type:"POST",
            contentType : "application/json;charset=UTF-8",
            dataType: "json",
            data:JSON.stringify({
                studentId: studentId,
                contestId: contestId,
            }),

            success:function(result){
                window.location.reload();
                console.log(result);
            }
        });
    }
}
//查询
queryContest  = function () {
    var title = $('#content').val();
    window.location.href = app.URL.enterContestUrl() + '?page=1&title='+title;
}