package com.api.socialbookbackend.book;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.currentTimeMillis;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;


    /**
     * Saves the file in the file system.
     * @param sourceFile the file to save
     * @param bookId the book id
     * @param ownerId the owner id
     * @return the file path or null if the file could not be saved
     */
    public String saveFile(
            @Nonnull MultipartFile sourceFile,
            @Nonnull Long ownerId) {

        final String fileUploadSubPath = "users" + File.separator + ownerId;

        return uploadFile(fileUploadSubPath, sourceFile);
    }

    /**
     * Saves the file in the file system.
     * @param sourceFile the file to save
     * @return the file path or null if the file could not be saved
     */
    private String uploadFile(
            @Nonnull String fileUploadSubPath,
            @Nonnull MultipartFile sourceFile) {

        final String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);

        if(!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdirs();
            if(!folderCreated) {
                log.warn("Failed to create folder: {}", finalUploadPath);
                return null;
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());

        if(fileExtension == null) {
            log.warn("Failed to extract file extension from file: {}", sourceFile.getOriginalFilename());
            throw new IllegalArgumentException("Failed to extract file extension from file: " + sourceFile.getOriginalFilename());
        }
        String targetFilePath = finalUploadPath + File.separator + currentTimeMillis() + "." + fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            log.info("File saved to: {}", targetFilePath);
            return targetFilePath;
        }
        catch (IOException exception){
            log.error("Failed to save file: {}", targetFilePath, exception);
        }
        return null;
    }


    /**
     * Extracts the file extension from the original filename.
     * @param originalFilename the original filename
     * @return the file extension or null if the original filename is null or empty
     */
    private String getFileExtension(String originalFilename) {
        if(originalFilename == null || originalFilename.isEmpty()) {
            return null;
        }
        //.jpg
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex == -1) {
            return null;
        }
        return originalFilename.substring(lastDotIndex);
    }
}
