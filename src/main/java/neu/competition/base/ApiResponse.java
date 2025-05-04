package neu.competition.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * RestAPI接口的响应
 *
 * @author lemonit.cn-liuri
 */
@Setter
@Getter
@NoArgsConstructor
public class ApiResponse {
    /**
     * 请求响应状态码
     */
    private Integer statusCode = 200;
    /**
     * 请求业务是否执行成功
     */
    private Boolean success;
    /**
     * 请求如果失败，该字段为失败的错误码
     */
    private String errCode;
    /**
     * 请求成功时业务返回响应体
     */
    private Object body;

    public ApiResponse(Object body) {
        this.statusCode = 200;
        this.body = body;
        this.errCode = "";
        this.success = true;
    }

    public ApiResponse(Boolean success, Object body) {
        this.statusCode = 200;
        this.body = body;
        this.errCode = "";
        this.success = success;
    }

    public ApiResponse(String errCode, Integer statusCode) {
        this.success = false;
        this.body = null;
        this.errCode = errCode;
        this.statusCode = statusCode;
    }

    public ApiResponse(String errCode) {
        this.success = false;
        this.body = null;
        this.errCode = errCode;
        this.statusCode = 500;
    }

    public static ApiResponse success(Object body) {
        return new ApiResponse(true, body);
    }

    public static ApiResponse failed(String errCode, Integer statusCode) {
        return new ApiResponse(errCode, statusCode);
    }

    public static ApiResponse failed(String errCode) {
        return new ApiResponse(false, errCode);
    }
}
