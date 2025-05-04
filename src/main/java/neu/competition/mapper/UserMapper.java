package neu.competition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import neu.competition.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author 25853
 * @version 1.0
 * @Create by 15/5/2024 下午9:58
 */

@Mapper
@ResponseBody
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM User WHERE id = #{id}")
    User getUserById(String id);
    @Select("SELECT * FROM User WHERE id = #{id} AND upwd = #{upwd}")
    User findByIdAndPassword(@Param("id") String id,
                             @Param("upwd") String upwd);

}