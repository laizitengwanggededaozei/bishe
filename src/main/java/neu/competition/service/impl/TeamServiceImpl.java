package neu.competition.service.impl;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        if (!isTeamRegisteredForCompetition(teamId, matchId)) {
            registerTeamForCompetition(teamId, matchId);
        }
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
    public void registerTeamForCompetition(int teamId, int matchId) {
        boolean alreadyRegistered = participationRecordMapper.existsByTeamIdAndMatchId(teamId, matchId);
        if (!alreadyRegistered) {
            ParticipationRecord record = new ParticipationRecord();
            record.setTeamId(teamId);
            record.setMatchId(matchId);
            participationRecordMapper.insert(record);
        } else {
            throw new IllegalStateException("该团队已经报名参加了该比赛");
        }
    }

    @Override
    public List<Team> getEligibleTeamsForUser(String userId, int matchId) {
        List<Team> teams = teamMapper.getEligibleTeamsForUser(userId, matchId);
        for (Team team : teams) {
            List<TeamMember> members = teamMemberMapper.getTeamMembersByTeamId(team.getId());
            team.setMembers(members);
        }
        return teams;
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
}