package neu.competition.controller;

import neu.competition.entity.Team;
import neu.competition.entity.User;
import neu.competition.service.TeamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private TeamService teamService;

    @PostMapping("/team/submitRegistration")
    public String submitRegistration(HttpServletRequest request, HttpSession session, Model model,
                                     @RequestParam(value = "teamId", required = false) Integer teamId,
                                     @RequestParam(value = "matchId", required = false) Integer matchId) {
        logger.info("=== 处理注册请求开始 ===");
        logger.info("请求方法: {}", request.getMethod());
        logger.info("请求URL: {}", request.getRequestURL().toString());
        logger.info("teamId参数: {}", teamId);
        logger.info("matchId参数: {}", matchId);

        // 如果请求参数中没有matchId，尝试从session获取
        if (matchId == null) {
            matchId = (Integer) session.getAttribute("matchId");
            logger.info("从session中获取matchId: {}", matchId);
        }

        if (teamId == null || matchId == null) {
            logger.error("缺少必要参数: teamId={}, matchId={}", teamId, matchId);
            model.addAttribute("errorMessage", "未选择团队或比赛ID");
            return "redirect:/team/register-competition?matchId=" + matchId;
        }

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            logger.error("用户未登录");
            return "redirect:/login";
        }

        // 检查用户是否已经有团队报名本比赛
        Team registeredTeam = teamService.getRegisteredTeamForMatch(user.getId(), matchId);
        if (registeredTeam != null && registeredTeam.getId() != teamId) {
            logger.warn("用户已有团队({})报名本比赛，不能重复报名", registeredTeam.getId());
            model.addAttribute("errorMessage", "您已经有团队报名此比赛");
            return "redirect:/competition-process/match/" + matchId;
        }

        try {
            logger.info("正在注册团队: teamId={}, matchId={}", teamId, matchId);
            // 注册团队
            teamService.registerTeamForCompetition(teamId, matchId);
            logger.info("注册成功");

            // 确保matchId在session中可用，以便重定向后使用
            session.setAttribute("matchId", matchId);
            model.addAttribute("successMessage", "报名成功！");

            return "redirect:/registrationSuccess";
        } catch (Exception e) {
            logger.error("注册过程中发生错误", e);
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/team/register-competition?matchId=" + matchId + "&error=true";
        } finally {
            logger.info("=== 处理注册请求结束 ===");
        }
    }

    @PostMapping("/team/register-competition")
    public String processRegistration(@RequestParam(value = "teamId", required = false) Integer teamId,
                                      @RequestParam(value = "matchId", required = false) Integer matchId,
                                      HttpSession session, Model model,
                                      HttpServletRequest request) {
        // 记录参数
        logger.info("处理POST /team/register-competition, teamId={}, matchId={}", teamId, matchId);

        // 如果请求中没有matchId，尝试从session获取
        if (matchId == null) {
            matchId = (Integer) session.getAttribute("matchId");
        }

        return submitRegistration(request, session, model, teamId, matchId);
    }
}