package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.CompetitionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CompetitionTypeMapper extends BaseMapper<CompetitionType> {
    
    @Select("SELECT * FROM competition_type WHERE discipline = #{discipline}")
    CompetitionType selectByDiscipline(@Param("discipline") String discipline);
}