package com.project.Ecommerce.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {
    public String uploadimage(String uploadDir, MultipartFile file) throws IOException {
        // Create directory if not exists
        File dir = new File( uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        // ✔ Create unique name for file
        String uniqueName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();


        // ✔ Path to save file
        Path path = Paths.get(uploadDir + uniqueName);


        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return uniqueName;
    }
}
