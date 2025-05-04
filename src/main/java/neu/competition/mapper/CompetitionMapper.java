package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.DTO.CompetitionDTO;
import neu.competition.entity.Competition;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CompetitionMapper extends BaseMapper<Competition> {
    @Select("SELECT * FROM `competition` WHERE com_name = #{comName}")
    Competition selectByComName(@Param("comName") String comName);

    @Select("SELECT * FROM `competition` WHERE com_name LIKE CONCAT('%', #{comName}, '%')")
    List<Competition> findCompetition(@Param("comName") String comName);
}
