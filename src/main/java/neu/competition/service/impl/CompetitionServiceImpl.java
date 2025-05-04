package neu.competition.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import neu.competition.DTO.CompetitionDTO;
import neu.competition.DTO.MatchesDTO;
import neu.competition.entity.Competition;
import neu.competition.entity.Matches;
import neu.competition.entity.User;
import neu.competition.mapper.CompetitionMapper;
import neu.competition.mapper.MatchMapper;
import neu.competition.service.CompetitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
public class CompetitionServiceImpl implements CompetitionService {

	@Autowired
	private CompetitionMapper competitionMapper;
	@Autowired
	private MatchMapper matchMapper;
	@Value("${file.upload-dir}")
	private String uploadDir;

	private CompetitionDTO setCompetitionDTO(Competition competition) {
		CompetitionDTO dto = new CompetitionDTO();

		dto.setComId(competition.getComId());
		dto.setComName(competition.getComName());
		dto.setOrganizer(competition.getOrganizer());
		dto.setComLevel(competition.getComLevel());
		dto.setDiscipline(competition.getDiscipline());
		dto.setComWebsite(competition.getComWebsite());
		dto.setComProfile(competition.getComProfile());
		dto.setComPurpose(competition.getComPurpose());
		dto.setComOther(competition.getComOther());
		dto.setImageUrl(competition.getImageUrl());
		dto.setUserId((competition.getUserId()));
		dto.setCommend(competition.getCommend());

		return dto;
	}

	private MatchesDTO setMatchDTO(Matches matches) {
		MatchesDTO dto = new MatchesDTO();

		dto.setMatchId(matches.getMatchId());
		dto.setMatchName(matches.getMatchName());
		dto.setMatchLocal(matches.getMatchLocal());
		dto.setSponsorContact(matches.getSponsorContact());
		dto.setMatchLeader(matches.getMatchLeader());
		dto.setGradeInstructor(matches.getGradeInstructor());
		dto.setMatchStartTime(matches.getMatchStartTime());
		dto.setMatchEndTime(matches.getMatchEndTime());
		dto.setJoinDeadline(matches.getJoinDeadline());
		dto.setJudgingInformation(matches.getJudgingInformation());
		dto.setCompetitionStandards(matches.getCompetitionStandards());
		dto.setPrizeDescription(matches.getPrizeDescription());
		dto.setImageUrl(matches.getImageUrl());
		dto.setComId(matches.getComId());
		dto.setUserId((matches.getUserId()));
		dto.setCommend(matches.getCommend());
		dto.setPublish(matches.getPublish());

		return dto;
	}

	private String setFile(MultipartFile file) {
		String url = null;
		if (!file.isEmpty()) {
			try {
				// 获取文件名
				String fileName = file.getOriginalFilename();

				// 构建目标路径
				Path targetDirectory = Paths.get("src/main/resources/static/image/");
				Path targetPath = targetDirectory.resolve(fileName);

				// 确保目标目录存在
				Files.createDirectories(targetDirectory);

				// 将文件写入目标目录
				Files.write(targetPath, file.getBytes());

				// 使用相对路径保存
				url = "/image/" + fileName;

				System.out.println("文件已成功保存到指定目录");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("文件保存过程中发生错误：" + e.getMessage());
			}
		}

		return url;
	}

	@Override
	public List<CompetitionDTO> readCompetition() {
		List<CompetitionDTO> dtoList = new ArrayList<>();
		List<Competition> list = competitionMapper.selectList(null);

		for (Competition competition : list) {
			CompetitionDTO dto = setCompetitionDTO(competition);

			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public List<CompetitionDTO> selectUserCompetition(User user) {
		List<CompetitionDTO> dtoList = new ArrayList<>();
		QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", user.getId());
		List<Competition> list = competitionMapper.selectList(queryWrapper);

		for (Competition competition : list) {
			CompetitionDTO dto = setCompetitionDTO(competition);

			dtoList.add(dto);
		}

		return dtoList;
	}


	@Override
	public CompetitionDTO selectCompetition(int id) {
		Competition competition = competitionMapper.selectById(id);

        return setCompetitionDTO(competition);
	}

	@Override
	public boolean isHadCompetition(String comName) {
        return competitionMapper.selectByComName(comName) != null;
	}

	@Override
	public void createCompetition(CompetitionDTO dto, MultipartFile file, User user) {
		String url = setFile(file);

		Competition competition = new Competition();
		if (dto.getComId() != 0) {
			competition.setComId(dto.getComId());
		}
		competition.setComName(dto.getComName());
		competition.setOrganizer(dto.getOrganizer());
		competition.setComLevel(dto.getComLevel());
		competition.setDiscipline(dto.getDiscipline());
		competition.setComWebsite(dto.getComWebsite());
		competition.setComProfile(dto.getComProfile());
		competition.setComPurpose(dto.getComPurpose());
		competition.setComOther(dto.getComOther());
		if (url != null) {
			competition.setImageUrl(url);
		}
		competition.setUserId(user.getId());
		competition.setCommend(dto.getCommend());

		if (competition.getComId() == 0) {
			competitionMapper.insert(competition);
		} else {
			UpdateWrapper<Competition> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("com_id", competition.getComId());
			competitionMapper.update(competition, updateWrapper);
		}
	}

	@Override
	public List<MatchesDTO> readMatch() {
		List<MatchesDTO> dtoList = new ArrayList<>();
		List<Matches> list = matchMapper.selectList(null); // 假设你有一个获取所有比赛的方法

		for (Matches matches : list) {
			MatchesDTO dto = setMatchDTO(matches);

			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public List<MatchesDTO> selectUserMatch(User user) {
		List<MatchesDTO> dtoList = new ArrayList<>();
		QueryWrapper<Matches> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", user.getId());
		List<Matches> list = matchMapper.selectList(queryWrapper);

		for (Matches matches : list) {
			MatchesDTO dto = setMatchDTO(matches);

			dtoList.add(dto);
		}

		return dtoList;
	}


	@Override
	public MatchesDTO selectNewMatch(int id) {
		Matches matches = matchMapper.selectNewMatch(id);

		MatchesDTO dto = new MatchesDTO();

		if (matches != null) {
			dto = setMatchDTO(matches);
		}

		return dto;
	}

	@Override
	public boolean isHadMatch(String matchName) {
		return matchMapper.selectByMatchName(matchName) != null;
	}

	@Override
	public MatchesDTO selectMatch(int id) {
		Matches matches = matchMapper.selectById(id);

        return setMatchDTO(matches);
	}

	@Override
	public void prepareMatch(MatchesDTO dto, MultipartFile file, User user) {
		String url = setFile(file);

		Matches matches = new Matches();

		QueryWrapper<Matches> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("com_id", dto.getComId());
		Long count = matchMapper.selectCount(queryWrapper);

		Competition competition = competitionMapper.selectById(dto.getComId());

		if (dto.getMatchId() != 0) {
			matches.setMatchId(dto.getMatchId());
		}
		matches.setMatchName(setMatchName(competition.getComName(), count));
		matches.setMatchLocal(dto.getMatchLocal());
		matches.setSponsorContact(dto.getSponsorContact());
		matches.setMatchLeader(dto.getMatchLeader());
		matches.setGradeInstructor(dto.getGradeInstructor());
		matches.setMatchStartTime(dto.getMatchStartTime());
		matches.setMatchEndTime(dto.getMatchEndTime());
		matches.setJoinDeadline(dto.getJoinDeadline());
		matches.setJudgingInformation(dto.getJudgingInformation());
		matches.setCompetitionStandards(dto.getCompetitionStandards());
		matches.setPrizeDescription(dto.getPrizeDescription());
		if (url != null) {
			matches.setImageUrl(url);
		}
		matches.setComId(dto.getComId());
		matches.setUserId(user.getId());
		matches.setCommend(competition.getCommend());
		matches.setPublish(0);

		if (matches.getMatchId() == 0) {
			matchMapper.insert(matches);
		} else {
			UpdateWrapper<Matches> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq("match_id", matches.getMatchId());
			matchMapper.update(matches, updateWrapper);
		}
	}

	private String setMatchName(String comName, Long count) {
        return "第" + count + "届 " + comName;
	}


	@Override
	public List<CompetitionDTO> findCompetition(String comName) {
		List<CompetitionDTO> dtoList = new ArrayList<>();
		List<Competition> list = competitionMapper.findCompetition(comName);

		for (Competition competition : list) {
			CompetitionDTO dto = setCompetitionDTO(competition);

			dtoList.add(dto);
		}

		return dtoList;
	}

	@Override
	public void setPublish(int id) {
		System.out.println(id);
		Matches matches = matchMapper.selectById(id);
		matches.setPublish(1);

		UpdateWrapper<Matches> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("match_id", matches.getMatchId());
		matchMapper.update(matches, updateWrapper);
	}

	@Override
	public List<CompetitionDTO> selectCommend() {
		QueryWrapper<Competition> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("commend", 1);
		List<Competition> list =  competitionMapper.selectList(queryWrapper);
		List<CompetitionDTO> dtoList = new ArrayList<>();

		for (Competition competition : list) {
			CompetitionDTO dto = setCompetitionDTO(competition);

			dtoList.add(dto);
		}

		// 调用 Mapper 的 selectList 方法来执行查询
		return dtoList;
	}

	@Override
	public List<MatchesDTO> selectNewMatches() {
		QueryWrapper<Matches> queryWrapper = new QueryWrapper<>();
		queryWrapper.apply("match_start_time > NOW()").orderByAsc("match_start_time");
		List<Matches> list = matchMapper.selectList(queryWrapper);
		List<MatchesDTO> dtoList = new ArrayList<>();

		for (Matches matches : list) {
			MatchesDTO dto = setMatchDTO(matches);

			dtoList.add(dto);
		}

		return dtoList;
	}
	@Override
	public List<MatchesDTO> getMatchesForEvaluation() {
		QueryWrapper<Matches> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("publish", 1);
		List<Matches> matches = matchMapper.selectList(queryWrapper);

		List<MatchesDTO> dtoList = new ArrayList<>();
		for (Matches match : matches) {
			MatchesDTO dto = setMatchDTO(match);
			dtoList.add(dto);
		}

		return dtoList;
	}
}
