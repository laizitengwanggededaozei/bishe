package neu.competition.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("User")
public class User  implements Serializable {

	private String id;//学号

	private String uname;//姓名

	private String umail;//邮箱

	private String upwd;//密码

	private String uprofilepic;//头像url


	public User(String id, String uname, String umail, String upwd, String uproilepic,String role) {
		this.id = id;
		this.uname = uname;
		this.umail = umail;
		this.upwd = upwd;
		this.uprofilepic = uprofilepic;
	}

}
