package neu.competition.controller;

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

    @RequestMapping(value = "/team/submitRegistration", method = RequestMethod.POST)
    public String submitRegistration(HttpServletRequest request, HttpSession session, Model model,
                                     @RequestParam(value = "teamId", required = false) Integer teamId) {
        logger.info("=== 处理注册请求开始 ===");
        logger.info("请求方法: {}", request.getMethod());
        logger.info("请求URL: {}", request.getRequestURL().toString());
        logger.info("请求URI: {}", request.getRequestURI());
        logger.info("Content-Type: {}", request.getContentType());
        logger.info("teamId参数: {}", teamId);
        logger.info("session中的matchId: {}", session.getAttribute("matchId"));

        // 记录所有请求参数
        request.getParameterMap().forEach((key, value) ->
                logger.info("参数 {}: {}", key, String.join(", ", value))
        );

        Integer matchId = (Integer) session.getAttribute("matchId");

        if (teamId == null || matchId == null) {
            logger.error("缺少必要参数: teamId={}, matchId={}", teamId, matchId);
            model.addAttribute("errorMessage", "未选择团队或比赛ID");
            return "redirect:/team/register-competition";
        }

        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            logger.error("用户未登录");
            return "redirect:/login";
        }

        logger.info("用户ID: {}, 用户名: {}", user.getId(), user.getUname());

        try {
            logger.info("正在注册团队: teamId={}, matchId={}", teamId, matchId);
            // 注册团队
            teamService.registerTeamForCompetition(teamId, matchId);
            logger.info("注册成功");

            model.addAttribute("successMessage", "报名成功！");

            // 清除会话中的teamId
            session.removeAttribute("teamId");
            logger.info("重定向到成功页面");

            return "redirect:/registrationSuccess";
        } catch (Exception e) {
            logger.error("注册过程中发生错误", e);
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/team/register-competition?matchId=" + matchId + "&error=true";
        } finally {
            logger.info("=== 处理注册请求结束 ===");
        }
    }

    // 添加一个简单的GET处理方法来测试是否能访问控制器
    @GetMapping("/team/test-registration")
    @ResponseBody
    public String testRegistration() {
        logger.info("测试注册控制器 - GET请求成功");
        return "注册控制器测试成功 - GET方法可用";
    }
}