package neu.competition.define;

import neu.competition.base.ServiceException;

/**
 * 用户相关业务处理产生的异常预定义
 *
 * @author lemonit.cn-liuri
 */
public class UserServiceExceptionDefine {
    public static final ServiceException REGISTER_NUMBER_ALREADY_EXISTS = new ServiceException("您填写的账号已经存在", "REGISTER_NUMBER_ALREADY_EXISTS", 409, "edge/register");
    public static final ServiceException REGISTER_CONFIRM_PASSWORD_MISMATCH = new ServiceException("您两次输入的密码不一致", "REGISTER_CONFIRM_PASSWORD_MISMATCH", 412, "edge/register");
}
