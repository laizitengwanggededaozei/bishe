package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.DTO.ResultDTO;
import neu.competition.entity.Result;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ResultMapper extends BaseMapper<Result> {
    
    @Select("SELECT r.*, t.tname as team_name, m.match_name " +
            "FROM result r " +
            "JOIN team t ON r.team_id = t.id " +
            "JOIN matches m ON r.match_id = m.match_id " +
            "WHERE r.match_id = #{matchId} " +
            "ORDER BY r.rank ASC")
    List<ResultDTO> selectResultsByMatchId(@Param("matchId") Integer matchId);
    
    @Select("SELECT r.*, t.tname as team_name, m.match_name " +
            "FROM result r " +
            "JOIN team t ON r.team_id = t.id " +
            "JOIN matches m ON r.match_id = m.match_id " +
            "WHERE r.team_id = #{teamId} AND r.match_id = #{matchId}")
    ResultDTO selectResultByTeamAndMatch(@Param("teamId") Integer teamId, @Param("matchId") Integer matchId);
}