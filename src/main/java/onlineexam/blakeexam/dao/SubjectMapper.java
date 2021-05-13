package onlineexam.blakeexam.dao;

import onlineexam.blakeexam.entity.Subject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public interface SubjectMapper {

    int insertSubject(@Param("subject") Subject subject);

    int updateSubjectById(@Param("subject") Subject subject);

    Subject getSubjectById(@Param("id") int id);

    int getCount();

    List<Subject> getSubjects();

    int deleteSubjectById(@Param("id") int id);

}
