package neu.competition.define;

/**
 * 系统角色相关字符串预定义
 *
 * @author lemonit.cn-liuri
 */
public class SystemRoleDefine {

    public static final String BASE_USER = "BASE_USER";

    public static final String SUPER_ADMIN = "SUPER_ADMIN";


    public static final String HAS_ROLE_BASE_USER = "hasAnyRole('" + SystemRoleDefine.BASE_USER + "')";
    public static final String HAS_ROLE_SUPER_ADMIN = "hasAnyRole('" + SystemRoleDefine.SUPER_ADMIN + "')";
}
