package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EvaluationMapper extends BaseMapper<Evaluation> {
    
    @Select("SELECT * FROM evaluation WHERE submission_id = #{submissionId}")
    List<Evaluation> selectEvaluationsBySubmissionId(@Param("submissionId") Integer submissionId);
    
    @Select("SELECT AVG(score) FROM evaluation WHERE submission_id = #{submissionId}")
    Double selectAverageScoreBySubmissionId(@Param("submissionId") Integer submissionId);
}