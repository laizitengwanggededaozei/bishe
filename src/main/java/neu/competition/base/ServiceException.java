package neu.competition.base;

import lombok.Data;

/**
 * 业务产生的异常
 *
 * @author lemonit.cn-liuri
 */
@Data
public class ServiceException extends Exception {
    /**
     * 错误码
     */
    private String code;
    /**
     * 状态码
     */
    private Integer status;
    /**
     * MVC模式下重定向到的目标位置
     */
    private String forwardPath;

    public ServiceException(String message, String code, Integer status, String forwardPath) {
        super(message);
        this.code = code;
        this.status = status;
        this.forwardPath = forwardPath;
    }
}
