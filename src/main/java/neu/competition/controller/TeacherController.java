package neu.competition.controller;

import neu.competition.entity.Team;
import neu.competition.entity.User;
import neu.competition.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/teams")
    public String guidedTeams(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !user.getId().startsWith("T")) {
            return "redirect:/unauthorized";
        }

        // 获取教师指导的团队
        List<Team> guidedTeams = teamService.getTeamsGuidedByTeacher(user.getId());
        model.addAttribute("guidedTeams", guidedTeams);
        return "competition/teacher/guidedTeams";
    }
}