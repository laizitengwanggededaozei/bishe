package neu.competition.controller;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author 25853
 * @version 1.0
 * @Create by 2025/5/4 22:49
 */

public class PageController {
    @GetMapping("/unauthorized")
    public String unauthorized() {
        return "unauthorized";
    }
}
