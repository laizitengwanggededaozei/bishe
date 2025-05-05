package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.Team;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface TeamMapper extends BaseMapper<Team> {



    @Select("SELECT * FROM team WHERE id NOT IN (SELECT team_id FROM participation_record WHERE match_id = #{matchId}) AND id IN (SELECT team_id FROM team_member WHERE uid = #{userId} AND role = '队长')")
    List<Team> findEligibleTeams(@Param("userId") String userId, @Param("matchId") int matchId);
    @Select("SELECT t.*, (SELECT tm.uname FROM team_member tm WHERE tm.team_id = t.id AND tm.role = '队长' LIMIT 1) AS leaderName " +
            "FROM team t " +
            "WHERE t.id IN (SELECT team_id FROM team_member WHERE uid = #{userId})")
    List<Team> getTeamsByUserId(@Param("userId") String userId);

    @Select("SELECT t.*, (SELECT tm.uname FROM team_member tm WHERE tm.team_id = t.id AND tm.role = '队长' LIMIT 1) AS leaderName " +
            "FROM team t " +
            "WHERE t.id = #{id}")
    Team getTeamById(@Param("id") int id);

    @Insert("INSERT INTO team (tname, logo) VALUES (#{tname}, #{logo})")
    int insertTeam(@Param("tname") String tname, @Param("logo") String logo);
    @Select("SELECT IFNULL(MAX(id), 0) FROM team")
    int getMaxTeamId();
    @Select("SELECT t.*, (SELECT tm.uname FROM team_member tm WHERE tm.team_id = t.id AND tm.role = '队长' LIMIT 1) AS leaderName " +
            "FROM team t " +
            "WHERE t.id IN (SELECT team_id FROM team_member WHERE uid = #{userId}) " +
            "AND t.id IN (SELECT team_id FROM participation_record WHERE match_id = #{matchId})")
    List<Team> getParticipatingTeamsForUser(@Param("userId") String userId, @Param("matchId") int matchId);

    // 在TeamMapper接口中添加
    @Select("SELECT t.*, (SELECT tm.uname FROM team_member tm WHERE tm.team_id = t.id AND tm.role = '队长' LIMIT 1) AS leaderName " +
            "FROM team t " +
            "WHERE t.id IN (SELECT team_id FROM team_member WHERE uid = #{teacherId} AND role = '教师')")
    List<Team> getTeamsGuidedByTeacher(@Param("teacherId") String teacherId);

    @Select("SELECT t.id as teamId, t.tname as teamName, tm.uname as leaderName, " +
            "pr.match_id as matchId, pr.registration_time as registrationTime, " +
            "pr.status, pr.canceled_by as canceledBy, pr.cancel_time as cancelTime " +
            "FROM participation_record pr " +
            "JOIN team t ON pr.team_id = t.id " +
            "JOIN team_member tm ON t.id = tm.team_id AND tm.role = '队长' " +
            "WHERE t.id IN (SELECT team_id FROM team_member WHERE uid = #{teacherId} AND role = '教师') " +
            "AND pr.match_id = #{matchId}")
    List<Map<String, Object>> getTeamRegistrationsForTeacher(@Param("teacherId") String teacherId, @Param("matchId") int matchId);
    @Select("SELECT t.*, (SELECT tm.uname FROM team_member tm WHERE tm.team_id = t.id AND tm.role = '队长' LIMIT 1) AS leaderName " +
            "FROM team t " +
            "WHERE t.id IN (SELECT team_id FROM team_member WHERE uid = #{userId}) " +
            "AND t.id NOT IN (SELECT team_id FROM participation_record WHERE match_id = #{matchId} AND status = 'ACTIVE')")
    List<Team> getEligibleTeamsForUser(@Param("userId") String userId, @Param("matchId") int matchId);

    @Select("SELECT t.* FROM team t " +
            "JOIN participation_record pr ON t.id = pr.team_id " +
            "JOIN team_member tm ON t.id = tm.team_id " +
            "WHERE tm.uid = #{userId} AND pr.match_id = #{matchId} " +
            "AND pr.status = 'ACTIVE' LIMIT 1")
    Team getRegisteredTeamForMatch(@Param("userId") String userId, @Param("matchId") int matchId);

}
