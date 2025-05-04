package neu.competition.controller;

import neu.competition.DTO.ProblemDTO;
import neu.competition.DTO.ResultDTO;
import neu.competition.DTO.SubmissionDTO;
import neu.competition.entity.Team;
import neu.competition.entity.User;
import neu.competition.service.CompetitionProcessService;
import neu.competition.service.CompetitionService;
import neu.competition.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/competition-process")
public class CompetitionProcessController {
    
    @Autowired
    private CompetitionProcessService competitionProcessService;
    
    @Autowired
    private CompetitionService competitionService;
    
    @Autowired
    private TeamService teamService;
    
    // 比赛进行页面
    @GetMapping("/match/{matchId}")
    public String competitionProcess(@PathVariable("matchId") Integer matchId, Model model, HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 获取比赛信息
        model.addAttribute("matchDTO", competitionService.selectMatch(matchId));
        
        // 获取用户参赛团队
        List<Team> teams = teamService.getEligibleTeamsForUser(user.getId(), matchId);
        model.addAttribute("teams", teams);
        
        // 获取比赛题目
        List<ProblemDTO> problems = competitionProcessService.getProblems(matchId);
        model.addAttribute("problems", problems);
        
        return "competition/process/competition-process";
    }
    
    // 题目详情页面
    @GetMapping("/problem/{problemId}")
    public String problemDetail(@PathVariable("problemId") Integer problemId, Model model, HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 获取题目详情
        ProblemDTO problem = competitionProcessService.getProblemDetail(problemId);
        model.addAttribute("problem", problem);
        
        // 获取用户参赛团队
        List<Team> teams = teamService.getEligibleTeamsForUser(user.getId(), problem.getMatchId());
        model.addAttribute("teams", teams);
        
        return "competition/process/problem-detail";
    }
    
    // 提交作品
    @PostMapping("/submit")
    public String submitSolution(@RequestParam("teamId") Integer teamId,
                                @RequestParam("matchId") Integer matchId,
                                @RequestParam("problemId") Integer problemId,
                                @RequestParam("solutionFile") MultipartFile file,
                                HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 保存文件并获取URL
        String contentUrl = saveFile(file);
        
        // 创建提交DTO
        SubmissionDTO submissionDTO = new SubmissionDTO();
        submissionDTO.setTeamId(teamId);
        submissionDTO.setMatchId(matchId);
        submissionDTO.setProblemId(problemId);
        submissionDTO.setContentUrl(contentUrl);
        
        // 提交解决方案
        boolean success = competitionProcessService.submitSolution(submissionDTO);
        
        if (success) {
            return "redirect:/competition-process/submissions?teamId=" + teamId + "&matchId=" + matchId;
        } else {
            return "redirect:/competition-process/problem/" + problemId + "?error=submit";
        }
    }
    
    // 查看团队提交列表
    @GetMapping("/submissions")
    public String teamSubmissions(@RequestParam("teamId") Integer teamId,
                                  @RequestParam("matchId") Integer matchId,
                                  Model model, HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 获取团队提交列表
        List<SubmissionDTO> submissions = competitionProcessService.getTeamSubmissions(teamId, matchId);
        model.addAttribute("submissions", submissions);
        
        // 获取比赛信息
        model.addAttribute("matchDTO", competitionService.selectMatch(matchId));
        
        // 获取团队信息
        model.addAttribute("team", teamService.getTeamById(teamId));
        
        return "competition/process/team-submissions";
    }

    // 修改文件：src/main/java/neu/competition/controller/CompetitionProcessController.java
// 修改评委评分页面方法
    @GetMapping("/evaluate/{submissionId}")
    public String evaluateSubmission(@PathVariable("submissionId") Integer submissionId,
                                     Model model, HttpSession session) {
        // 检查用户是否登录并且是组织者或教师
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        String role = String.valueOf(user.getId().charAt(0));
        if (!role.equals("B") && !role.equals("T")) {
            return "redirect:/unauthorized";
        }

        // 获取提交详情
        SubmissionDTO submission = getSubmissionDetail(submissionId);
        model.addAttribute("submission", submission);

        return "competition/process/evaluate-submission";
    }

    // 修改提交评分方法
    @PostMapping("/evaluate")
    public String submitEvaluation(@RequestParam("submissionId") Integer submissionId,
                                   @RequestParam("score") Double score,
                                   @RequestParam("comment") String comment,
                                   HttpSession session) {
        // 检查用户是否登录并且是组织者或教师
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        String role = String.valueOf(user.getId().charAt(0));
        if (!role.equals("B") && !role.equals("T")) {
            return "redirect:/unauthorized";
        }

        // 提交评分
        boolean success = competitionProcessService.evaluateSubmission(submissionId, user.getId(), score, comment);

        // 获取提交详情以便重定向
        SubmissionDTO submission = getSubmissionDetail(submissionId);

        if (success) {
            return "redirect:/competition-process/submissions?teamId=" + submission.getTeamId() + "&matchId=" + submission.getMatchId();
        } else {
            return "redirect:/competition-process/evaluate/" + submissionId + "?error=evaluate";
        }
    }

    // 修改生成比赛结果方法
    @PostMapping("/generate-results")
    public String generateResults(@RequestParam("matchId") Integer matchId, HttpSession session) {
        // 检查用户是否登录并且是组织者
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }

        String role = String.valueOf(user.getId().charAt(0));
        if (!role.equals("B")) {
            return "redirect:/unauthorized";
        }

        // 生成比赛结果
        boolean success = competitionProcessService.generateResults(matchId);

        if (success) {
            return "redirect:/competition-process/results?matchId=" + matchId;
        } else {
            return "redirect:/competition-process/match/" + matchId + "?error=generate";
        }
    }
    
    // 查看比赛结果
    @GetMapping("/results")
    public String competitionResults(@RequestParam("matchId") Integer matchId, Model model, HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 获取比赛结果
        List<ResultDTO> results = competitionProcessService.getCompetitionResults(matchId);
        model.addAttribute("results", results);
        
        // 获取比赛信息
        model.addAttribute("matchDTO", competitionService.selectMatch(matchId));
        
        return "competition/process/competition-results";
    }
    
    // 查看团队结果
    @GetMapping("/team-result")
    public String teamResult(@RequestParam("teamId") Integer teamId,
                           @RequestParam("matchId") Integer matchId,
                           Model model, HttpSession session) {
        // 检查用户是否登录
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/login";
        }
        
        // 获取团队比赛结果
        ResultDTO result = competitionProcessService.getTeamResult(teamId, matchId);
        model.addAttribute("result", result);
        
        // 获取比赛信息
        model.addAttribute("matchDTO", competitionService.selectMatch(matchId));
        
        // 获取团队信息
        model.addAttribute("team", teamService.getTeamById(teamId));
        
        return "competition/process/team-result";
    }
    
    // 辅助方法：保存文件并返回URL
    private String saveFile(MultipartFile file) {
        // 这里应调用您现有的文件服务实现
        // 简化版，实际应用中应引用您的FileService实现
        String url = "/upload/" + file.getOriginalFilename();
        
        // 保存文件的代码
        
        return url;
    }
    
    // 辅助方法：获取提交详情
    private SubmissionDTO getSubmissionDetail(Integer submissionId) {
        // 简化版，实际应引用SubmissionMapper
        // 这里应该调用submissionMapper.selectSubmissionById
        // 这里返回一个假的对象以保持代码连贯性
        return new SubmissionDTO();
    }
}