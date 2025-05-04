package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("result")
public class Result implements Serializable {
    
    @TableId
    private Integer resultId;           // 结果ID
    private Integer matchId;            // 比赛ID
    private Integer teamId;             // 团队ID
    private BigDecimal totalScore;      // 总分
    private Integer rank;               // 排名
    private LocalDateTime createTime;   // 创建时间
}