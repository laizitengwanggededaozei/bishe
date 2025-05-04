package neu.competition.controller;

import neu.competition.DTO.CompetitionDTO;
import neu.competition.DTO.MatchesDTO;
import neu.competition.entity.Team;
import neu.competition.entity.User;
import neu.competition.service.CompetitionProcessService;
import neu.competition.service.CompetitionService;
import neu.competition.service.TeamService;
import neu.competition.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CompetitionController {

	@Autowired
	private CompetitionService competitionService;
	@Autowired
	private UserService userService;
	@Autowired
	private TeamService teamService;

	// 添加以下代码:
	@Autowired
	private CompetitionProcessService competitionProcessService;

	private String getPrefix(String id) {
		if (id.startsWith("S")) {
			return "S";
		} else if (id.startsWith("T")) {
			return "T";
		} else if (id.startsWith("B")) {
			return "B";
		}
		return null;
	}

	// 页面跳转 - 我的赛事
	@RequestMapping("/admin/create-competition")
	public String myCompetition() {
		return "redirect:/myCompetition";
	}

	@RequestMapping("/myCompetition")
	public String myCompetitionView(Model model, HttpSession session) {
		User user = (User) session.getAttribute("loggedUser");
		List<CompetitionDTO> competitionDTOList = competitionService.selectUserCompetition(user);
		String prefix = getPrefix(user.getId());
		model.addAttribute("myCompetition", competitionDTOList);
		model.addAttribute("prefix", prefix);
		return "competition/myCompetition";
	}

	@RequestMapping("/competition")
	public String competitionView(Model model, int id) {
		CompetitionDTO competitionDTO = competitionService.selectCompetition(id);
		MatchesDTO matchesDTO = competitionService.selectNewMatch(id);
		model.addAttribute("competitionDTO", competitionDTO);
		model.addAttribute("matchDTO", matchesDTO);
		return "competition/competition";
	}

	@RequestMapping("/editCompetition")
	public String competitionEdit(@RequestParam("comId") int id, Model model) {
		CompetitionDTO competitionDTO = competitionService.selectCompetition(id);
		model.addAttribute("competitionDTO", competitionDTO);
		return "competition/createCompetition";
	}

	// 页面跳转 - 创建赛事
	@RequestMapping("/createCompetition")
	public String createCompetitionView(Model model) {
		model.addAttribute("competitionDTO", new CompetitionDTO());
		return "competition/createCompetition";
	}

	// 创建赛事的提交按钮方法
	@RequestMapping("/create")
	public String createCompetition(@RequestParam("image") MultipartFile file, @ModelAttribute("competitionDTO") CompetitionDTO competitionDTO, Model model, HttpSession session) {
		if (!competitionService.isHadCompetition(competitionDTO.getComName()) || competitionDTO.getComId() != 0) {
			User user = (User) session.getAttribute("loggedUser");
			competitionService.createCompetition(competitionDTO, file, user);
			return "redirect:/myCompetition"; // 直接重定向，不需要添加message
		} else {
			model.addAttribute("message", "赛事名已存在，请重新输入");
			model.addAttribute("competitionDTO", new CompetitionDTO());
			return "competition/createCompetition"; // 返回视图名称，不是重定向
		}
	}
	// 页面跳转 - 我的比赛
	@RequestMapping("/create-match")
	public String myMatch() {
		return "redirect:/myMatch";
	}

	@RequestMapping("/myMatch")
	public String myMatchView(Model model, HttpSession session) {
		User user = (User) session.getAttribute("loggedUser");
		List<MatchesDTO> matchesDTOList = competitionService.selectUserMatch(user);
		String prefix = getPrefix(user.getId());
		model.addAttribute("myMatch", matchesDTOList);
		model.addAttribute("prefix", prefix);
		return "competition/myMatch";
	}

	@RequestMapping("/match")
	public String matchView(Model model, int id, HttpSession session) {
		MatchesDTO matchesDTO = competitionService.selectMatch(id);
		model.addAttribute("matchDTO", matchesDTO);

		// 添加判断用户是否已有参赛团队的逻辑
		User user = (User) session.getAttribute("loggedUser");
		boolean hasParticipatingTeam = false;

		if (user != null && user.getId().startsWith("S")) {
			// 只对学生用户进行检查
			List<Team> teams = teamService.getParticipatingTeamsForUser(user.getId(), id);
			hasParticipatingTeam = teams != null && !teams.isEmpty();
		}

		model.addAttribute("hasParticipatingTeam", hasParticipatingTeam);
		return "competition/match";
	}

	@RequestMapping("/select")
	public String selectCompetition(@RequestParam(value="comName", required=false) String comName, Model model) {
        List<CompetitionDTO> list;
        if (comName == null) {
			list = competitionService.readCompetition();
        } else {
			list = competitionService.findCompetition(comName);
        }
        model.addAttribute("competitionList", list);
        return "competition/searchCompetition";
	}

	@RequestMapping("/editMatch")
	public String matchEdit(@RequestParam("matchId") int id, Model model) {
		MatchesDTO matchesDTO = competitionService.selectMatch(id);
		CompetitionDTO competitionDTO = competitionService.selectCompetition(matchesDTO.getComId());
		model.addAttribute("matchesDTO", matchesDTO);
		model.addAttribute("myCompetition", competitionDTO);
		return "competition/prepareMatch";
	}

	// 页面跳转 - 筹备比赛
	@RequestMapping("/prepareMatch")
	public String prepareMatchView(Model model, int id) {
		CompetitionDTO competitionDTO = competitionService.selectCompetition(id);
		model.addAttribute("myCompetition", competitionDTO);
		model.addAttribute("matchesDTO", new MatchesDTO());
		return "competition/prepareMatch";
	}

	// 筹备比赛的提交按钮方法
	@RequestMapping("/prepare")
	public String prepareMatch(@RequestParam("image") MultipartFile file, @ModelAttribute("matchesDTO") MatchesDTO matchesDTO, HttpSession session) {
		User user = (User) session.getAttribute("loggedUser");
		competitionService.prepareMatch(matchesDTO, file, user);
		return "redirect:/myMatch";
	}

	@RequestMapping("/search")
	public String findCompetition(@RequestParam("comName") String comName, Model model) {
		List<CompetitionDTO> list = competitionService.findCompetition(comName);
		model.addAttribute("competitionList", list);
		return "competition/findCompetition";
	}

	@RequestMapping("/publish")
	public String setPublish(@RequestParam("matchId") int id) {
		competitionService.setPublish(id);
		return "redirect:/match?id=" + id;
	}

	@GetMapping("/register-competition")
	public String showRegisterCompetitionPage(@RequestParam("matchId") int matchId, HttpSession session, Model model) {
		return getString(matchId, session, model, teamService);
	}

	static String getString(@RequestParam("matchId") int matchId, HttpSession session, Model model, TeamService teamService) {
		return getString(matchId, session, model, teamService);
	}

	@PostMapping("/submitRegistration")
	public String submitRegistration(HttpSession session, Model model, @RequestParam("teamId") Integer teamId) {
		Integer matchId = (Integer) session.getAttribute("matchId");

		if (teamId == null || matchId == null) {
			model.addAttribute("errorMessage", "未选择团队或比赛ID");
			return "redirect:/team/register-competition";
		}
		User user = (User) session.getAttribute("loggedUser");
		if (user == null) {
			return "redirect:/login";
		}
		System.out.println("Received teamId: " + teamId);
		System.out.println("Received matchId: " + matchId);
		teamService.registerTeamForCompetition(teamId, matchId);
		model.addAttribute("successMessage", "报名成功！");

		// 清除会话中的teamId和matchId
		session.removeAttribute("teamId");
		session.removeAttribute("matchId");

		return "redirect:/registrationSuccess";
	}


	@GetMapping("/registrationSuccess")
	public String showRegistrationSuccessPage() {
		return "competition/registrationSuccess";
	}
}
