package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("problem")
public class Problem implements Serializable {
    
    @TableId
    private Integer problemId;          // 题目ID
    private Integer matchId;            // 比赛ID
    private String title;               // 标题
    private String content;             // 内容
    private String type;                // 类型
    private LocalDateTime createTime;   // 创建时间
}