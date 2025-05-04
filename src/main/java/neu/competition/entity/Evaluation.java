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
@TableName("evaluation")
public class Evaluation implements Serializable {
    
    @TableId
    private Integer evaluationId;       // 评价ID
    private Integer submissionId;       // 提交ID
    private String userId;              // 用户ID
    private BigDecimal score;           // 分数
    private String comment;             // 评语
    private LocalDateTime evalTime;     // 评价时间
}