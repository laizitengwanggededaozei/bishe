package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.DTO.ProblemDTO;
import neu.competition.entity.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProblemMapper extends BaseMapper<Problem> {
    
    @Select("SELECT p.*, m.match_name FROM problem p JOIN matches m ON p.match_id = m.match_id WHERE p.match_id = #{matchId}")
    List<ProblemDTO> selectProblemsByMatchId(@Param("matchId") Integer matchId);
    
    @Select("SELECT p.*, m.match_name FROM problem p JOIN matches m ON p.match_id = m.match_id WHERE p.problem_id = #{problemId}")
    ProblemDTO selectProblemById(@Param("problemId") Integer problemId);
}