package neu.competition.config;

import lombok.extern.slf4j.Slf4j;
import neu.competition.base.ServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller异常捕捉器
 * 解析ServiceException，并返回指定状态码和跳转指定位置
 */
@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ServiceException.class)
    public String exceptionHandler(HttpServletRequest request, ServiceException e, RedirectAttributes redirectAttrs) {
        if (StringUtils.hasText(e.getForwardPath())) {
            // 当ServiceException对象中明确指定了跳转的视图名称时，跳转过去并带入错误信息
            request.setAttribute("ERR_MSG", e.getMessage());
            request.setAttribute("ERR_CODE", e.getCode());
            request.setAttribute("ERR_STATUS", e.getStatus());
            return e.getForwardPath();
        } else {
            // 当ServiceException对象中没有明确指定跳转的视图名称时，则重定向回referer路径，同时强制写入错误信息
            redirectAttrs.addFlashAttribute("ERR_MSG", e.getMessage());
            redirectAttrs.addFlashAttribute("ERR_CODE", e.getCode());
            redirectAttrs.addFlashAttribute("ERR_STATUS", e.getStatus());
            return "redirect:" + request.getHeader("referer");
        }
    }
}
