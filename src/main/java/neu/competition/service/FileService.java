package neu.competition.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {

    public String saveFile(MultipartFile file) {
        String url = null;
        if (!file.isEmpty()) {
            try {
                // 获取文件名
                String fileName = file.getOriginalFilename();

                // 构建目标路径
                Path targetDirectory = Paths.get("src/main/resources/static/image/team/");
                Path targetPath = null;
                if (fileName != null) {
                    targetPath = targetDirectory.resolve(fileName);
                }

                // 确保目标目录存在
                Files.createDirectories(targetDirectory);

                // 将文件写入目标目录
                Files.write(targetPath, file.getBytes());

                // 使用相对路径保存
                url = "/image/team/" + fileName;

                System.out.println("文件已成功保存到指定目录");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("文件保存过程中发生错误：" + e.getMessage());
            }
        }

        return url;
    }
}
