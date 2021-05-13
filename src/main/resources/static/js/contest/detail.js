/**
 * 模块JavaScript
 */
var contestDetailPage = {
    data:{
        contest: null,
        questions: [],
        currentQuestionIndex: 0,
    },
    init: function (contest, questions) {
        contestDetailPage.data.contest = contest;
        contestDetailPage.data.questions = questions;

        //console.log(contestDetailPage.data.questions);

        //TODO::考试时间倒计时
        $("#contestTimeCountdown").countdown(new Date(contestDetailPage.data.contest.endTime), function (event) {
            // 事件格式
            var format = event.strftime("%D:%H:%M:%S");
            //console.log(format);
            $("#contestTimeCountdown").html(format);
        }).on('finish.countdown', function () {
            // TODO::交卷事件触发
            contestDetailPage.finishContestAction();
        });
        //答题区
        if (contestDetailPage.data.questions[0].questionType == 0) {
            $('#currentQuetionTitle').html('(单选)'+contestDetailPage.data.questions[0].content+'(2分)');
            var selectOptionStr = '<div class="grouped fields">\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="A"/>\n' +
                '        <label>A.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionA+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="B"/>\n' +
                '        <label>B.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionB+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="C"/>\n' +
                '        <label>C.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionC+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="D"/>\n' +
                '        <label>D.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionD+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '  </div>\n';


            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length <= 1){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction(1)" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);
        }
        else if (contestDetailPage.data.questions[0].questionType == 1) {
            $('#currentQuetionTitle').html('(填空)'+contestDetailPage.data.questions[0].content+'(5分)(答案从左往后依次，英文分号隔开;)');
            var selectOptionStr = '<div class="field">\n' +
                '                        <textarea  id="questionAnswer" rows="5"></textarea>\n' +
                '                    </div>';

            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length <= 1){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction(1)" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);
            // var selectOptionStr = '<div class="grouped fields">\n' +
            //     '    <div class="field">\n' +
            //     '      <div class="ui toggle checkbox">\n' +
            //     '        <input type="checkbox" name="questionAnswer" value="A"/>\n' +
            //     '        <label>A.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionA+'</label>\n' +
            //     '      </div>\n' +
            //     '    </div>\n' +
            //     '    <div class="field">\n' +
            //     '      <div class="ui toggle checkbox">\n' +
            //     '        <input type="checkbox" name="questionAnswer" value="B"/>\n' +
            //     '        <label>B.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionB+'</label>\n' +
            //     '      </div>\n' +
            //     '    </div>\n' +
            //     '    <div class="field">\n' +
            //     '      <div class="ui toggle checkbox">\n' +
            //     '        <input type="checkbox" name="questionAnswer" value="C"/>\n' +
            //     '        <label>C.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionC+'</label>\n' +
            //     '      </div>\n' +
            //     '    </div>\n' +
            //     '    <div class="field">\n' +
            //     '      <div class="ui toggle checkbox">\n' +
            //     '        <input type="checkbox" name="questionAnswer" value="D"/>\n' +
            //     '        <label>D.&nbsp;&nbsp;'+contestDetailPage.data.questions[0].optionD+'</label>\n' +
            //     '      </div>\n' +
            //     '    </div>\n' +
            //     '  </div>';
            // $('#currentQuestionAnswer').html(selectOptionStr);
        }
        else if (contestDetailPage.data.questions[0].questionType == 2){
            $('#currentQuetionTitle').html('(简答)'+contestDetailPage.data.questions[0].content+'(10分)');
            var selectOptionStr = '<div class="field">\n' +
                '                        <textarea  id="questionAnswer" rows="20"></textarea>\n' +
                '                    </div>';
            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length <= 1){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction(1)" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;


            $('#currentQuestionAnswer').html(selectOptionStr);
        }
        else if (contestDetailPage.data.questions[0].questionType == 3){
            $('#currentQuetionTitle').html('(应用)'+contestDetailPage.data.questions[0].content+'(20分)');
            var selectOptionStr = '<div class="field">\n' +
                '                        <textarea  id="questionAnswer" rows="20"></textarea>\n' +
                '                    </div>';

            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length <= 1){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction(1)" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);
        }
        else if (contestDetailPage.data.questions[0].questionType == 4){
            $('#currentQuetionTitle').html('(判断)'+contestDetailPage.data.questions[0].content+'(1分)');
            var selectOptionStr = '<div class="grouped fields">\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="T"/>\n' +
                '        <label>T.&nbsp;&nbsp;是</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="F"/>\n' +
                '        <label>F.&nbsp;&nbsp;否</label>\n' +
                '      </div>\n' +
                '    </div>';

            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length <= 1){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction(1)" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);
        }
        //下一题按钮
        // var nextQuestionButtonStr = '';
        // if(questions.length <= 1)nextQuestionButtonStr = '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
        //     else nextQuestionButtonStr = '<button type="button" onclick="contestDetailPage.targetQuestionAction(1)" class="ui positive button">下一题</button>';
        //     $('#nextQuestionButton').html(nextQuestionButtonStr);
        //答题卡
        var currentQuestionButtonStr = '';
        for (var i = 0; i < questions.length; i++) {
            var buttonStr = '';
            if (contestDetailPage.data.currentQuestionIndex == i) buttonStr = '<button class="mini ui positive button" style="margin-top: 10px;margin-left: 5px;">'+(i+1)+'</button>';
            else buttonStr = '<button class="mini ui button" onclick="contestDetailPage.targetQuestionAction('+i+')" style="margin-top: 10px;margin-left: 5px;">'+(i+1)+'</button>';
            currentQuestionButtonStr += buttonStr;
        }
        $('#currentQuestionButton').html(currentQuestionButtonStr);
    },
    targetQuestionAction: function (index) {
        var preIndex = contestDetailPage.data.currentQuestionIndex;
        contestDetailPage.data.currentQuestionIndex = index;
        console.log(index);

        //记录答案
        if (contestDetailPage.data.questions[preIndex].questionType == 0) {
            contestDetailPage.data.questions[preIndex].answer = '';
            $.each($("input[name='questionAnswer']:checked"),function(){
                console.log($(this).val());
                contestDetailPage.data.questions[preIndex].answer += $(this).val();
            });
        }
        else if (contestDetailPage.data.questions[preIndex].questionType == 1) {
            console.log($("#questionAnswer").val());
            contestDetailPage.data.questions[preIndex].answer = $("#questionAnswer").val();
        }
        else if(contestDetailPage.data.questions[preIndex].questionType == 2){
            console.log($("#questionAnswer").val());
            contestDetailPage.data.questions[preIndex].answer = $("#questionAnswer").val();
        }
        else if(contestDetailPage.data.questions[preIndex].questionType == 3){
            console.log($("#questionAnswer").val());
            contestDetailPage.data.questions[preIndex].answer = $("#questionAnswer").val();
        }
        else if(contestDetailPage.data.questions[preIndex].questionType == 4){
            contestDetailPage.data.questions[preIndex].answer = '';
            $.each($("input[name='questionAnswer']:checked"),function(){
                console.log($(this).val());
                contestDetailPage.data.questions[preIndex].answer += $(this).val();
            });
        }

        if (contestDetailPage.data.questions[index].questionType == 0) {
            $('#currentQuetionTitle').html('(单选)'+contestDetailPage.data.questions[index].content+'(2分)');
            var selectOptionStr = '<div class="grouped fields">\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="A"/>\n' +
                '        <label>A.&nbsp;&nbsp;'+contestDetailPage.data.questions[index].optionA+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="B"/>\n' +
                '        <label>B.&nbsp;&nbsp;'+contestDetailPage.data.questions[index].optionB+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="C"/>\n' +
                '        <label>C.&nbsp;&nbsp;'+contestDetailPage.data.questions[index].optionC+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="D"/>\n' +
                '        <label>D.&nbsp;&nbsp;'+contestDetailPage.data.questions[index].optionD+'</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '  </div>';
            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length-1 == index){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction('+(index+1)+')" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;


            $('#currentQuestionAnswer').html(selectOptionStr);

            //显示之前作答区的答案
            if (contestDetailPage.data.questions[index].answer != '') {
                $.each($("input[name='questionAnswer']"),function(){
                    if (contestDetailPage.data.questions[index].answer.indexOf($(this).val()) != -1) {
                        $(this).attr("checked", "checked");
                    }
                });
            }
        }
        else if (contestDetailPage.data.questions[index].questionType == 1) {
            $('#currentQuetionTitle').html('(填空)'+contestDetailPage.data.questions[index].content+'(5分)(答案从左往后依次，英文分号隔开;)');
            var selectOptionStr = '<div class="field">\n' +
                '                        <textarea  id="questionAnswer" rows="5"></textarea>\n' +
                '                    </div>';
            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length-1 == index){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction('+(index+1)+')" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);

            //显示之前作答区的答案
            if (contestDetailPage.data.questions[index].answer != '') {
                $('#questionAnswer').val(contestDetailPage.data.questions[index].answer);
            }
        }
        else if(contestDetailPage.data.questions[index].questionType == 2){
            $('#currentQuetionTitle').html('(简答)'+contestDetailPage.data.questions[index].content+'(10分)');
            var selectOptionStr = '<div class="field">\n' +
                '                        <textarea  id="questionAnswer" name="questionAnswer" rows="20"></textarea>\n' +
                '                    </div>';
            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length-1 == index){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction('+(index+1)+')" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);

            //显示之前作答区的答案
            if (contestDetailPage.data.questions[index].answer != '') {
                $('#questionAnswer').val(contestDetailPage.data.questions[index].answer);
            }
        }
        else if(contestDetailPage.data.questions[index].questionType == 3){
            $('#currentQuetionTitle').html('(应用)'+contestDetailPage.data.questions[index].content+'(20分)');
            var selectOptionStr = '<div class="field">\n' +
                '                        <textarea  id="questionAnswer" name="questionAnswer" rows="20"></textarea>\n' +
                '                    </div>';
            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length-1 == index){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction('+(index+1)+')" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);

            //显示之前作答区的答案
            if (contestDetailPage.data.questions[index].answer != '') {
                $('#questionAnswer').val(contestDetailPage.data.questions[index].answer);
            }
        }
        else if(contestDetailPage.data.questions[index].questionType == 4){
            $('#currentQuetionTitle').html('(判断)'+contestDetailPage.data.questions[0].content+'(1分)');
            var selectOptionStr = '<div class="grouped fields">\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="T"/>\n' +
                '        <label>T.&nbsp;&nbsp;是</label>\n' +
                '      </div>\n' +
                '    </div>\n' +
                '    <div class="field">\n' +
                '      <div class="ui toggle checkbox">\n' +
                '        <input type="radio" name="questionAnswer" value="F"/>\n' +
                '        <label>F.&nbsp;&nbsp;否</label>\n' +
                '      </div>\n' +
                '    </div>';
            //下一题按钮
            var nextQuestionButtonStr = '';
            if(questions.length-1 == index){
                nextQuestionButtonStr ='<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
            }
            else {
                nextQuestionButtonStr = '<br/>'+'<br/>'+'<br/>'+
                    '<button type="button" onclick="contestDetailPage.targetQuestionAction('+(index+1)+')" class="ui positive button">下一题</button>';
            }
            selectOptionStr += nextQuestionButtonStr;

            $('#currentQuestionAnswer').html(selectOptionStr);

            //显示之前作答区的答案
            if (contestDetailPage.data.questions[index].answer != '') {
                $.each($("input[name='questionAnswer']"),function(){
                    if (contestDetailPage.data.questions[index].answer.indexOf($(this).val()) != -1) {
                        $(this).attr("checked", "checked");
                    }
                });
            }
        }

        // var nextQuestionButtonStr = '';
        // if(questions.length-1 == index)nextQuestionButtonStr = '<button type="button" onclick="contestDetailPage.finishContestAction()" class="ui positive button">交卷</button>';
        // else nextQuestionButtonStr = '<button type="button" onclick="contestDetailPage.targetQuestionAction('+index+1+')" class="ui positive button">下一题</button>';
        // $('#nextQuestionButton').html(nextQuestionButtonStr);


        var currentQuestionButtonStr = '';
        for (var i = 0; i < contestDetailPage.data.questions.length; i++) {
            var buttonStr = '';
            if (contestDetailPage.data.currentQuestionIndex == i) {
                buttonStr = '<button class="mini ui positive button" style="margin-top: 10px;margin-left: 5px;">'+(i+1)+'</button>';
            } else if (contestDetailPage.data.questions[i].answer != '') {
                // console.log(i);
                // console.log(contestDetailPage.data.questions[i]);
                buttonStr = '<button class="mini ui green basic button" onclick="contestDetailPage.targetQuestionAction('+i+')" style="margin-top: 10px;margin-left: 5px;">'+(i+1)+'</button>';
            } else {
                buttonStr = '<button class="mini ui button" onclick="contestDetailPage.targetQuestionAction('+i+')" style="margin-top: 10px;margin-left: 5px;">'+(i+1)+'</button>';
            }
            currentQuestionButtonStr += buttonStr;
        }
        $('#currentQuestionButton').html(currentQuestionButtonStr);
    },
    //交卷事假触发
    finishContestAction: function () {
        if(confirm("确认要提交试卷吗")) {
            var index = contestDetailPage.data.currentQuestionIndex;
            //记录答案
            if (contestDetailPage.data.questions[index].questionType == 0) {
                contestDetailPage.data.questions[index].answer = '';
                $.each($("input[name='questionAnswer']:checked"), function () {
                    console.log($(this).val());
                    contestDetailPage.data.questions[index].answer += $(this).val();
                });
            }
            else if (contestDetailPage.data.questions[index].questionType == 4) {
                contestDetailPage.data.questions[index].answer = '';
                $.each($("input[name='questionAnswer']:checked"), function () {
                    console.log($(this).val());
                    contestDetailPage.data.questions[index].answer += $(this).val();
                });
            }
            else if (contestDetailPage.data.questions[index].questionType == 1) {
                console.log($("#questionAnswer").val());
                contestDetailPage.data.questions[index].answer = $("#questionAnswer").val();
            }
            else if (contestDetailPage.data.questions[index].questionType == 2) {
                console.log($("#questionAnswer").val());
                contestDetailPage.data.questions[index].answer = $("#questionAnswer").val();
            }
            else if (contestDetailPage.data.questions[index].questionType == 3) {
                console.log($("#questionAnswer").val());
                contestDetailPage.data.questions[index].answer = $("#questionAnswer").val();
            }
            //TODO::交卷
            contestDetailPage.submittingContestAction();
        }
    },
    //正在交卷
    submittingContestAction: function () {
        $('#waitSubmitModal').modal({
            /**
             * 必须点击相关按钮才能关闭
             */
            closable  : false,
            /**
             * 模糊背景
             */
            blurring: true,
        }).modal('show');
        for (var i = 0; i < contestDetailPage.data.questions.length; i++) {
            console.log(contestDetailPage.data.questions[i].answer);
        }

        var answerJsonStr = '';
        for (var i = 0; i < contestDetailPage.data.questions.length; i++) {
            answerJsonStr += contestDetailPage.data.questions[i].answer;
            if (i < contestDetailPage.data.questions.length-1) {
                answerJsonStr += '_~_';
            }
        }
        console.log("answerJson = " + answerJsonStr);

        //向后端API发送答题卡
        $.ajax({
            url : app.URL.submitGradeUrl(),
            type : "POST",
            dataType: "json",
            contentType : "application/json;charset=UTF-8",
            <!-- 向后端传输的数据 -->
            data : JSON.stringify({
                contestId: contestDetailPage.data.contest.id,
                answerJson: answerJsonStr,
            }),
            success:function(result) {
                if (result && result['success']) {
                } else {
                    //TODO::发送答题卡出错处理
                    console.log(result.message);
                }
            },
            error:function(result){
                //TODO::发送答题卡出错处理
                console.log(result.message);
            }
        });

        setTimeout(function () {
            $("#waitSubmitModal").modal("hide");
            window.location.href = app.URL.contestIndexUrl();
        }, 5000);
    },
};