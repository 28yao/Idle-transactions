package com.campus.marketplace.service;

import com.campus.marketplace.exception.BusinessException;
import com.campus.marketplace.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Value("${file.upload-path}")
    private String uploadPath;

    @Value("${file.access-url}")
    private String accessUrl;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );

    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024L; // 5MB

    public String uploadImage(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase())) {
            throw new BusinessException(ErrorCode.FILE_TYPE_ERROR);
        }

        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BusinessException(ErrorCode.FILE_SIZE_ERROR);
        }

        String originalName = file.getOriginalFilename();
        String ext = "";
        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf('.')).toLowerCase();
        }
        String fileName = UUID.randomUUID().toString().replace("-", "") + ext;

        String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String relativeDir = subDir + "/" + dateDir;
        File targetDir = new File(uploadPath + relativeDir);
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        File targetFile = new File(targetDir, fileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_UPLOAD_ERROR);
        }

        return accessUrl + relativeDir + "/" + fileName;
    }
}
