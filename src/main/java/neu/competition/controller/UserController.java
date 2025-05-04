package neu.competition.controller;

import neu.competition.entity.User;
import neu.competition.service.FileService2;
import neu.competition.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private FileService2 fileService2;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        logger.info("Accessed login page");
        model.addAttribute("user", new User());
        return "competition/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("id") String id,
                        @RequestParam("upwd") String upwd,
                        Model model, HttpSession session) {
        String processedId = addPrefixToId(id);
        logger.info("Attempting login for user ID: {}", processedId);
        User user = userService.findByIdAndPassword(processedId, upwd);
        if (user != null) {
            logger.info("Login successful for user ID: {}", processedId);
            session.setAttribute("loggedUser", user);
            session.setAttribute("role", getRoleFromPrefix(processedId));
            System.out.println("Stored role in session: " + session.getAttribute("role"));
            return "redirect:/index";
        } else {
            logger.warn("Login failed for user ID: {}", processedId);
            model.addAttribute("loginError", "无效的账号或密码");
            model.addAttribute("user", new User());
            return "competition/login";
        }
    }

    private String addPrefixToId(String id) {
        if (id.startsWith("S") || id.startsWith("T") || id.startsWith("B")) {
            return id;
        }

        String[] prefixes = {"S", "T", "B"};
        for (String prefix : prefixes) {
            String newId = prefix + id;
            if (userService.existsById(newId)) {
                return newId;
            }
        }

        // 如果没有找到匹配的前缀，默认添加学生前缀
        return "S" + id;
    }

    private String getRoleFromPrefix(String id) {
        if (id.startsWith("S")) {
            return "学生";
        } else if (id.startsWith("T")) {
            return "教师";
        } else
            return "组织者";
    }

    private boolean isNonEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.info("Accessed registration page");
        model.addAttribute("user", new User());
        List<String> roles = Arrays.asList("学生", "教师", "组织者");
        model.addAttribute("roles", roles);
        return "competition/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam("uname") String uname,
                           @RequestParam("id") String id,
                           @RequestParam("umail") String umail,
                           @RequestParam("upwd") String upwd,
                           @RequestParam("role") String role, Model model) {
        logger.info("Registering user: {}", id);
        String prefix = "";
        switch (role) {
            case "学生":
                prefix = "S";
                break;
            case "教师":
                prefix = "T";
                break;
            case "组织者":
                prefix = "B";
                break;
            default:
                model.addAttribute("registrationError", "无效的角色选择");
                populateUserModel(model, uname, id, umail, role);
                return "competition/register";
        }
        String processedId = prefix + id;
        logger.debug("Processed user ID with prefix: {}", processedId);

        // 检查ID、用户名、邮箱的唯一性
        if (userService.existsById(processedId)) {
            model.addAttribute("registrationError", "该ID已被注册");
            populateUserModel(model, uname, id, umail, role);
            return "competition/register";
        }

        if (userService.existsByUname(uname)) {
            model.addAttribute("registrationError", "该用户名已被注册");
            populateUserModel(model, uname, id, umail, role);
            return "competition/register";
        }

        if (userService.existsByUmail(umail)) {
            model.addAttribute("registrationError", "该邮箱已被注册");
            populateUserModel(model, uname, id, umail, role);
            return "competition/register";
        }

        // 检查用户名和邮箱是否为空
        if (uname.isEmpty() || umail.isEmpty()) {
            logger.warn("Registration failed due to empty username or email for user: {}", id);
            model.addAttribute("registrationError", "用户名和邮箱不能为空");
            populateUserModel(model, uname, id, umail, role);
            return "competition/register";
        }

        // 检查密码强度
        if (!isStrongPassword(upwd)) {
            logger.warn("Password strength validation failed for user: {}", id);
            model.addAttribute("passwordError",
                    "密码强度不够，请确保密码长度至少8位，" +
                            "含有数字、字母、符号且包含大小写字母。");
            populateUserModel(model, uname, id, umail, role);
            return "competition/register";
        }

        // 创建新用户并保存
        User newUser = new User()
                .setId(processedId)
                .setUname(uname)
                .setUmail(umail)
                .setUpwd(upwd);

        userService.Save(newUser);
        logger.info("User registered successfully: {}", processedId);
        return "redirect:/profile";
    }

    // 这是一个帮助方法，用于在发生错误时填充模型
    private void populateUserModel(Model model, String uname, String id, String umail, String role) {
        User user = new User();
        user.setUname(uname);
        user.setId(id);
        user.setUmail(umail);
        model.addAttribute("user", user);
        model.addAttribute("selectedRole", role);

        // 确保角色列表也被添加到模型中
        List<String> roles = Arrays.asList("学生", "教师", "组织者");
        model.addAttribute("roles", roles);
    }

    // 密码强度验证方法
    private boolean isStrongPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSymbol = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) hasDigit = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (!Character.isLetterOrDigit(c)) hasSymbol = true;
            if (hasDigit && hasLower && hasUpper && hasSymbol) return true;
        }
        return false;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model, HttpSession session) throws UnsupportedEncodingException {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            logger.warn("Access to profile page without login");
            return "redirect:/login";
        }
        String encodedUname = URLEncoder.encode(user.getUname(), StandardCharsets.UTF_8.toString());
        model.addAttribute("user", user);
        model.addAttribute("encodedUname", encodedUname);
        logger.info("Profile page accessed for user: {}", user.getId());
        return "competition/profile";
    }

    @GetMapping("/profile/modify")
    public String showProfileModifyPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            logger.warn("Access to profile modify page without login");
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "competition/profileModify";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser,
                                @RequestParam("profilePicFile") MultipartFile profilePicFile,
                                HttpSession session) {
        try {
            User loggedUser = (User) session.getAttribute("loggedUser");
            if (loggedUser == null) {
                logger.warn("Attempt to update profile without login");
                return "redirect:/login";
            }

            // 检查邮箱的唯一性
            if (userService.existsByUmail(updatedUser.getUmail()) && !loggedUser.getUmail().equals(updatedUser.getUmail())) {
                logger.warn("Email already exists: {}", updatedUser.getUmail());
                return "redirect:/profile/modify?error=email";
            }

            // 更新会话中的用户信息
            userService.updateUserProfile(loggedUser, updatedUser, profilePicFile);
            session.setAttribute("loggedUser", loggedUser);
            logger.info("Profile updated for user: {}", loggedUser.getId());
            return "redirect:/profile";
        } catch (Exception e) {
            logger.error("Error updating profile for user: {}", updatedUser.getId(), e);
            return "redirect:/profile/modify?error=update";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        User user = (User) session.getAttribute("loggedUser");
        if (user != null) {
            logger.info("User logged out: {}", user.getId());
        }
        session.invalidate();
        return "redirect:/login";
    }
}
