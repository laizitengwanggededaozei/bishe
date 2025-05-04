package neu.competition.DTO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("resultDTO")
public class ResultDTO implements Serializable {
    
    @TableId
    private Integer resultId;           // 结果ID
    private Integer matchId;            // 比赛ID
    private Integer teamId;             // 团队ID
    private BigDecimal totalScore;      // 总分
    private Integer rank;               // 排名
    private LocalDateTime createTime;   // 创建时间
    private String teamName;            // 团队名称
    private String matchName;           // 比赛名称
    
    // Constructors
    public ResultDTO() {
    }
    
    // Getter 和 Setter 方法
    public Integer getResultId() {
        return resultId;
    }
    
    public void setResultId(Integer resultId) {
        this.resultId = resultId;
    }
    
    public Integer getMatchId() {
        return matchId;
    }
    
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }
    
    public Integer getTeamId() {
        return teamId;
    }
    
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
    
    public BigDecimal getTotalScore() {
        return totalScore;
    }
    
    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
    
    public Integer getRank() {
        return rank;
    }
    
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public String getMatchName() {
        return matchName;
    }
    
    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }
    
    @Override
    public String toString() {
        return "ResultDTO{" +
                "resultId=" + resultId +
                ", matchId=" + matchId +
                ", teamId=" + teamId +
                ", totalScore=" + totalScore +
                ", rank=" + rank +
                ", createTime=" + createTime +
                ", teamName='" + teamName + '\'' +
                ", matchName='" + matchName + '\'' +
                '}';
    }
}