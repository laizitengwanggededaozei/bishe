package neu.competition.DTO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("problemDTO")
public class ProblemDTO implements Serializable {
    
    @TableId
    private Integer problemId;          // 题目ID
    private Integer matchId;            // 比赛ID
    private String title;               // 标题
    private String content;             // 内容
    private String type;                // 类型
    private LocalDateTime createTime;   // 创建时间
    private String matchName;           // 比赛名称


    
    // Constructors
    public ProblemDTO() {
    }
    
    // Getter 和 Setter 方法
    public Integer getProblemId() {
        return problemId;
    }
    
    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }
    
    public Integer getMatchId() {
        return matchId;
    }
    
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getMatchName() {
        return matchName;
    }
    
    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }
    
    @Override
    public String toString() {
        return "ProblemDTO{" +
                "problemId=" + problemId +
                ", matchId=" + matchId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type='" + type + '\'' +
                ", createTime=" + createTime +
                ", matchName='" + matchName + '\'' +
                '}';
    }
}