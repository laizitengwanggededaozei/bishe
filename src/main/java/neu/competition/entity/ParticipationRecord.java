package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("participation_record") // 确认表名一致
public class ParticipationRecord implements Serializable {
    private int teamId;
    private int matchId;
}
