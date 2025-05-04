package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.DTO.SubmissionDTO;
import neu.competition.entity.Submission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SubmissionMapper extends BaseMapper<Submission> {
    
    @Select("SELECT s.*, t.tname as team_name, p.title as problem_title, m.match_name " +
            "FROM submission s " +
            "JOIN team t ON s.team_id = t.id " +
            "JOIN problem p ON s.problem_id = p.problem_id " +
            "JOIN matches m ON s.match_id = m.match_id " +
            "WHERE s.match_id = #{matchId}")
    List<SubmissionDTO> selectSubmissionsByMatchId(@Param("matchId") Integer matchId);
    @Select("SELECT s.*, t.tname as team_name, p.title as problem_title, m.match_name " +
            "FROM submission s " +
            "JOIN team t ON s.team_id = t.id " +
            "JOIN problem p ON s.problem_id = p.problem_id " +
            "JOIN matches m ON s.match_id = m.match_id " +
            "WHERE s.match_id = #{matchId} AND (s.status = 'PENDING' OR s.status = 'SUBMITTED')")
    List<SubmissionDTO> selectPendingSubmissionsByMatchId(@Param("matchId") Integer matchId);
    @Select("SELECT s.*, t.tname as team_name, p.title as problem_title, m.match_name " +
            "FROM submission s " +
            "JOIN team t ON s.team_id = t.id " +
            "JOIN problem p ON s.problem_id = p.problem_id " +
            "JOIN matches m ON s.match_id = m.match_id " +
            "WHERE s.team_id = #{teamId} AND s.match_id = #{matchId}")
    List<SubmissionDTO> selectSubmissionsByTeamAndMatch(@Param("teamId") Integer teamId, @Param("matchId") Integer matchId);
    
    @Select("SELECT s.*, t.tname as team_name, p.title as problem_title, m.match_name " +
            "FROM submission s " +
            "JOIN team t ON s.team_id = t.id " +
            "JOIN problem p ON s.problem_id = p.problem_id " +
            "JOIN matches m ON s.match_id = m.match_id " +
            "WHERE s.submission_id = #{submissionId}")
    SubmissionDTO selectSubmissionById(@Param("submissionId") Integer submissionId);
}