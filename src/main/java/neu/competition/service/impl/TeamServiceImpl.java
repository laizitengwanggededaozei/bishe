package neu.competition.service.impl;

import neu.competition.controller.RegistrationController;
import neu.competition.entity.ParticipationRecord;
import neu.competition.entity.Team;
import neu.competition.entity.TeamMember;
import neu.competition.entity.User;
import neu.competition.mapper.ParticipationRecordMapper;
import neu.competition.mapper.TeamMapper;
import neu.competition.mapper.TeamMemberMapper;
import neu.competition.mapper.UserMapper;
import neu.competition.service.FileService;
import neu.competition.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamMapper teamMapper;

    @Autowired
    private TeamMemberMapper teamMemberMapper;

    @Autowired
    private ParticipationRecordMapper participationRecordMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    @Override
    public List<Team> getEligibleTeams(String userId, int matchId) {
        List<Team> teams = teamMapper.findEligibleTeams(userId, matchId);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(team.getId());
            team.setMembers(members);
        }
        return teams;
    }

    @Override
    public void registerTeamForMatch(int teamId, int matchId) {

    }


    @Override
    public List<TeamMember> getTeamMembersByTeamId(int teamId) {
        return teamMemberMapper.getTeamMembersByTeamId(teamId);
    }

    @Override
    public boolean isTeamRegisteredForCompetition(int teamId, int matchId) {
        return participationRecordMapper.existsByTeamIdAndMatchId(teamId, matchId);
    }

    @Override
    public boolean addMember(String memberType, String userId, int teamId) {
        TeamMember teamMember = new TeamMember();
        teamMember.setUid(userId);
        teamMember.setTeamId(teamId);
        teamMember.setUname(getUserNameById(userId));
        teamMember.setRole(memberType);
        return teamMemberMapper.insert(teamMember) > 0;
    }

    private String getUserNameById(String userId) {
        // 根据用户ID获取用户名的逻辑
        User user = userMapper.getUserById(userId);
        if (user != null) {
            return user.getUname();
        } else {
            throw new RuntimeException("用户不存在，ID：" + userId);
        }
    }

    @Override
    public TeamMember getTeamLeaderByTeamId(int teamId) {
        return teamMemberMapper.getTeamLeaderByTeamId(teamId);
    }



    @Override
    public List<TeamMember> getStudentMembersByTeamId(int teamId) {
        return teamMemberMapper.getStudentMembersByTeamId(teamId);
    }

    @Override
    public List<TeamMember> getTeacherMembersByTeamId(int teamId) {
        return teamMemberMapper.getTeacherMembersByTeamId(teamId);
    }

    @Override
    @Transactional
    public void createTeam(int newTeamId, Team team, String studentMembers, String studentRoles, String teacherMembers, MultipartFile logoFile, String currentUserId) throws Exception {
        // 处理文件上传
        String logoPath = fileService.saveFile(logoFile);
        if (logoPath != null) {
            team.setLogo(logoPath);
        } else {
            throw new Exception("文件上传失败");
        }

        // 使用传递的 newTeamId
        team.setId(newTeamId);

        // 插入团队记录
        teamMapper.insert(team);

        // 插入团队成员记录
        addMembers(newTeamId, studentMembers, studentRoles, "队员");
        addMembers(newTeamId, teacherMembers, "教师");

        addMember("队长", currentUserId, newTeamId);
    }

    private void addMembers(int teamId, String members, String roles, String defaultRole) throws Exception {
        String[] memberIds = members.split(",");
        String[] memberRoles = roles.split(",");
        for (int i = 0; i < memberIds.length; i++) {
            TeamMember member = new TeamMember();
            member.setTeamId(teamId);
            member.setUid(memberIds[i].trim());
            member.setRole(memberRoles.length > i ? memberRoles[i].trim() : defaultRole);
            member.setUname(getUserNameById(memberIds[i].trim())); // 获取用户名
            int result = teamMemberMapper.insert(member);
            if (result != 1) {
                throw new Exception("插入成员失败");
            }
        }
    }
    @Override
    @Transactional
    public void updateTeam(int teamId, Team team, String studentMembers, String teacherMembers, String leaderMember, MultipartFile logoFile) throws Exception {
        // 处理文件上传
        if (logoFile != null && !logoFile.isEmpty()) {
            String logoPath = fileService.saveFile(logoFile);
            String flag = logoPath;
            if (logoPath != null) {
                team.setLogo(logoPath);  // 设置新的 logo 路径
            } else {
                throw new Exception("文件上传失败");
            }
        }

        // 更新团队信息
        team.setId(teamId);
        teamMapper.updateById(team);

        // 处理学生成员
        if (studentMembers != null && !studentMembers.isEmpty()) {
            updateMembers(teamId, studentMembers, "学生");
        }

        // 处理教师成员
        if (teacherMembers != null && !teacherMembers.isEmpty()) {
            updateMembers(teamId, teacherMembers, "教师");
        }

        // 更新队长信息
        if (leaderMember != null && !leaderMember.isEmpty()) {
            changeTeamLeader(teamId, leaderMember);
        }
    }

    private void updateMembers(int teamId, String members, String role) throws Exception {
        String[] memberIds = members.split(",");
        for (String memberId : memberIds) {
            TeamMember existingMember = teamMemberMapper.getTeamMemberByIdAndTeamId(memberId.trim(), teamId);
            if (existingMember == null) {
                TeamMember member = new TeamMember();
                member.setTeamId(teamId);
                member.setUid(memberId.trim());
                member.setRole(role);  // 确保传入的 role 是 '学生' 或 '教师'
                member.setUname(getUserNameById(memberId.trim())); // 获取用户名
                int result = teamMemberMapper.insert(member);
                if (result != 1) {
                    throw new Exception("插入成员失败");
                }
            }
        }
    }

    private void addMembers(int teamId, String members, String role) throws Exception {
        String[] memberIds = members.split(",");
        for (String memberId : memberIds) {
            TeamMember member = new TeamMember();
            member.setTeamId(teamId);
            member.setUid(memberId.trim());
            member.setRole(role);  // 确保传入的 role 是 '队员' 或 '教师'
            member.setUname(getUserNameById(memberId.trim())); // 获取用户名
            System.out.println("准备插入成员，ID：" + memberId + "，团队ID：" + teamId);
            int result = teamMemberMapper.insert(member);
            if (result != 1) {
                throw new Exception("插入成员失败");
            }
            System.out.println("成员插入成功，ID：" + memberId);
        }
    }

    @Override
    public List<Team> getMyTeamsByUserId(String userId) {
        List<Team> teams = teamMapper.getTeamsByUserId(userId);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(team.getId());
            team.setMembers(members);
        }
        return teams;
    }

    @Override
    public Team getTeamById(int teamId) {
        Team team = teamMapper.getTeamById(teamId);
        List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(teamId);
        team.setMembers(members);
        return team;
    }

    @Override
    public int getMaxTeamId() {
        return teamMapper.getMaxTeamId();
    }

    @Override
    @Transactional
    public void changeTeamLeader(int teamId, String newLeaderId) {
        // 获取当前队长
        TeamMember currentLeader = teamMemberMapper.getTeamLeaderByTeamId(teamId);

        // 将当前队长的角色降为队员
        if (currentLeader != null) {
            currentLeader.setRole("队员");
            teamMemberMapper.updateById(currentLeader.getUid(), teamId, "队员");
        }

        // 获取新队长的信息
        TeamMember newLeader = teamMemberMapper.getTeamMemberByIdAndTeamId(newLeaderId, teamId);

        // 将新队长的角色设置为队长
        if (newLeader != null) {
            newLeader.setRole("队长");
            teamMemberMapper.updateById(newLeader.getUid(), teamId, "队长");
        } else {
            throw new IllegalArgumentException("新队长必须是该团队的成员");
        }
    }
    @Override
    public List<Team> getParticipatingTeamsForUser(String userId, int matchId) {
        List<Team> teams = teamMapper.getParticipatingTeamsForUser(userId, matchId);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(team.getId());
            team.setMembers(members);
        }
        return teams;
    }
    @Override
    public boolean hasParticipatingTeam(String userId, int matchId) {
        List<Team> teams = teamMapper.getParticipatingTeamsForUser(userId, matchId);
        return teams != null && !teams.isEmpty();
    }
    // 在TeamServiceImpl实现中添加
    @Override
    public List<Team> getTeamsGuidedByTeacher(String teacherId) {
        // 查找教师作为指导老师的团队
        List<Team> teams = teamMapper.getTeamsGuidedByTeacher(teacherId);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(team.getId());
            team.setMembers(members);
        }
        return teams;
    }

    @Override
    public boolean isTeamLeader(String userId, int teamId) {
        TeamMember leader = teamMemberMapper.getTeamLeaderByTeamId(teamId);
        return leader != null && leader.getUid().equals(userId);
    }

    @Override
    public boolean isTeamTeacher(String userId, int teamId) {
        List<TeamMember> teachers = teamMemberMapper.getTeacherMembersByTeamId(teamId);
        return teachers.stream().anyMatch(teacher -> teacher.getUid().equals(userId));
    }

    @Override
    @Transactional
    public void cancelTeamRegistration(int teamId, int matchId, String cancelUserId) {
        // 查询报名记录
        ParticipationRecord record = participationRecordMapper.selectOneByTeamAndMatch(teamId, matchId);
        if (record == null) {
            throw new IllegalArgumentException("未找到报名记录");
        }

        if ("CANCELED".equals(record.getStatus())) {
            throw new IllegalArgumentException("该报名已被撤销");
        }

        // 更新报名状态
        record.setStatus("CANCELED");
        record.setCanceledBy(cancelUserId);
        record.setCancelTime(LocalDateTime.now());
        participationRecordMapper.updateById(record);

        // 删除用户比赛会话记录
        jdbcTemplate.update(
                "DELETE FROM user_competition_session WHERE match_id = ? AND team_id = ?",
                matchId, teamId
        );
    }

    @Override
    public List<Map<String, Object>> getTeamRegistrationsForTeacher(String teacherId, int matchId) {
        return teamMapper.getTeamRegistrationsForTeacher(teacherId, matchId);
    }
    @Override
    public Team getRegisteredTeamForMatch(String userId, int matchId) {
        try {
            /// 找到用户作为队长或队员的所有团队ID
            List<TeamMember> memberRecords = teamMemberMapper.getTeamMembersByUserId(userId);
            if (memberRecords.isEmpty()) {
                return null;
            }

            // 从这些团队中找出已经报名该比赛的团队
            for (TeamMember member : memberRecords) {
                int teamId = member.getTeamId();
                boolean hasRegistered = participationRecordMapper.existsByTeamIdAndMatchId(teamId, matchId);
                if (hasRegistered) {
                    // 找到已报名团队，返回完整团队信息
                    Team team = teamMapper.getTeamById(teamId);
                    return team;
                }
            }

            return null; // 未找到已报名团队
        } catch (Exception e) {
            logger.error("获取已报名团队时出错", e);
            return null; // 出错时返回null
        }
    }

    @Override
    public List<Team> getEligibleTeamsForUser(String userId, int matchId) {
        try {
            // 获取用户可选团队（用户为队长的且未报名该比赛的团队）
            List<Team> teams = teamMapper.getEligibleTeamsForUser(userId, matchId);

            // 为每个团队加载成员信息
            for (Team team : teams) {
                List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(team.getId());
                team.setMembers(members);
            }

            return teams;
        } catch (Exception e) {
            logger.error("获取可选团队时出错", e);
            return new ArrayList<>(); // 出错时返回空集合而非null
        }
    }




    @Override
    @Transactional
    public void registerTeamForCompetition(int teamId, int matchId) {
        // 检查是否已经报名
        boolean alreadyRegistered = participationRecordMapper.existsByTeamIdAndMatchId(teamId, matchId);
        if (alreadyRegistered) {
            // 查询报名状态
            ParticipationRecord record = participationRecordMapper.selectOneByTeamAndMatch(teamId, matchId);
            if (record != null && "ACTIVE".equals(record.getStatus())) {
                throw new IllegalStateException("该团队已经报名参加了该比赛");
            } else if (record != null && "CANCELED".equals(record.getStatus())) {
                // 如果之前报名被取消，则重新激活
                record.setStatus("ACTIVE");
                record.setCanceledBy(null);
                record.setCancelTime(null);
                record.setRegistrationTime(LocalDateTime.now());
                participationRecordMapper.updateById(record);
                return;
            }
        }

        // 获取团队队长信息
        TeamMember leader = teamMemberMapper.getTeamLeaderByTeamId(teamId);
        if (leader == null) {
            throw new IllegalStateException("团队必须有队长才能报名比赛");
        }

        // 检查队长是否已经带领其他团队参加了该比赛
        String leaderId = leader.getUid();
        List<TeamMember> leaderTeams = teamMemberMapper.getTeamMembersByUserIdAndRole(leaderId, "队长");

        for (TeamMember leaderTeam : leaderTeams) {
            int otherTeamId = leaderTeam.getTeamId();
            if (otherTeamId != teamId) { // 排除当前团队
                boolean hasRegistered = participationRecordMapper.existsByTeamIdAndMatchIdAndStatus(
                        otherTeamId, matchId, "ACTIVE");
                if (hasRegistered) {
                    throw new IllegalStateException("队长已经带领其他团队参加了该比赛");
                }
            }
        }

        // 创建新的报名记录
        ParticipationRecord record = new ParticipationRecord();
        record.setTeamId(teamId);
        record.setMatchId(matchId);
        record.setStatus("ACTIVE");
        record.setRegistrationTime(LocalDateTime.now());
        participationRecordMapper.insert(record);
    }

}