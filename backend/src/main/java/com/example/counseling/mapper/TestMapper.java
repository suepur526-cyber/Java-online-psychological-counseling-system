package com.example.counseling.mapper;

import com.example.counseling.model.PsychologicalTest;
import com.example.counseling.model.TestQuestion;
import com.example.counseling.model.TestRecord;
import org.apache.ibatis.annotations.*;
import java.util.List;

public interface TestMapper {
    @Select("select * from psychological_tests order by id desc")
    List<PsychologicalTest> findTests();

    @Select("select * from psychological_tests where status='ACTIVE' order by id desc")
    List<PsychologicalTest> findActiveTests();

    @Select("select * from psychological_tests where id=#{id}")
    PsychologicalTest findTest(Long id);

    @Insert("insert into psychological_tests(name,duration_minutes,status,description) values(#{name},#{durationMinutes},#{status},#{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertTest(PsychologicalTest test);

    @Update("update psychological_tests set name=#{name}, duration_minutes=#{durationMinutes}, status=#{status}, description=#{description} where id=#{id}")
    int updateTest(PsychologicalTest test);

    @Select("select * from test_questions where test_id=#{testId} order by sequence_no desc, id asc")
    List<TestQuestion> findQuestions(Long testId);

    @Select("select * from test_questions where id=#{id}")
    TestQuestion findQuestion(Long id);

    @Insert("insert into test_questions(test_id,question_name,options_json,score,answer,analysis,type,sequence_no) values(#{testId},#{questionName},#{optionsJson},#{score},#{answer},#{analysis},#{type},#{sequenceNo})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertQuestion(TestQuestion question);

    @Update("update test_questions set question_name=#{questionName}, options_json=#{optionsJson}, score=#{score}, answer=#{answer}, analysis=#{analysis}, type=#{type}, sequence_no=#{sequenceNo} where id=#{id}")
    int updateQuestion(TestQuestion question);

    @Delete("delete from test_questions where id=#{id}")
    int deleteQuestion(Long id);

    @Insert("insert into test_records(user_id,username,test_id,test_name,total_score,result_level,answers_json) values(#{userId},#{username},#{testId},#{testName},#{totalScore},#{resultLevel},#{answersJson})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRecord(TestRecord record);

    @Select("select * from test_records order by id desc")
    List<TestRecord> findRecords();

    @Select("select * from test_records where user_id=#{userId} order by id desc")
    List<TestRecord> findRecordsByUser(Long userId);
}
