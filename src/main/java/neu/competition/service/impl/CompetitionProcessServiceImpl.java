package neu.competition.service.impl;

import neu.competition.DTO.ProblemDTO;
import neu.competition.DTO.ResultDTO;
import neu.competition.DTO.SubmissionDTO;
import neu.competition.mapper.ProblemMapper;
import neu.competition.mapper.ResultMapper;
import neu.competition.mapper.SubmissionMapper;
import neu.competition.service.CompetitionProcessService;
import neu.competition.service.factory.CompetitionStrategyFactory;
import neu.competition.service.strategy.CompetitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompetitionProcessServiceImpl implements CompetitionProcessService {
    
    @Autowired
    private CompetitionStrategyFactory strategyFactory;
    
    @Autowired
    private ProblemMapper problemMapper;
    
    @Autowired
    private SubmissionMapper submissionMapper;
    
    @Autowired
    private ResultMapper resultMapper;
    
    @Override
    public List<ProblemDTO> getProblems(Integer matchId) {
        return problemMapper.selectProblemsByMatchId(matchId);
    }
    
    @Override
    public ProblemDTO getProblemDetail(Integer problemId) {
        return problemMapper.selectProblemById(problemId);
    }
    
    @Override
    public boolean submitSolution(SubmissionDTO submissionDTO) {
        CompetitionStrategy strategy = strategyFactory.getStrategy(submissionDTO.getMatchId());
        return strategy.submitSolution(submissionDTO);
    }
    
    @Override
    public List<SubmissionDTO> getTeamSubmissions(Integer teamId, Integer matchId) {
        return submissionMapper.selectSubmissionsByTeamAndMatch(teamId, matchId);
    }
    
    @Override
    public boolean evaluateSubmission(Integer submissionId, String userId, Double score, String comment) {
        SubmissionDTO submission = submissionMapper.selectSubmissionById(submissionId);
        if (submission == null) {
            return false;
        }
        
        CompetitionStrategy strategy = strategyFactory.getStrategy(submission.getMatchId());
        return strategy.evaluateSubmission(submissionId, userId, score, comment);
    }
    
    @Override
    public boolean generateResults(Integer matchId) {
        CompetitionStrategy strategy = strategyFactory.getStrategy(matchId);
        return strategy.generateResults(matchId);
    }
    
    @Override
    public List<ResultDTO> getCompetitionResults(Integer matchId) {
        return resultMapper.selectResultsByMatchId(matchId);
    }
    
    @Override
    public ResultDTO getTeamResult(Integer teamId, Integer matchId) {
        return resultMapper.selectResultByTeamAndMatch(teamId, matchId);
    }
}