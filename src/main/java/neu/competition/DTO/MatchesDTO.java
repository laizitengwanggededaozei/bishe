package neu.competition.DTO;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("matchesDTO")
public class MatchesDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId
	private int matchId;                   // 比赛编号
	private String matchName;              // 比赛名称
	private String matchLocal;             // 比赛地点
	private String sponsorContact;         // 赞助联系人
	private String matchLeader;            // 比赛负责人
	private String gradeInstructor;        // 素质教师
	private String matchStartTime;         // 比赛开始时间
	private String matchEndTime;           // 比赛结束时间
	private String joinDeadline;           // 报名截止时间
	private String judgingInformation;     // 裁判信息
	private String competitionStandards;   // 比赛标准
	private String prizeDescription;       // 奖品说明
	private String imageUrl;               // 比赛图片
	private int comId;                     // 赛事编号
	private String userId;                    // 用户编号
	private int commend;                   // 是否展示
	private int publish;                   // 是否发布

	// Constructors
	public MatchesDTO() {
	}

	// Getter 和 Setter 方法
	public int getMatchId() {
		return matchId;
	}

	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getMatchLocal() {
		return matchLocal;
	}

	public void setMatchLocal(String matchLocal) {
		this.matchLocal = matchLocal;
	}

	public String getSponsorContact() {
		return sponsorContact;
	}

	public void setSponsorContact(String sponsorContact) {
		this.sponsorContact = sponsorContact;
	}

	public String getMatchLeader() {
		return matchLeader;
	}

	public void setMatchLeader(String matchLeader) {
		this.matchLeader = matchLeader;
	}

	public String getGradeInstructor() {
		return gradeInstructor;
	}

	public void setGradeInstructor(String gradeInstructor) {
		this.gradeInstructor = gradeInstructor;
	}

	public String getMatchStartTime() {
		return matchStartTime;
	}

	public void setMatchStartTime(String matchStartTime) {
		this.matchStartTime = matchStartTime;
	}

	public String getMatchEndTime() {
		return matchEndTime;
	}

	public void setMatchEndTime(String matchEndTime) {
		this.matchEndTime = matchEndTime;
	}

	public String getJoinDeadline() {
		return joinDeadline;
	}

	public void setJoinDeadline(String joinDeadline) {
		this.joinDeadline = joinDeadline;
	}

	public String getJudgingInformation() {
		return judgingInformation;
	}

	public void setJudgingInformation(String judgingInformation) {
		this.judgingInformation = judgingInformation;
	}

	public String getCompetitionStandards() {
		return competitionStandards;
	}

	public void setCompetitionStandards(String competitionStandards) {
		this.competitionStandards = competitionStandards;
	}

	public String getPrizeDescription() {
		return prizeDescription;
	}

	public void setPrizeDescription(String prizeDescription) {
		this.prizeDescription = prizeDescription;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String image) {
		this.imageUrl = image;
	}

	public int getComId() {
		return comId;
	}

	public void setComId(int comId) {
		this.comId = comId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCommend() {
		return commend;
	}

	public void setCommend(int commend) {
		this.commend = commend;
	}

	public int getPublish() {
		return publish;
	}

	public void setPublish(int publish) {
		this.publish = publish;
	}

	@Override
	public String toString() {
		return "MatchesDTO{" +
				"matchId=" + matchId +
				", matchName='" + matchName + '\'' +
				", matchLocal='" + matchLocal + '\'' +
				", sponsorContact='" + sponsorContact + '\'' +
				", matchLeader='" + matchLeader + '\'' +
				", gradeInstructor='" + gradeInstructor + '\'' +
				", matchStartTime='" + matchStartTime + '\'' +
				", matchEndTime='" + matchEndTime + '\'' +
				", joinDeadline='" + joinDeadline + '\'' +
				", judgingInformation='" + judgingInformation + '\'' +
				", competitionStandards='" + competitionStandards + '\'' +
				", prizeDescription='" + prizeDescription + '\'' +
				", imageUrl='" + imageUrl + '\'' +
				", comId=" + comId +
				", userId=" + userId +
				", commend=" + commend +
				", publish=" + publish +
				'}';
	}
}
