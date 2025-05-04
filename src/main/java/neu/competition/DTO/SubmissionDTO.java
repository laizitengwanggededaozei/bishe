package neu.competition.DTO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("submissionDTO")
public class SubmissionDTO implements Serializable {
    
    @TableId
    private Integer submissionId;       // 提交ID
    private Integer teamId;             // 团队ID
    private Integer matchId;            // 比赛ID
    private Integer problemId;          // 题目ID
    private String contentUrl;          // 内容URL
    private LocalDateTime submitTime;   // 提交时间
    private String status;              // 状态
    private String teamName;            // 团队名称
    private String problemTitle;        // 题目标题
    private String matchName;           // 比赛名称
    
    // Constructors
    public SubmissionDTO() {
    }
    
    // Getter 和 Setter 方法
    public Integer getSubmissionId() {
        return submissionId;
    }
    
    public void setSubmissionId(Integer submissionId) {
        this.submissionId = submissionId;
    }
    
    public Integer getTeamId() {
        return teamId;
    }
    
    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }
    
    public Integer getMatchId() {
        return matchId;
    }
    
    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }
    
    public Integer getProblemId() {
        return problemId;
    }
    
    public void setProblemId(Integer problemId) {
        this.problemId = problemId;
    }
    
    public String getContentUrl() {
        return contentUrl;
    }
    
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
    
    public LocalDateTime getSubmitTime() {
        return submitTime;
    }
    
    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTeamName() {
        return teamName;
    }
    
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
    
    public String getProblemTitle() {
        return problemTitle;
    }
    
    public void setProblemTitle(String problemTitle) {
        this.problemTitle = problemTitle;
    }
    
    public String getMatchName() {
        return matchName;
    }
    
    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }
    
    @Override
    public String toString() {
        return "SubmissionDTO{" +
                "submissionId=" + submissionId +
                ", teamId=" + teamId +
                ", matchId=" + matchId +
                ", problemId=" + problemId +
                ", contentUrl='" + contentUrl + '\'' +
                ", submitTime=" + submitTime +
                ", status='" + status + '\'' +
                ", teamName='" + teamName + '\'' +
                ", problemTitle='" + problemTitle + '\'' +
                ", matchName='" + matchName + '\'' +
                '}';
    }
}