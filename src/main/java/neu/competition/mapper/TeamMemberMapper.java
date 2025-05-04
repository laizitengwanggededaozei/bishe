package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.TeamMember;
import neu.competition.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TeamMemberMapper extends BaseMapper<TeamMember> {

    @Insert("INSERT INTO team_member (uid, team_id, uname, role) VALUES (#{uid}, #{teamId}, #{uname}, #{role})")
    int insertTeamMember(@Param("uid") String uid, @Param("teamId") int teamId, @Param("uname") String uname, @Param("role") String role);

    @Select("SELECT tm.*, u.uname FROM team_member tm " +
            "JOIN user u ON tm.uid = u.id " +
            "WHERE tm.team_id = #{teamId} AND tm.role = '队员'")
    List<TeamMember> getStudentMembersByTeamId(@Param("teamId") int teamId);

    @Select("SELECT tm.*, u.uname FROM team_member tm " +
            "JOIN user u ON tm.uid = u.id " +
            "WHERE tm.team_id = #{teamId} AND tm.role = '教师'")
    List<TeamMember> getTeacherMembersByTeamId(@Param("teamId") int teamId);

    @Select("SELECT tm.*, u.uname FROM team_member tm " +
            "JOIN user u ON tm.uid = u.id " +
            "WHERE tm.team_id = #{teamId}")
    List<TeamMember> getTeamMembersByTeamId(@Param("teamId") int teamId);

    @Select("SELECT tm.*, u.uname FROM team_member tm " +
            "JOIN user u ON tm.uid = u.id " +
            "WHERE tm.team_id = #{teamId} AND tm.role = '队长'")
    TeamMember getTeamLeaderByTeamId(@Param("teamId") int teamId);

    @Delete("DELETE FROM team_member WHERE team_id = #{teamId}")
    void deleteByTeamId(@Param("teamId") int teamId);

    @Select("SELECT * FROM team_member WHERE uid = #{uid} AND team_id = #{teamId}")
    TeamMember getTeamMemberByIdAndTeamId(@Param("uid") String uid, @Param("teamId") int teamId);

    @Update("UPDATE team_member SET role = #{role} WHERE uid = #{uid} AND team_id = #{teamId}")
    void updateById(@Param("teamId") int teamId, @Param("uid") String uid, @Param("role") String role);

    @Update("UPDATE team_member SET role = #{role} WHERE uid = #{uid} AND team_id = #{teamId}")
    int updateById(@Param("uid") String uid, @Param("teamId") int teamId, @Param("role") String role);
}
