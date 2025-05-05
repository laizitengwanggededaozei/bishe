package neu.competition.service;

import neu.competition.entity.Team;
import neu.competition.entity.TeamMember;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface TeamService {
    @Transactional
    void createTeam(int newTeamId, Team team, String studentMembers, String studentRoles, String teacherMembers, MultipartFile logoFile, String currentUserId) throws Exception;

    int getMaxTeamId();

    List<TeamMember> getStudentMembersByTeamId(int teamId);

    List<TeamMember> getTeacherMembersByTeamId(int teamId);

    List<Team> getEligibleTeams(String userId, int matchId);

    void registerTeamForMatch(int teamId, int matchId);

    List<TeamMember> getTeamMembersByTeamId(int teamId);

    boolean isTeamRegisteredForCompetition(int teamId, int matchId);
    List<Team> getParticipatingTeamsForUser(String userId, int matchId);


    @Transactional
    void updateTeam(int teamId, Team team, String studentMembers, String teacherMembers, String leaderMember, MultipartFile logoFile) throws Exception;

    List<Team> getMyTeamsByUserId(String userId);

    Team getTeamById(int teamId);

    boolean addMember(String memberType, String userId, int teamId);

    TeamMember getTeamLeaderByTeamId(int teamId);

    void registerTeamForCompetition(int teamId, int matchId);

    List<Team> getEligibleTeamsForUser(String userId, int matchId);

    @Transactional
    void changeTeamLeader(int teamId, String newLeaderId);
    boolean hasParticipatingTeam(String userId, int matchId);
    List<Team> getTeamsGuidedByTeacher(String teacherId);
    // 获取用户在指定比赛已报名的团队
    Team getRegisteredTeamForMatch(String userId, int matchId);
    // 检查用户是否是团队队长
    boolean isTeamLeader(String userId, int teamId);

    // 检查用户是否是团队教师
    boolean isTeamTeacher(String userId, int teamId);

    // 撤销团队报名
    void cancelTeamRegistration(int teamId, int matchId, String cancelUserId);

    // 获取教师指导的团队在指定比赛的报名情况
    List<Map<String, Object>> getTeamRegistrationsForTeacher(String teacherId, int matchId);
}
