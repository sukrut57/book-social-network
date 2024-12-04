package com.api.socialbookbackend.book;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {

    public static byte[] readFromFileLocation(String fileUrl) {
        if(StringUtils.isNotBlank(fileUrl)){
            return null;
        }
        try{
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        }catch (IOException e){
            log.warn("Failed to read file from location: {}", fileUrl);
        }
        return null;
    }
}
