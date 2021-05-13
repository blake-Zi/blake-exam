/**
 * 模块化JavaScript
 **/
var manageQuestionBoardPage = {
    data:{
        pageNum: 0,
        pageSize: 0,
        totalPageNum: 0,
        totalPageSize: 0,
        content: "",
        questions: [],
    },
    init: function (pageNum, pageSize, totalPageNum, totalPageSize, content, questions) {
        manageQuestionBoardPage.data.pageNum = pageNum;
        manageQuestionBoardPage.data.pageSize = pageSize;
        manageQuestionBoardPage.data.totalPageNum = totalPageNum;
        manageQuestionBoardPage.data.totalPageSize = totalPageSize;
        manageQuestionBoardPage.data.content = content;
        manageQuestionBoardPage.data.questions = questions;
        //分页初始化
        manageQuestionBoardPage.subPageMenuInit();

        $('#content').val(content);

        //新增题目，弹出新增窗口
        $("#addQuestionBtn").click(function () {
            //输入框初始化数据
            manageQuestionBoardPage.initAddQuestionData();
            $("#addQuestionModal").modal({
                keyboard : false,
                show : true,
                backdrop : "static"
            });
        });


        //弹出批量新增题目窗口
        $("#addQuestionsBtn").click(function () {
            $("#addQuestionsModal").modal({
                keyboard : false,
                show : true,
                backdrop : "static"
            });
        });

        //下载模板
        $('#export').click(function () {
            window.location.href="/question/export"
        });

        //批量新增题目，取消题目增加
        $('#cancelAddQuestionsBtn').click(function(){
            $("#addQuestionsModal").modal('hide');
        });

        //批量新增题目，确定增加题目
        $('#confirmAddQuestionsBtn').click(function(){
            manageQuestionBoardPage.addQuestionsAction();
        });


        //新增题目，取消题目增加
        $('#cancelAddQuestionBtn').click(function(){
            $("#addQuestionModal").modal('hide');
        });

        //新增题目，确定增加题目
        $('#confirmAddQuestionBtn').click(function(){
            manageQuestionBoardPage.addQuestionAction();
        });

        //编辑题目，取消题目编辑
        $('#cancelUpdateQuestionBtn').click(function(){
            $("#updateQuestionModal").modal('hide');
        });

        //编辑题目，确定保存考试
        $('#confirmUpdateQuestionBtn').click(function(){
            manageQuestionBoardPage.updateQuestionAction();
        });

        //查詢按鈕触发
        $('#queryQuestionBtn').click(function () {
            manageQuestionBoardPage.queryQuestionAction();
        });

        //关闭错误框
        $('#addModalErrorMessage,.close').on('click', function() {
            $(this).closest('#addModalErrorMessage').transition('fade');
        });

    },
    firstPage: function () {
        var content = $('#content').val();
        window.location.href = app.URL.manageQuestionUrl() + '?page=1&content='+content;
    },
    prevPage: function () {
        var content = $('#content').val();
        window.location.href = app.URL.manageQuestionUrl() + '?page=' + (pageNum-1)
            + '&content='+content;
    },
    targetPage: function (page) {
        var content = $('#content').val();
        window.location.href = app.URL.manageQuestionUrl() + '?page='
            + page + '&content='+content;
    },
    nextPage: function () {
        var content = $('#content').val();
        window.location.href = app.URL.manageQuestionUrl() + '?page=' + (pageNum+1)
            + '&content='+content;
    },
    lastPage: function () {
        var content = $('#content').val();
        window.location.href = app.URL.manageQuestionUrl() + '?page='
            + manageQuestionBoardPage.data.totalPageNum + '&content='+content;
    },
    subPageMenuInit: function(){
        var subPageStr = '<ul class="pagination">';
        if (manageQuestionBoardPage.data.pageNum == 1) {
            subPageStr += '<li class="disabled"><a><span>首页</span></a></li>';
            subPageStr += '<li class="disabled"><a><span>上一页</span></a></li>';
        } else {
            subPageStr += '<li><a onclick="manageQuestionBoardPage.firstPage()"><span>首页</span></a></li>';
            subPageStr += '<li><a onclick="manageQuestionBoardPage.prevPage()"><span>上一页</span></a></li>';
        }
        var startPage = (manageQuestionBoardPage.data.pageNum-4 > 0 ? manageQuestionBoardPage.data.pageNum-4 : 1);
        var endPage = (startPage+7 > manageQuestionBoardPage.data.totalPageNum ? manageQuestionBoardPage.data.totalPageNum : startPage+7);
        console.log('startPage = ' + startPage);
        console.log('endPage = ' + endPage);
        console.log('pageNum = ' + manageQuestionBoardPage.data.pageNum);
        console.log('totalPageNum = ' + manageQuestionBoardPage.data.totalPageNum);
        for (var i = startPage; i <= endPage; i++) {
            if (i == manageQuestionBoardPage.data.pageNum) {
                subPageStr += '<li class="active"><a onclick="manageQuestionBoardPage.targetPage('+i+')">'+i+'</a></li>';
            } else {
                subPageStr += '<li><a onclick="manageQuestionBoardPage.targetPage('+i+')">'+i+'</a></li>';
            }
        }
        if (manageQuestionBoardPage.data.pageNum == manageQuestionBoardPage.data.totalPageNum) {
            subPageStr += '<li class="disabled"><a><span>下一页</span></a></li>';
            subPageStr += '<li class="disabled"><a><span>末页</span></a></li>';
        } else {
            subPageStr += '<li><a onclick="manageQuestionBoardPage.nextPage()"><span>下一页</span></a></li>';
            subPageStr += '<li><a onclick="manageQuestionBoardPage.lastPage()"><span>末页</span></a></li>';
        }
        $('#subPageHead').html(subPageStr);
    },
    initAddQuestionData: function () {
        //初始化数据
        $('#questionTitle').val("");
        $('#questionContent').val("");
        $('#optionA').val("");
        $('#optionB').val("");
        $('#optionC').val("");
        $('#optionD').val("");
        $('#questionAnswer').val("");
        $('#questionParse').val("");
        // $('#questionScore').val("");
    },
    checkAddQuestionData: function (questionTitle, questionContent, questionType,
                                    optionA, optionB, optionC, optionD,
                                    questionAnswer, questionParse, questionDifficulty,
                                    questionValue) {
        //TODO::校验

        if (questionTitle == null || questionTitle == ''
            || questionTitle.replace(/(^s*)|(s*$)/g, "").length == 0) {
            //TODO::信息校验
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目标题不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        if (questionContent == null || questionContent == ''
            || questionContent.replace(/(^s*)|(s*$)/g, "").length == 0) {
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目内容不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        if (questionAnswer == null || questionAnswer == ''
            || questionAnswer.replace(/(^s*)|(s*$)/g, "").length == 0) {
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目答案不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        if (questionParse == null || questionParse == ''
            || questionParse.replace(/(^s*)|(s*$)/g, "").length == 0) {
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目解析不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        return true;

    },
    addQuestionAction: function () {
        var questionTitle = $('#questionTitle').val();
        var questionContent = $('#questionContent').val();
        var questionType = $('#questionType').val();
        var subjectId = $('#subjectId').val();
        var optionA = $('#optionA').val();
        var optionB = $('#optionB').val();
        var optionC = $('#optionC').val();
        var optionD = $('#optionD').val();
        var questionAnswer = $('#questionAnswer').val();
        var questionParse = $('#questionParse').val();
        var questionDifficulty = $('#questionDifficulty').val();
        // var questionScore = $('#questionScore').val();

        if (manageQuestionBoardPage.checkAddQuestionData(questionTitle, questionContent,
                questionType, optionA,optionB, optionC, optionD, questionAnswer, questionParse,
                questionDifficulty)) {
            $.ajax({
                url : app.URL.addQuestionUrl(),
                type : "POST",
                dataType: "json",
                contentType : "application/json;charset=UTF-8",
                <!-- 向后端传输的数据 -->
                data : JSON.stringify({
                    title: questionTitle,
                    content: questionContent,
                    questionType: questionType,
                    optionA: optionA,
                    optionB: optionB,
                    optionC: optionC,
                    optionD: optionD,
                    answer: questionAnswer,
                    parse: questionParse,
                    subjectId: subjectId,
                    contestId: 0,
                    // score: questionScore,
                    difficulty: questionDifficulty,
                }),
                success:function(result) {
                    if (result && result['success']) {
                        // 验证通过 刷新页面
                        window.location.reload();
                    } else {
                        $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                            '                <p>'+result.message+'</p>');
                        $('#loginModalErrorMessage').removeClass('hidden');
                    }
                },
                error:function(result){
                    $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                        '                <p>'+result.message+'</p>');
                    $('#loginModalErrorMessage').removeClass('hidden');
                }
            });
        }
    },
    //编辑题目模态框触发
    updateQuestionModalAction: function (index) {
        //编辑考试，弹出编辑窗口
        //console.log(index);
        //输入框初始化数据
        manageQuestionBoardPage.initUpdateQuestionData(index);
        $("#updateQuestionModal").modal({
            keyboard : false,
            show : true,
            backdrop : "static"
        });
    },
    initUpdateQuestionData: function (index) {
        //初始化数据
        $('#updateQuestionIndex').val(index);
        $('#updateQuestionTitle').val(questions[index].title);
        $('#updateQuestionContent').val(questions[index].content);
        var selectQuestionTypes = document.getElementById('updateQuestionType');
        for (var i = 0; i < selectQuestionTypes.length; i++) {
            if (selectQuestionTypes[i].value == questions[index].questionType) {
                selectQuestionTypes[i].selected = true;
            }
        }
        var selectSubjectIds = document.getElementById('updateSubjectId');
        for (var i = 0; i < selectSubjectIds.length; i++) {
            if (selectSubjectIds[i].value == questions[index].subjectId) {
                selectSubjectIds[i].selected = true;
            }
        }
        $('#updateOptionA').val(questions[index].optionA);
        $('#updateOptionB').val(questions[index].optionB);
        $('#updateOptionC').val(questions[index].optionC);
        $('#updateOptionD').val(questions[index].optionD);
        $('#updateQuestionAnswer').val(questions[index].answer);
        $('#updateQuestionParse').val(questions[index].parse);
        var selectQuestionDifficulty = document.getElementById('updateQuestionDifficulty');
        for (var i = 0; i < selectQuestionDifficulty.length; i++) {
            if (selectQuestionDifficulty[i].value == questions[index].difficulty) {
                selectQuestionDifficulty[i].selected = true;
            }
        }
        // $('#updateQuestionScore').val(questions[index].score);
    },
    checkUpdateQuestionData: function (questionTitle, questionContent, questionType,
                                       optionA, optionB, optionC, optionD,
                                       questionAnswer, questionParse, questionDifficulty
                                       ) {
        //TODO::校验
        if (questionTitle == null || questionTitle == ''
            || questionTitle.replace(/(^s*)|(s*$)/g, "").length == 0) {
            //TODO::信息校验
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目标题不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        if (questionContent == null || questionContent == ''
            || questionContent.replace(/(^s*)|(s*$)/g, "").length == 0) {
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目内容不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        if (questionAnswer == null || questionAnswer == ''
            || questionAnswer.replace(/(^s*)|(s*$)/g, "").length == 0) {
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目答案不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        if (questionParse == null || questionParse == ''
            || questionParse.replace(/(^s*)|(s*$)/g, "").length == 0) {
            $('#addModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>'+'题目解析不能为空'+'</p>');
            $('#addModalErrorMessage').removeClass('hidden');
            return false;
        }
        return true;

    },
    updateQuestionAction: function () {
        var index = $('#updateQuestionIndex').val();
        var questionTitle = $('#updateQuestionTitle').val();
        var questionContent = $('#updateQuestionContent').val();
        var questionType = $('#updateQuestionType').val();
        var subjectId = $('#updateSubjectId').val();
        var optionA = $('#updateOptionA').val();
        var optionB = $('#updateOptionB').val();
        var optionC = $('#updateOptionC').val();
        var optionD = $('#updateOptionD').val();
        var questionAnswer = $('#updateQuestionAnswer').val();
        var questionParse = $('#updateQuestionParse').val();
        var questionDifficulty = $('#updateQuestionDifficulty').val();
        // var questionScore = $('#updateQuestionScore').val();

        if (manageQuestionBoardPage.checkUpdateQuestionData(questionTitle, questionContent,
                questionType, optionA,optionB, optionC, optionD, questionAnswer, questionParse,
                questionDifficulty)) {
            $.ajax({
                url : app.URL.updateQuestionUrl(),
                type : "POST",
                dataType: "json",
                contentType : "application/json;charset=UTF-8",
                <!-- 向后端传输的数据 -->
                data : JSON.stringify({
                    id: questions[index].id,
                    title: questionTitle,
                    content: questionContent,
                    questionType: questionType,
                    optionA: optionA,
                    optionB: optionB,
                    optionC: optionC,
                    optionD: optionD,
                    answer: questionAnswer,
                    parse: questionParse,
                    subjectId: subjectId,
                    contestId: 0,
                    // score: questionScore,
                    difficulty: questionDifficulty,
                }),
                success:function(result) {
                    if (result && result['success']) {
                        // 验证通过 刷新页面
                        window.location.reload();
                    } else {
                        $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                            '                <p>'+result.message+'</p>');
                        $('#loginModalErrorMessage').removeClass('hidden');
                    }
                },
                error:function(result){
                    $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                        '                <p>'+result.message+'</p>');
                    $('#loginModalErrorMessage').removeClass('hidden');
                }
            });
        }
    },
    deleteQuestionAction: function (index) {
        if(confirm("确认要删除该题目吗")) {
            $.ajax({
                url: app.URL.deleteQuestionUrl() + index,
                type: "DELETE",
                dataType: "json",
                contentType: "application/json;charset=UTF-8",
                success: function (result) {
                    if (result && result['success']) {
                        // 验证通过 刷新页面
                        window.location.reload();
                    } else {
                        $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                            '                <p>' + result.message + '</p>');
                        $('#loginModalErrorMessage').removeClass('hidden');
                    }
                },
                error: function (result) {
                    $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                        '                <p>' + result.message + '</p>');
                    $('#loginModalErrorMessage').removeClass('hidden');
                }
            });
        }
    },
    //查询
    queryQuestionAction: function () {
        var content = $('#content').val();
        window.location.href = app.URL.manageQuestionUrl() + '?page=1&content='+content;
    },

    //批量添加
    addQuestionsAction: function () {
        var fileName = $('#myfile').val();　　　　　　　　　　　　　　　　　//获得文件名称
        var fileType1 = fileName.substr(fileName.length-5,fileName.length);　　//截取文件类型,如(.xlsx)
        var fileType2 = fileName.substr(fileName.length-4,fileName.length);    //截取文件类型,如(.xls)
        if((fileType1=='.xlsx' || fileType1=='.xls') || (fileType2=='.xlsx' || fileType2=='.xls')){　　　　　//验证文件类型,此处验证也可使用正则
            var formData = new FormData();
            formData.append('file', $('#myfile')[0].files[0]);
            formData.append('subjectId',$('#subject').val());
            $.ajax({
                url: "/question/api/addQuestions",　　　　　　　　　　//上传地址
                type: 'POST',
                cache: false,
                data: formData,　　　　　　　　　　　　　//表单数据
                processData: false,
                contentType: false,
                success:function(result){

                    if (result) {
                        // 验证通过 刷新页面
                        window.location.reload();
                    } else {
                        $('#updateAccountErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                            '                <p>'+result.message+'</p>');
                        $('#updateAccountErrorMessage').removeClass('hidden');
                    }

                }
            });
        }else{
            $('#updateAccountErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
                '                <p>*上传文件类型错误,支持类型: .xlsx .xls</p>');
            $('#updateAccountErrorMessage').removeClass('hidden');
        }
    },



};