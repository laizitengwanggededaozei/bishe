package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.DTO.MatchesDTO;
import neu.competition.entity.Matches;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MatchMapper extends BaseMapper<Matches> {
    @Select("SELECT * FROM `matches` WHERE match_name = #{matchName}")
    Matches selectByMatchName(@Param("matchName") String matchName);

    @Select("SELECT * FROM `matches` WHERE com_id = #{comId} ORDER BY match_id DESC LIMIT 1")
    Matches selectNewMatch(@Param("comId") int comId);

    @Select("SELECT * FROM `matches` ORDER BY match_id DESC")
    List<Matches> selectNewMatches();
}
