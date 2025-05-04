package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Getter
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("team")
public class Team implements Serializable {
    // Getters and Setters
    private int id;
    private String tname;
    private String logo;
    @TableField(exist = false)
    private String leaderName;
    // 添加成员列表
    @TableField(exist = false)
    private List<TeamMember> members;

    public void setMembers(List<TeamMember> members) {
        this.members = members;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
