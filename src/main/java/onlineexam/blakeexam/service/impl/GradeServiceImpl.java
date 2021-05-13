package onlineexam.blakeexam.service.impl;

import com.github.pagehelper.PageHelper;
import onlineexam.blakeexam.dao.GradeMapper;
import onlineexam.blakeexam.entity.Grade;
import onlineexam.blakeexam.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("gradeService")
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public boolean addGrade(Grade grade) {
        return gradeMapper.insertGrade(grade) > 0;
    }

    @Override
    public boolean updateGrade(Grade grade) {
        grade.setFinishTime(new Date());
        return gradeMapper.updateGradeById(grade) > 0;
    }

    @Override
    public boolean deleteGrade(int id) {
        return gradeMapper.deleteGrade(id) > 0;
    }

    @Override
    public Grade getGradeById(int id) {
        return gradeMapper.getGradeById(id);
    }

    @Override
    public Map<String, Object> getGradesByStudentId(int pageNum, int pageSize, int studentId) {
        Map<String, Object> data = new HashMap<>();
        int count = gradeMapper.getCountByStudentId(studentId);
        if(count == 0){
            data.put("pageNum", 0);
            data.put("pageSize",0);
            data.put("totalPageNum", 1);
            data.put("totalPageSize", 0);
            data.put("gradeSize", 0);
            return data;
        }
        int totalPageNum = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
        if(totalPageNum < pageNum){
            data.put("pageNum", 0);
            data.put("pageSize", 0);
            data.put("totalPageNum", totalPageNum);
            data.put("totalPageSize", 0);
            data.put("gradeSize", 0);
            data.put("grades", new ArrayList<>());
            return data;
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Grade> grades = gradeMapper.getGradesByStudentId(studentId);
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        data.put("totalPageNum", totalPageNum);
        data.put("totalPageSize", count);
        data.put("gradeSize", grades.size());
        data.put("grades", grades);
        return data;
    }

    @Override
    public List<Grade> getGradesByContestId(int contestId) {
        return gradeMapper.getGradesByContestId(contestId);
    }

    @Override
    public boolean deleteGradesByContestId(int contestId) {
        return gradeMapper.deleteGradesByContestId(contestId) > 0;
    }

    @Override
    public boolean deleteGradesByStudentId(int studentId) {
        return gradeMapper.deleteGradesByStudentId(studentId) > 0;
    }
}
