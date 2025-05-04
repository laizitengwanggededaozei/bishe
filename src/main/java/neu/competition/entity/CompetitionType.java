package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("competition_type")
public class CompetitionType implements Serializable {
    
    @TableId
    private Integer typeId;             // 类型ID
    private String discipline;          // 学科
    private String typeName;            // 类型名称
    private String description;         // 描述
}