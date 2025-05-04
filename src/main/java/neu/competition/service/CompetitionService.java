package neu.competition.service;

import neu.competition.DTO.CompetitionDTO;
import neu.competition.DTO.MatchesDTO;
import neu.competition.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface CompetitionService {

	List<CompetitionDTO> readCompetition();

	List<CompetitionDTO> selectUserCompetition(User user);

	CompetitionDTO selectCompetition(int id);

	boolean isHadCompetition(String comName);

	void createCompetition(CompetitionDTO competitionDTO, MultipartFile file, User user);

    List<MatchesDTO> readMatch();

	List<MatchesDTO> selectUserMatch(User user);

	void prepareMatch(MatchesDTO matchesDTO, MultipartFile file, User user);

    boolean isHadMatch(String matchName);

    MatchesDTO selectNewMatch(int id);

	MatchesDTO selectMatch(int id);

	List<CompetitionDTO> findCompetition(String comName);

    void setPublish(int matchId);

    List<CompetitionDTO> selectCommend();

	List<MatchesDTO> selectNewMatches();
	List<MatchesDTO> getMatchesForEvaluation();
}
