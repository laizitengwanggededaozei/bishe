package neu.competition.service.strategy;

import neu.competition.DTO.ProblemDTO;
import neu.competition.DTO.SubmissionDTO;
import neu.competition.entity.Matches;
import neu.competition.entity.Team;

import java.util.List;

public interface CompetitionStrategy {
    
    // 获取比赛环境类型
    String getCompetitionType();
    
    // 获取比赛题目
    List<ProblemDTO> getProblems(Integer matchId);
    
    // 提交作品
    boolean submitSolution(SubmissionDTO submissionDTO);
    
    // 评分方法
    boolean evaluateSubmission(Integer submissionId, String userId, Double score, String comment);
    
    // 生成比赛结果
    boolean generateResults(Integer matchId);
    
    // 获取比赛结果
    List<Object> getResults(Integer matchId);
}