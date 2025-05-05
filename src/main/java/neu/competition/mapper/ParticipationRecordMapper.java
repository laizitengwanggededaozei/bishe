package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.ParticipationRecord;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;

@Mapper
public interface ParticipationRecordMapper extends BaseMapper<ParticipationRecord> {

    @Select("SELECT COUNT(*) > 0 FROM participation_record WHERE team_id = #{teamId} AND match_id = #{matchId}")
    boolean existsByTeamIdAndMatchId(@Param("teamId") int teamId, @Param("matchId") int matchId);

    // 在ParticipationRecordMapper接口中修改或添加
    @Delete("DELETE FROM participation_record WHERE team_id = #{teamId} AND match_id = #{matchId}")
    int deleteByCompositeKey(@Param("teamId") int teamId, @Param("matchId") int matchId);

    @Select("SELECT * FROM participation_record WHERE team_id = #{teamId} AND match_id = #{matchId}")
    ParticipationRecord selectOneByTeamAndMatch(@Param("teamId") int teamId, @Param("matchId") int matchId);

    @Update("UPDATE participation_record SET status = #{status}, canceled_by = #{canceledBy}, " +
            "cancel_time = #{cancelTime} WHERE team_id = #{teamId} AND match_id = #{matchId}")
    int updateStatusByTeamAndMatch(@Param("teamId") int teamId, @Param("matchId") int matchId,
                                   @Param("status") String status, @Param("canceledBy") String canceledBy,
                                   @Param("cancelTime") LocalDateTime cancelTime);
}
