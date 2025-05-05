package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("participation_record")
public class ParticipationRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private int teamId;
    private int matchId;
    private String status = "ACTIVE"; // 报名状态：ACTIVE-有效, CANCELED-已撤销
    private LocalDateTime registrationTime;
    private String canceledBy; // 撤销人ID
    private LocalDateTime cancelTime; // 撤销时间

    public ParticipationRecord(int teamId, int matchId, LocalDateTime registrationTime) {
        this.teamId = teamId;
        this.matchId = matchId;
        this.registrationTime = registrationTime;
    }

    public ParticipationRecord(int teamId, int matchId, String status, LocalDateTime registrationTime,
                               String canceledBy, LocalDateTime cancelTime) {
        this.teamId = teamId;
        this.matchId = matchId;
        this.status = status;
        this.registrationTime = registrationTime;
        this.canceledBy = canceledBy;
        this.cancelTime = cancelTime;
    }
}