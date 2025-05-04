package neu.competition.service.strategy.impl;

import neu.competition.DTO.ProblemDTO;
import neu.competition.DTO.ResultDTO;
import neu.competition.DTO.SubmissionDTO;
import neu.competition.entity.Evaluation;
import neu.competition.entity.Result;
import neu.competition.mapper.EvaluationMapper;
import neu.competition.mapper.ProblemMapper;
import neu.competition.mapper.ResultMapper;
import neu.competition.mapper.SubmissionMapper;
import neu.competition.service.strategy.CompetitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service("speakingStrategy")
public class SpeakingStrategy implements CompetitionStrategy {
    
    @Autowired
    private ProblemMapper problemMapper;
    
    @Autowired
    private SubmissionMapper submissionMapper;
    
    @Autowired
    private EvaluationMapper evaluationMapper;
    
    @Autowired
    private ResultMapper resultMapper;
    
    @Override
    public String getCompetitionType() {
        return "SPEAKING";
    }
    
    @Override
    public List<ProblemDTO> getProblems(Integer matchId) {
        return problemMapper.selectProblemsByMatchId(matchId);
    }
    
    @Override
    public boolean submitSolution(SubmissionDTO submissionDTO) {
        // 演讲竞赛通常需要提交视频或现场演讲
        submissionDTO.setStatus("SUBMITTED");  // 已提交
        submissionDTO.setSubmitTime(LocalDateTime.now());
        
        // 构建Submission实体并保存
        try {
            return submissionMapper.insert(submissionDTOToEntity(submissionDTO)) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean evaluateSubmission(Integer submissionId, String userId, Double score, String comment) {
        // 演讲竞赛通常由评委评分
        Evaluation evaluation = new Evaluation();
        evaluation.setSubmissionId(submissionId);
        evaluation.setUserId(userId);
        evaluation.setScore(BigDecimal.valueOf(score));
        evaluation.setComment(comment);
        evaluation.setEvalTime(LocalDateTime.now());
        
        try {
            // 更新提交状态为已评分
            SubmissionDTO submission = submissionMapper.selectSubmissionById(submissionId);
            if (submission != null) {
                submission.setStatus("EVALUATED");
                submissionMapper.updateById(submissionDTOToEntity(submission));
            }
            
            return evaluationMapper.insert(evaluation) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean generateResults(Integer matchId) {
        try {
            // 获取比赛所有提交
            List<SubmissionDTO> submissions = submissionMapper.selectSubmissionsByMatchId(matchId);
            
            // 按团队分组并计算总分
            List<ResultDTO> results = new ArrayList<>();
            submissions.stream()
                    .collect(Collectors.groupingBy(SubmissionDTO::getTeamId))
                    .forEach((teamId, teamSubmissions) -> {
                        BigDecimal totalScore = BigDecimal.ZERO;
                        int count = 0;
                        
                        for (SubmissionDTO submission : teamSubmissions) {
                            Double avgScore = evaluationMapper.selectAverageScoreBySubmissionId(submission.getSubmissionId());
                            if (avgScore != null) {
                                // 演讲比赛通常只有一个题目，直接使用评委平均分
                                totalScore = BigDecimal.valueOf(avgScore);
                                count++;
                            }
                        }
                        
                        ResultDTO result = new ResultDTO();
                        result.setMatchId(matchId);
                        result.setTeamId(teamId);
                        result.setTotalScore(totalScore);
                        result.setCreateTime(LocalDateTime.now());
                        result.setTeamName(teamSubmissions.get(0).getTeamName());
                        result.setMatchName(teamSubmissions.get(0).getMatchName());
                        results.add(result);
                    });
            
            // 排序并设置排名
            results.sort(Comparator.comparing(ResultDTO::getTotalScore).reversed());
            for (int i = 0; i < results.size(); i++) {
                results.get(i).setRank(i + 1);
            }
            
            // 保存结果
            for (ResultDTO result : results) {
                resultMapper.insert(resultDTOToEntity(result));
            }
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<Object> getResults(Integer matchId) {
        return new ArrayList<>(resultMapper.selectResultsByMatchId(matchId));
    }
    
    // 辅助方法：DTO转实体
    private Result resultDTOToEntity(ResultDTO dto) {
        Result result = new Result();
        result.setResultId(dto.getResultId());
        result.setMatchId(dto.getMatchId());
        result.setTeamId(dto.getTeamId());
        result.setTotalScore(dto.getTotalScore());
        result.setRank(dto.getRank());
        result.setCreateTime(dto.getCreateTime());
        return result;
    }
    
    private neu.competition.entity.Submission submissionDTOToEntity(SubmissionDTO dto) {
        neu.competition.entity.Submission submission = new neu.competition.entity.Submission();
        submission.setSubmissionId(dto.getSubmissionId());
        submission.setTeamId(dto.getTeamId());
        submission.setMatchId(dto.getMatchId());
        submission.setProblemId(dto.getProblemId());
        submission.setContentUrl(dto.getContentUrl());
        submission.setSubmitTime(dto.getSubmitTime());
        submission.setStatus(dto.getStatus());
        return submission;
    }
}