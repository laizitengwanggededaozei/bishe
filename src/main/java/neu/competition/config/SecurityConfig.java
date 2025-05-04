package neu.competition.config;

/**
 * 对SpringSecurity安全认证的配置
 *
 * @author lemonit.cn-liuri
 */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
/*    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 此行为了解决security默认不运行页面被嵌入iframe
                .headers().frameOptions().disable().and()
                // 放开第三方库 样式 图片 和js库的权限校验
                .authorizeHttpRequests()
                .antMatchers("/libs/**", "/styles/**", "/javascript/**", "/images/**", "/res/**").permitAll()
                .antMatchers("/", "/edge/**").permitAll()
                .anyRequest()
                .authenticated()
                // 自定义登录页面的字段
                .and()
                .formLogin()
//                .loginPage("/edge/login")
//                .loginProcessingUrl("/edge/handler/login")
                .usernameParameter("number")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .permitAll()
                // 配置退出登录
//                .and()
//                .logout()
//                .logoutUrl("/user-center/handler/logout")
//                .logoutSuccessUrl("/")

                .and()
                .csrf()
                .disable();
        ;
        return http.build();
    }
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(new SecurityPasswordEncoder());
    }*/
}
