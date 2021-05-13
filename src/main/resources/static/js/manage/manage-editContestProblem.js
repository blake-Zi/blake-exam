/**
 * 模块化JavaScript
 **/
var manageEditContestProblemPage = {
    data:{
        contest: null,
        questions: [],
        cqs: [],
    },
    init: function (contest, questions, cqs) {
        manageEditContestProblemPage.data.contest = contest;
        manageEditContestProblemPage.data.questions = questions;
        manageEditContestProblemPage.data.cqs = cqs;

        //
        // //手动组卷
        // $("#ManualPaperBtn").click(function () {
        //     //输入框初始化数据
        //    // manageEditContestProblemPage.initManualPaperData();
        //     $("#manualPaperModal").modal({
        //         keyboard : false,
        //         show : true,
        //         backdrop : "static"
        //     });
        // });
        //
        //
        //
        // //手动组卷，取消
        // $('#cancelManualPaperBtn').click(function(){
        //     $("#manualPaperModal").modal('hide');
        // });
        //
        // //手动组卷，确定
        // $('#confirmtoManualPaperBtn').click(function(){
        //     manageEditContestProblemPage.ManualPaperAction();
        // });









    },
    initManualPaperData: function () {
        //初始化数据
        $('#cq').val([]);

    },

    ManualPaperModalAction: function () {
        //编辑考试，弹出编辑窗口
        //console.log(index);
        //输入框初始化数据
        //manageQuestionBoardPage.initUpdateQuestionData(index);
        $("#manualPaperModal").modal({
            keyboard : false,
            show : true,
            backdrop : "static"
        });
    },

    // ManualPaperAction: function () {
    //     var cq = [];
    //     $("input[name='cq']:cq").each(function (i) {
    //         cq[i] = $(this).val() ;
    //     })
    //
    //
    //         $.ajax({
    //             url : app.URL.ManualPaperUrl(),
    //             type : "POST",
    //             dataType: "json",
    //             contentType : "application/json;charset=UTF-8",
    //             <!-- 向后端传输的数据 -->
    //             data : JSON.stringify({
    //                 cq : cq,
    //             }),
    //             success:function(result) {
    //                 if (result && result['success']) {
    //                     // 验证通过 刷新页面
    //                     window.location.reload();
    //                 } else {
    //                     $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
    //                         '                <p>'+result.message+'</p>');
    //                     $('#loginModalErrorMessage').removeClass('hidden');
    //                 }
    //             },
    //             error:function(result){
    //                 $('#loginModalErrorMessage').html('<i class="close icon"></i><div class="header">错误提示</div>\n' +
    //                     '                <p>'+result.message+'</p>');
    //                 $('#loginModalErrorMessage').removeClass('hidden');
    //             }
    //         });
    //
    // },

    deletePaperQuestionAction: function (contestId, id) {
        $.ajax({
            url : app.URL.deletePaperQuestionUrl()+contestId+"/"+id,
            type : "DELETE",
            dataType: "json",
            contentType : "application/json;charset=UTF-8",
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
    },






}



/**
 * 删除题目
 */
deleteProblem = function(contestId, id){
    if(confirm("确认要删除该选择题么")){
        $.ajax({
            url: "/contest/"+contestId+"/deleteProblem/"+id,
            type:"DELETE",
            success:function(result){
                window.location.reload();
                console.log(result);
            }
        });
    }
}

/**
 * 手动选题
 * @returns {boolean}
 */
manual = function(){
    $("#manualPaper").ajaxSubmit(function(result) {
        console.log(result);
        if(result.success){
            window.location.reload();
        }
        else {
            alert(result.message);
        }
    });
    $("#manualPaperModal").modal('hide')
    return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转
}



/**
 * 自动选题
 * @returns {boolean}
 */
auto = function(){
    $("#autoPaper").ajaxSubmit(function(result) {
        console.log(result);
        if(result.success){
            window.location.reload();
        }
        else {
            alert(result.message);
        }
    });
    $("#autoPaperModal").modal('hide')
    return false; // 必须返回false，否则表单会自己再做一次提交操作，并且页面跳转
}

/**
 * 更换新增试卷类型
 */
changeType = function(text){
    if(text.value==="choiceinfo"){
        $("#choiceinfo").css('display','');
        $("#compinfo").css('display','none');
        $("#judgeinfo").css('display','none');
        $("#designinfo").css('display','none');
        $("#appinfo").css('display','none');

    }
    else if(text.value==="compinfo"){
        $("#choiceinfo").css('display','none');
        $("#compinfo").css('display','');
        $("#judgeinfo").css('display','none');
        $("#designinfo").css('display','none');
        $("#appinfo").css('display','none');
    }
    else if(text.value==="judgeinfo"){
        $("#choiceinfo").css('display','none');
        $("#compinfo").css('display','none');
        $("#judgeinfo").css('display','');
        $("#designinfo").css('display','none');
        $("#appinfo").css('display','none');
    }else if(text.value==="designinfo"){
        $("#choiceinfo").css('display','none');
        $("#compinfo").css('display','none');
        $("#judgeinfo").css('display','none');
        $("#designinfo").css('display','');
        $("#appinfo").css('display','none');
    } else if(text.value==="appinfo"){
        $("#choiceinfo").css('display','none');
        $("#compinfo").css('display','none');
        $("#judgeinfo").css('display','none');
        $("#designinfo").css('display','none');
        $("#appinfo").css('display','');
    }
}

