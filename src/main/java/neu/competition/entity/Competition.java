package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;

import java.io.Serializable;

@Getter
@TableName("competition")
public class Competition implements Serializable {
    // Getter 和 Setter 方法
    @TableId
    private int comId;       // 赛事编号
    private String comName;      // 赛事名称
    private String organizer;    // 主办方
    private String comLevel;     // 赛事级别
    private String discipline;   // 学科
    private String comWebsite;   // 赛事官网
    private String comProfile;   // 赛事简介
    private String comPurpose;   // 赛事宗旨
    private String comOther;     // 其他说明
    private String imageUrl;        // 赛事标识
    private String userId;          // 用户编号
    private int commend;         // 是否展示

    // Constructors
    public Competition() {
    }

    public void setComId(int comId) {
        this.comId = comId;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public void setComLevel(String comLevel) {
        this.comLevel = comLevel;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public void setComWebsite(String comWebsite) {
        this.comWebsite = comWebsite;
    }

    public void setComProfile(String comProfile) {
        this.comProfile = comProfile;
    }

    public void setComPurpose(String comPurpose) {
        this.comPurpose = comPurpose;
    }

    public void setComOther(String comOther) {
        this.comOther = comOther;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCommend(int commend) {
        this.commend = commend;
    }

    @Override
    public String toString() {
        return "CompetitionDTO{" +
                "comId=" + comId +
                ", comName='" + comName + '\'' +
                ", organizer='" + organizer + '\'' +
                ", comLevel='" + comLevel + '\'' +
                ", discipline='" + discipline + '\'' +
                ", comWebsite='" + comWebsite + '\'' +
                ", comProfile='" + comProfile + '\'' +
                ", comPurpose='" + comPurpose + '\'' +
                ", comOther='" + comOther + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", userId=" + userId +
                ", commend=" + commend +
                '}';
    }
}
