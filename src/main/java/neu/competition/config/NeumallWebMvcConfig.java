package neu.competition.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 动态MvcConfig，增加静态资源路径，让上传文件夹中的文件可以下载
 */
@Configuration
public class NeumallWebMvcConfig implements WebMvcConfigurer {

    @Value("${neumall.workspace}")
    private String workspacePath;
    @Value("${neumall.resource-prefix}")
    private String resourcePrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 添加静态资源路径，将上传文件夹的资源映射出来
        registry.addResourceHandler(resourcePrefix + "**").addResourceLocations("file:" + workspacePath);
    }
}
