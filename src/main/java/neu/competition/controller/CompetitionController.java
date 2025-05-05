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
		// 获取比赛信息
		MatchesDTO matchesDTO = competitionService.selectMatch(id);
		model.addAttribute("matchDTO", matchesDTO);

		User user = (User) session.getAttribute("loggedUser");
		if (user != null) {
			String userRole = user.getId().substring(0, 1);

			// 对于学生用户，获取报名信息
			if ("S".equals(userRole)) {
				// 查询该学生在该比赛已报名的团队
				Team registeredTeam = teamService.getRegisteredTeamForMatch(user.getId(), id);
				model.addAttribute("registeredTeam", registeredTeam);

				// 如果没有报名，则获取可报名的团队列表
				if (registeredTeam == null) {
					List<Team> eligibleTeams = teamService.getEligibleTeamsForUser(user.getId(), id);
					model.addAttribute("eligibleTeams", eligibleTeams);
				}
			}
		}

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
		// 重定向到 team 控制器的注册页面
		return "redirect:/team/register-competition?matchId=" + matchId;
	}

	// 移除重复的 submitRegistration 方法，因为 TeamController 已经提供了此功能

	@GetMapping("/registrationSuccess")
	public String showRegistrationSuccessPage() {
		return "competition/registrationSuccess";
	}
}