package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.ParticipationRecord;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ParticipationRecordMapper extends BaseMapper<ParticipationRecord> {

    @Select("SELECT COUNT(*) > 0 FROM participation_record WHERE team_id = #{teamId} AND match_id = #{matchId}")
    boolean existsByTeamIdAndMatchId(@Param("teamId") int teamId, @Param("matchId") int matchId);
    // 在ParticipationRecordMapper接口中修改或添加
    @Delete("DELETE FROM participation_record WHERE team_id = #{teamId} AND match_id = #{matchId}")
    int deleteByCompositeKey(@Param("teamId") int teamId, @Param("matchId") int matchId);
}
