package com.kosi.kosi_safety_training_vt.util;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
public class FilesUtil {

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static String getPathByOS(String path) {
        return isWindows() ? "c:" + path : path;
    }

    public static String uploadToVideoPath(String uploadTrainingVideoPath, MultipartFile uploadVideoFile) throws IOException {
        String fileName = UUID.randomUUID() + "_" + uploadVideoFile.getOriginalFilename(); // 파일명 충돌 방지
        String filePath = uploadTrainingVideoPath + "/" + fileName;

        File videoFilePath = new File(filePath);
        uploadVideoFile.transferTo(videoFilePath);
        return filePath;
    }

    public static void responseFileDownload(HttpServletResponse response, String filePath, String fileName, String contentType, boolean isInline) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        File file = new File(filePath);

        response.setContentType(contentType);
        if(isInline){
            response.setHeader("Content-Disposition", "inline" + "; filename*=UTF-8''" + encodedFileName);
        }else {
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        }
        response.setContentLengthLong(file.length());

        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {
            FileCopyUtils.copy(fileInputStream, outputStream);
        }

        if (file.exists() && !file.delete()) {
            log.error("Failed to delete file: " + file.getAbsolutePath());
        }
    }

    public static void responseFileDownload(HttpServletResponse response, byte[] fileData, String fileName, String contentType, boolean isInline) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replaceAll("\\+", "%20");
        File file = convertByteArrayToFile(fileData, fileName); // byte[]를 파일로 저장

        response.setContentType(contentType);
        if (isInline) {
            response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encodedFileName);
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        }
        response.setContentLengthLong(file.length());

        try (OutputStream outputStream = response.getOutputStream();
             FileInputStream fileInputStream = new FileInputStream(file)) {
            FileCopyUtils.copy(fileInputStream, outputStream);
        }

        if (file.exists() && !file.delete()) {
            log.error("Failed to delete file: " + file.getAbsolutePath());
        }
    }

    public static File convertByteArrayToFile(byte[] fileData, String fileName) throws IOException {
        File tempFile = File.createTempFile("temp_", "_" + fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(fileData);
        }
        return tempFile;
    }

    public static byte[] resizeImage(byte[] originalImageFile) throws Exception {
        int width = 200, height = 200;
        // byte[] → InputStream
        ByteArrayInputStream bais = new ByteArrayInputStream(originalImageFile);

        // InputStream → BufferedImage
        BufferedImage originalImage = ImageIO.read(bais);
        if (originalImage == null) {
            throw new IOException("Invalid image data");
        }

        // 이미지 리사이즈
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        // BufferedImage → byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);
        baos.flush();
        byte[] resizedImageBytes = baos.toByteArray();
        baos.close();

        return resizedImageBytes;
    }
}
