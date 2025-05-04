package neu.competition.service.impl;

import neu.competition.service.ResourceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 资源的业务处理
 * 如上传的资源文件等业务
 *
 * @author lemonit.cn-liuri
 */
@Service
@Transactional
public class ResourceServiceImpl implements ResourceService {
    @Value("${neumall.workspace}")
    private String workspacePath;
    @Value("${neumall.resource-prefix}")
    private String resourcePrefix;

    /**
     * 将上传的文件转存到服务器指定目录并生成资源路径
     *
     * @param uploadFile Multipart文件对象
     * @param toPath     要转存的本地路径目录（目录自动拼接到config:neumall/workspace父路径之后）
     * @return 上传后的相对资源路径
     */
    @Override
    public String uploadFileTransfer(MultipartFile uploadFile, String toPath) {
        String originalFileName = uploadFile.getOriginalFilename();
        String finalSubPath = toPath + "/" + UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        String finalPath = workspacePath + finalSubPath;
        File finalFile = new File(finalPath);
        if (!finalFile.getParentFile().exists()) {
            finalFile.getParentFile().mkdirs();
        }
        try {
            uploadFile.transferTo(finalFile);
            return resourcePrefix + finalSubPath;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
