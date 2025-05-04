package neu.competition.service;

import neu.competition.DTO.ProblemDTO;
import neu.competition.DTO.ResultDTO;
import neu.competition.DTO.SubmissionDTO;

import java.util.List;

public interface CompetitionProcessService {
    
    // 获取比赛题目
    List<ProblemDTO> getProblems(Integer matchId);
    
    // 获取比赛题目详情
    ProblemDTO getProblemDetail(Integer problemId);
    
    // 提交作品
    boolean submitSolution(SubmissionDTO submissionDTO);
    
    // 获取团队提交列表
    List<SubmissionDTO> getTeamSubmissions(Integer teamId, Integer matchId);
    
    // 评分
    boolean evaluateSubmission(Integer submissionId, String userId, Double score, String comment);
    
    // 生成比赛结果
    boolean generateResults(Integer matchId);
    
    // 获取比赛结果
    List<ResultDTO> getCompetitionResults(Integer matchId);
    
    // 获取团队比赛结果
    ResultDTO getTeamResult(Integer teamId, Integer matchId);
}