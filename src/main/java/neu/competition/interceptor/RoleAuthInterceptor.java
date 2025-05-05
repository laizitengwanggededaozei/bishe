// 新建文件：src/main/java/neu/competition/interceptor/RoleAuthInterceptor.java
package neu.competition.interceptor;

import neu.competition.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RoleAuthInterceptor implements HandlerInterceptor {

    // 定义路径与角色的映射
    private static final Map<String, List<String>> PATH_ROLE_MAP = new HashMap<>();
    
    static {
        // 组织者专用路径
        PATH_ROLE_MAP.put("/competition/create", Arrays.asList("B"));
        PATH_ROLE_MAP.put("/competition/edit", Arrays.asList("B"));
        PATH_ROLE_MAP.put("/competition/publish", Arrays.asList("B"));
        PATH_ROLE_MAP.put("/competition/manage", Arrays.asList("B"));
        
        // 教师专用路径
        PATH_ROLE_MAP.put("/teacher/teams", Arrays.asList("T"));
        PATH_ROLE_MAP.put("/competition-process/evaluate", Arrays.asList("B", "T"));
        
        // 学生专用路径
        PATH_ROLE_MAP.put("/team/create", Arrays.asList("S"));
        PATH_ROLE_MAP.put("/team/register-competition", Arrays.asList("S"));
        PATH_ROLE_MAP.put("/competition-process/submit", Arrays.asList("S"));
        PATH_ROLE_MAP.put("/submitRegistration", Arrays.asList("S"));
        
        // 共享路径
        PATH_ROLE_MAP.put("/competition-process/results", Arrays.asList("B", "T", "S"));
    }
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        
        // 检查是否需要权限控制
        for (Map.Entry<String, List<String>> entry : PATH_ROLE_MAP.entrySet()) {
            if (requestURI.startsWith(entry.getKey())) {
                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("loggedUser");
                
                // 用户未登录，重定向到登录页面
                if (user == null) {
                    response.sendRedirect("/login");
                    return false;
                }
                
                // 获取用户角色
                String role = String.valueOf(user.getId().charAt(0));
                
                // 检查用户角色是否有权限
                if (!entry.getValue().contains(role)) {
                    response.sendRedirect("/unauthorized");
                    return false;
                }
                
                break;
            }
        }
        
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("loggedUser");
            
            if (user != null) {
                String role = String.valueOf(user.getId().charAt(0));
                modelAndView.addObject("userRole", role);
                
                // 根据角色设置不同的布局模板
                switch (role) {
                    case "B":
                        modelAndView.addObject("layoutTemplate", "layouts/organizer-layout");
                        break;
                    case "T":
                        modelAndView.addObject("layoutTemplate", "layouts/teacher-layout");
                        break;
                    case "S":
                        modelAndView.addObject("layoutTemplate", "layouts/student-layout");
                        break;
                    default:
                        modelAndView.addObject("layoutTemplate", "layouts/default-layout");
                }
            }
        }
    }
}