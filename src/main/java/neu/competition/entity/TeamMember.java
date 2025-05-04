package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@TableName("team_member")
public class TeamMember implements Serializable {
    // 不使用@TableId，而是利用MP的声明式处理方式
    private String uid;
    private int teamId;
    private String uname;
    private String role;
    public void setRole(String role) {
        this.role = role;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
