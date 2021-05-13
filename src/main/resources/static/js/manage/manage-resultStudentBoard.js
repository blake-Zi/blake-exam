/**
 * 模块化JavaScript
 **/
var manageResultStudentBoardPage = {
    data:{
        contest: null,
        questions: [],
        myClasses: [],
    },
    init: function (contest, questions, myClasses) {
        manageResultStudentBoardPage.data.contest = contest;
        manageResultStudentBoardPage.data.questions = questions;
        manageResultStudentBoardPage.data.myClasses = myClasses;

        //评分，取消分数编辑
        $('#cancelUpdateGradeBtn').click(function(){
            $("#updateGradeModal").modal('hide');
        });

        //评分，确定保存分数
        $('#confirmUpdateGradeBtn').click(function(){
            manageResultStudentBoardPage.updateGradeAction();
        });
    },
    initUpdateGradeData: function (index) {
        var questions = manageResultStudentBoardPage.data.questions;
        var myClass = manageResultStudentBoardPage.data.myClasses[index];
        var grade = myClass.grade;
        var answers = grade.answerJson.split("_~_");
        var score = 5;
        console.log(answers);

        $('#gradeTitle').html(myClass.student.name+'的主观题答题卡');

        var gradeBodyStr = '<input type="hidden" id="updateGradeIndex" value="'+index+'"/>';
        for (var i = 0; i < questions.length; i++) {
            if (questions[i].questionType > 0 && questions[i].questionType < 4) {
                if(questions[i].questionType == 1) score = 5;
                else if(questions[i].questionType == 2) score = 10;
                else score = 20;
                gradeBodyStr += '<div class="row">\n' +
                    '                    <form class="form-horizontal" role="form" onsubmit="return false;">\n' +
                    '                        <div class="form-group">\n' +
                    '                            <label class="col-sm-2 control-label">题号</label>\n' +
                    '                            <div class="col-sm-8">\n' +
                    '                                <input value="'+(i+1)+(questions[i].title)+'" readonly="readonly" type="text" class="form-control" placeholder="" />\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                        <div class="form-group">\n' +
                    '                            <label class="col-sm-2 control-label">考生答案</label>\n' +
                    '                            <div class="col-sm-8">\n' +
                    '                                <textarea onscroll="this.rows++;" class="form-control" readonly="readonly">'+answers[i]+'</textarea>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                        <div class="form-group">\n' +
                    '                            <label class="col-sm-2 control-label">参考答案</label>\n' +
                    '                            <div class="col-sm-8">\n' +
                    '                                <textarea onscroll="this.rows++;" class="form-control" readonly="readonly">'+questions[i].answer+'</textarea>\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                        <div class="form-group">\n' +
                    '                            <label class="col-sm-2 control-label">分值</label>\n' +
                    '                            <div class="col-sm-8">\n' +
                    '                                <input  value="'+(score)+'" readonly="readonly" type="text" class="form-control" placeholder="" />\n' +
                    '                            </div>\n' +
                    '                        </div>\n' +
                    '                    </form>\n' +
                    '                    <h5 class="page-header"></h5>\n' +
                    '                </div>';
            }
        }
        gradeBodyStr += '<div class="row">\n' +
            '                    <form class="form-horizontal" role="form" onsubmit="return false;">\n' +
            '                        <div class="form-group">\n' +
            '                            <label class="col-sm-2 control-label">主观题得分</label>\n' +
            '                            <div class="col-sm-8">\n' +
            '                                <input id="gradeManulResult" type="text" class="form-control" placeholder="" />\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </form>\n' +
            '                </div>';
        $('#gradeBody').html(gradeBodyStr);
    },
    //编辑成绩模态框触发
    updateGradeModalAction: function (index) {
        //编辑考试，弹出编辑窗口
        //console.log(index);
        //输入框初始化数据
        manageResultStudentBoardPage.initUpdateGradeData(index);
        $("#updateGradeModal").modal({
            keyboard : false,
            show : true,
            backdrop : "static"
        });
    },
    updateGradeAction: function () {
        var index = $('#updateGradeIndex').val();
        var manulResult = $('#gradeManulResult').val();
        $.ajax({
            url : app.URL.finishGradeUrl(),
            type : "POST",
            dataType: "json",
            contentType : "application/json;charset=UTF-8",
            <!-- 向后端传输的数据 -->
            data : JSON.stringify({
                id: myClasses[index].grade.id,
                studentId: myClasses[index].grade.studentId,
                contestId: myClasses[index].grade.contestId,
                result: myClasses[index].grade.result,
                autoResult: myClasses[index].grade.autoResult,
                manulResult: manulResult,
                answerJson: myClasses[index].grade.answerJson,
                createTime: myClasses[index].grade.createTime,
            }),
            success:function(result) {
                if (result && result['success']) {
                    // 验证通过 刷新页面
                    window.location.reload();
                } else {
                    console.log(result.message);
                }
            },
            error:function(result){
                console.log(result.message);
            }
        });
    },
    finishContestAction: function (contestId) {
        $.ajax({
            url : app.URL.finishContestUrl()+contestId,
            type : "POST",
            dataType: "json",
            contentType : "application/json;charset=UTF-8",
            success:function(result) {
                if (result && result['success']) {
                    // 验证通过 刷新页面
                    window.location.reload();
                } else {
                    console.log(result.message);
                }
            },
            error:function(result){
                console.log(result.message);
            }
        });
    },
};