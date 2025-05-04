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
@TableName("submission")
public class Submission implements Serializable {
    
    @TableId
    private Integer submissionId;       // 提交ID
    private Integer teamId;             // 团队ID
    private Integer matchId;            // 比赛ID
    private Integer problemId;          // 题目ID
    private String contentUrl;          // 内容URL
    private LocalDateTime submitTime;   // 提交时间
    private String status;              // 状态
}