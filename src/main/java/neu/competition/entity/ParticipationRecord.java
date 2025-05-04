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
    // 移除@TableId注解
    private int teamId;
    private int matchId;
    private LocalDateTime registrationTime;
}
