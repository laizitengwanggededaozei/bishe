package neu.competition.controller;

import neu.competition.entity.Team;
import neu.competition.entity.TeamMember;
import neu.competition.entity.User;
import neu.competition.service.TeamService;
import neu.competition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @GetMapping("/manage")
    public String manageTeams(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        String userId = user.getId();
        List<Team> myTeams = teamService.getMyTeamsByUserId(userId);
        model.addAttribute("myTeams", myTeams);
        return "competition/teamManagement";
    }

    @GetMapping("/detail")
    public String teamDetail(@RequestParam("id") int teamId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Team team = teamService.getTeamById(teamId);
        List<TeamMember> studentMembers = teamService.getStudentMembersByTeamId(teamId);
        List<TeamMember> teacherMembers = teamService.getTeacherMembersByTeamId(teamId);
        TeamMember teamLeader = teamService.getTeamLeaderByTeamId(teamId);

        model.addAttribute("team", team);
        model.addAttribute("studentMembers", studentMembers);
        model.addAttribute("teacherMembers", teacherMembers);
        model.addAttribute("teamLeader", teamLeader != null ? List.of(teamLeader) : List.of());

        return "competition/teamDetail";
    }

    @GetMapping("/create")
    public String showCreateTeamForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("team", new Team());
        int maxTeamId = teamService.getMaxTeamId();
        int newTeamId = maxTeamId + 1;
        model.addAttribute("newTeamId", newTeamId);
        return "competition/teamCreate";
    }

    @PostMapping("/create")
    public String createTeam(@RequestParam("newTeamId") int newTeamId,
                             @ModelAttribute Team team,
                             @RequestParam("logoFile") MultipartFile logoFile,
                             @RequestParam("studentMembers") String studentMembers,
                             @RequestParam("studentRoles") String studentRoles,
                             @RequestParam("teacherMembers") String teacherMembers,
                             HttpSession session) {
        try {
            User currentUser = (User) session.getAttribute("loggedUser");
            if (currentUser == null) {
                return "redirect:/login";
            }
            String currentUserId = currentUser.getId();

            // 调用service创建团队
            teamService.createTeam(newTeamId, team, studentMembers, studentRoles, teacherMembers, logoFile, currentUserId);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/team/create?error";
        }
        return "redirect:/team/manage";
    }

    @GetMapping("/detail/modify")
    public String showTeamDetailModify(@RequestParam("id") int teamId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        Team team = teamService.getTeamById(teamId);
        List<TeamMember> studentMembers = teamService.getStudentMembersByTeamId(teamId);
        List<TeamMember> teacherMembers = teamService.getTeacherMembersByTeamId(teamId);
        TeamMember teamLeader = teamService.getTeamLeaderByTeamId(teamId);

        model.addAttribute("team", team);
        model.addAttribute("studentMembers", studentMembers);
        model.addAttribute("teacherMembers", teacherMembers);
        model.addAttribute("teamLeader", teamLeader);

        return "competition/teamDetailModify";
    }

    @PostMapping("/update")
    public String updateTeam(@ModelAttribute Team team,
                             @RequestParam("teamId") int teamId,
                             @RequestParam(value = "studentMembers", required = false) String studentMembers,
                             @RequestParam(value = "teacherMembers", required = false) String teacherMembers,
                             @RequestParam(value = "leaderMember", required = false) String leaderMember,
                             @RequestParam(value = "logoFile", required = false) MultipartFile logoFile) {
        try {
            // 调用 service 更新团队信息
            teamService.updateTeam(teamId, team, studentMembers, teacherMembers, leaderMember, logoFile);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/team/detail/modify?id=" + teamId + "&error";
        }
        return "redirect:/team/detail?id=" + teamId;
    }

    @PostMapping("/changeLeader")
    @ResponseBody
    public String changeTeamLeader(@RequestParam("teamId") int teamId, @RequestParam("newLeaderId") String newLeaderId) {
        try {
            teamService.changeTeamLeader(teamId, newLeaderId);
            return "success";
        } catch (Exception e) {
            return "error: " + e.getMessage();
        }
    }

    @GetMapping("/searchMember")
    @ResponseBody
    public ResponseEntity<Map<String, String>> searchMember(@RequestParam String id) {
        User user = userService.findById(id);
        if (user != null) {
            Map<String, String> result = new HashMap<>();
            result.put("id", user.getId());
            result.put("uname", user.getUname());
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/selectTeam")  // Changed from "/team/selectTeam"
    @ResponseBody
    public ResponseEntity<String> selectTeam(@RequestParam("teamId") int teamId, HttpSession session) {
        session.setAttribute("teamId", teamId);
        return ResponseEntity.ok("Team selected");
    }

    private String getString(int matchId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        String userId = user.getId();
        List<Team> teams = teamService.getEligibleTeamsForUser(userId, matchId);
        model.addAttribute("teams", teams);
        model.addAttribute("matchId", matchId);
        session.setAttribute("matchId", matchId);  // 将 matchId 存储到会话中
        return "competition/registerCompetition";
    }

    @GetMapping("/register-competition")
    public String showRegisterCompetitionPage(@RequestParam("matchId") int matchId, HttpSession session, Model model) {
        return getString(matchId, session, model);
    }



    @GetMapping("/registrationSuccess")
    public String showRegistrationSuccessPage() {
        return "redirect:/registrationSuccess";  // Redirect to the non-team specific success page
    }

    @GetMapping("/my-teams")
    public String myTeams(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        String userId = user.getId();
        // 使用已有的getMyTeamsByUserId方法
        List<Team> myTeams = teamService.getMyTeamsByUserId(userId);
        model.addAttribute("myTeams", myTeams);
        return "competition/teamManagement"; // 重用已有页面
    }
}