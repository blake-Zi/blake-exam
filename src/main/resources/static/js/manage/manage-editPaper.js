/**
 * 模块化JavaScript
 **/
var manageEditContestProblemPage = {
    data:{
        contest: null,
    },
    init: function (contest) {
        manageEditContestProblemPage.data.contest = contest;
        
        
        
        
        $('#export').click(function () {
            window.location.href="/contest/export/"+contest.id;
        });
    },


    



}
