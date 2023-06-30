package com.cloud.secure.streaming.common.utilities;

import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.encoder.DefaultCsvEncoder;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

/**
 * @author 689Cloud
 */
@Component
@Slf4j
public class AppUtil {
    public static final Random RANDOM = new SecureRandom();
    public static final String UTF8_BOM = "\uFEFF";

    /**
     * Encrypt a String to Hash MD5
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String encryptMD5(String value) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(value.getBytes());
        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
    /**
     * Generate random password
     * @param len
     * @return
     */
    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghi"
                +"jklmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }

    /**
     * Generate Salt for the password
     *
     * @return
     */
    public static String generateSalt() {
        byte[] salt = new byte[Constant.SALT_LENGTH];
        RANDOM.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }


    public static String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip == null || ip.length() == 0) {
            return "unknown";
        } else {
            return ip;
        }
    }

    public static String getFileExtension(MultipartFile file) {
        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename());
        if (extensionType != null) {
            extensionType = extensionType.toLowerCase();
        }
        return extensionType;
    }

    public static void exportCSV(String[] headers, String[] headersMapping,  List<?> datas, String csvFileName, HttpServletResponse response) {
        if (!csvFileName.endsWith(".csv")) {
            throw new ApplicationException(RestAPIStatus.INTERNAL_SERVER_ERROR, "Because of me");
        }
        response.setContentType("text/csv");
        // creates mock data
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", csvFileName);
        response.setHeader(headerKey, headerValue);
        response.setHeader("Download-File", "true");
        try {
            Writer writer = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
            writer.write('\uFEFF'); // BOM for UTF-*
            ICsvBeanWriter csvWriter = new CsvBeanWriter(
                    writer,
                    new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
                            .useEncoder(new DefaultCsvEncoder()).build());
            csvWriter.writeHeader(headers);

            if (!datas.isEmpty()) {
                // check contents != isEmpty
                datas.forEach(data -> {
                    try {
                        csvWriter.write(data, headersMapping);
                    } catch (IOException e) {
                        log.error("Error export users by team: " + e.getMessage());
                        throw new ApplicationException(RestAPIStatus.INTERNAL_SERVER_ERROR);
                    }
                });
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            log.error("Error export users by team: " + e.getMessage());
            throw new ApplicationException(RestAPIStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
