package neu.competition.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 为spring security定义的密码解码编码器
 * <p>
 * 使用sha256作为数据库密码存储密文方式
 * <p>
 * 匹配时候使用sha256对原密码进行两次摘要计算，然后与库中密码做匹配
 * 之所以密码都做两次sha256摘要是因为一次摘要还是容易被撞库破解
 *
 * @author lemonit.cn-liuri
 */
@Component
public class SecurityPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(rawPassword.toString()));
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return DigestUtils.sha256Hex(DigestUtils.sha256Hex(rawPassword.toString())).equals(encodedPassword);
    }
}
