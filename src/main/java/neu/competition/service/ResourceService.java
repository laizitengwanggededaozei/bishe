package neu.competition.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 资源的业务处理
 * 如上传的资源文件等业务
 *
 * @author lemonit.cn-liuri
 */
public interface ResourceService {
    /**
     * 将上传的文件转存到服务器指定目录并生成资源路径
     *
     * @param uploadFile Multipart文件对象
     * @param toPath     要转存的本地路径目录（目录自动拼接到config:neumall/workspace父路径之后）
     * @return 生成的完成的子路径
     */
    String uploadFileTransfer(MultipartFile uploadFile, String toPath);
}
