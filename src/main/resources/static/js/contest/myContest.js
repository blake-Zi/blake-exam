/**
 * 模块JavaScript
 */
var myContestsPage = {
    data:{
        pageNum: 0,
        pageSize: 0,
        totalPageNum: 0,
        totalPageSize: 0,
        myClasses: [],
    },
    init: function (pageNum, pageSize, totalPageNum, totalPageSize, myClasses) {
        myContestsPage.data.pageNum = pageNum;
        myContestsPage.data.pageSize = pageSize;
        myContestsPage.data.totalPageNum = totalPageNum;
        myContestsPage.data.totalPageSize = totalPageSize;
        myContestsPage.data.myClasses = myClasses;
        //分页初始化
        myContestsPage.subPageMenuInit();

    },



    firstPage: function () {
        window.location.href = app.URL.myContestsUrl() + '?page=1';
    },
    prevPage: function () {
        window.location.href = app.URL.myContestsUrl() + '?page=' + (pageNum-1);
    },
    targetPage: function (page) {
        window.location.href = app.URL.myContestsUrl() + '?page=' + page;
    },
    nextPage: function () {
        window.location.href = app.URL.myContestsUrl() + '?page=' + (pageNum+1);
    },
    lastPage: function () {
        window.location.href = app.URL.myContestsUrl() + '?page=' + myContestsPage.data.totalPageNum;
    },
    subPageMenuInit: function(){
        var subPageStr = '';
        if (myContestsPage.data.pageNum == 1) {
            subPageStr += '<a class="item disabled">首页</a>';
            subPageStr += '<a class="item disabled">上一页</a>';
        } else {
            subPageStr += '<a onclick="myContestsPage.firstPage()" class="item">首页</a>';
            subPageStr += '<a onclick="myContestsPage.prevPage()" class="item">上一页</a>';
        }
        var startPage = (myContestsPage.data.pageNum-4 > 0 ? myContestsPage.data.pageNum-4 : 1);
        var endPage = (startPage+7 > myContestsPage.data.totalPageNum ? myContestsPage.data.totalPageNum : startPage+7);
        console.log('startPage = ' + startPage);
        console.log('endPage = ' + endPage);
        console.log('pageNum = ' + myContestsPage.data.pageNum);
        console.log('totalPageNum = ' + myContestsPage.data.totalPageNum);
        for (var i = startPage; i <= endPage; i++) {
            if (i == myContestsPage.data.pageNum) {
                subPageStr += '<a onclick="myContestsPage.targetPage('+i+')" class="active item">'+i+'</a>';
            } else {
                subPageStr += '<a onclick="myContestsPage.targetPage('+i+')" class="item">'+i+'</a>'
            }
        }
        if (myContestsPage.data.pageNum == myContestsPage.data.totalPageNum) {
            subPageStr += '<a class="item disabled">下一页</a>';
            subPageStr += '<a class="item disabled">末页</a>';
        } else {
            subPageStr += '<a onclick="myContestsPage.nextPage()" class="item">下一页</a>';
            subPageStr += '<a onclick="myContestsPage.lastPage()" class="item">末页</a>';
        }
        $('#subPageMenu').html(subPageStr);
    },

};

/**
 * 退出课程
 */
outContest = function (id) {
    if(confirm("确认要加入该课程吗")){
        $.ajax({
            url: "/mycontest/api/out/"+id,
            type:"DELETE",
            success:function(result){
                window.location.reload();
                console.log(result);
            }
        });
    }
}