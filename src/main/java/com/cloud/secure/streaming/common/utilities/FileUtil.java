/*
 * Copyright (c) 689Cloud LLC. All Rights Reserved.
 * This software is the confidential and proprietary information of 689Cloud,
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with 689Cloud.
 */
package com.cloud.secure.streaming.common.utilities;

import com.cloud.secure.streaming.common.exceptions.ApplicationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Class Name: FileUtil.java
 * @author: HoGo
 * @created: Jan 2, 2013
 * @version: Beta
 * @author: HoGo
 */
public class FileUtil {

    public final Logger LOGGER = (Logger) LogFactory.getLog(this.getClass());

    private String parentName = null;
    private File dir = null;

    private static final Map<String, String> MIME_TYPE_TO_EXTENSION_MAP;

    static {
        MIME_TYPE_TO_EXTENSION_MAP = new HashMap<>();

        try {
            InputStream is = MimeTypeUtils.class.getResourceAsStream("/data/mimetypes.default");
            if (is != null) {
                try {
                    loadStream(is);
                } finally {
                    is.close();
                }
            }
        } catch (IOException e) {
            // ignore
            System.err.println(e.getMessage());
        }
    }

    private static void loadStream(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            addMimeTypes(line);
        }
    }

    public static synchronized void addMimeTypes(String mime_types) {
        int hashPos = mime_types.indexOf('#');
        if (hashPos != -1) {
            mime_types = mime_types.substring(0, hashPos);
        }
        StringTokenizer tok = new StringTokenizer(mime_types);
        if (!tok.hasMoreTokens()) {
            return;
        }
        String contentType = tok.nextToken();
        while (tok.hasMoreTokens()) {
            String fileType = tok.nextToken();
            MIME_TYPE_TO_EXTENSION_MAP.put(fileType, contentType);
        }
    }

    /**
     * @param parentName
     * @param dir
     */
    FileUtil(String parentName, File dir) {
        this.parentName = parentName;
        this.dir = dir;
    }

    /**
     * Delete file
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        return false;
    }

    public static void deleteDirectory2(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            //
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                deleteFile(files[i].getPath());
            }
            //
            dir.delete();
        }
    }

    public static void deleteDirectory(String path) {
        if (path != null) {
            File dir = new File(path);
            if (dir.exists()) {
                // 
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        deleteFile(files[i].getPath());
                    } else {
                        deleteDirectory(files[i].getPath());
                    }
                }
                // 
                dir.delete();
            }
        }
    }

    public static boolean deleteDirectory3(String path)  {
        try{
            if (path != null) {
                File dir = new File(path);
                if (dir.exists()) {
                    //
                    File[] files = dir.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        if (files[i].isFile()) {
                            FileUtils.forceDelete(files[i]);
                        } else {
                            deleteDirectory3(files[i].getPath());
                        }
                    }
                    //
                    dir.delete();
                }
            }
            return true;
        } catch (IOException e){
            return false;
        }
    }

    public static void copyFile(String in, String out) throws Exception {

        try (
                FileInputStream fileInputStream = new FileInputStream(in);
                FileOutputStream fileOutputStream = new FileOutputStream(out)
        ) {
            FileChannel ic = fileInputStream.getChannel();
            FileChannel oc = fileOutputStream.getChannel();
            oc.transferFrom(ic, 0, ic.size());
        }


    }

    public static void copyFolder(String in, String out) throws Exception {

        File newdir = new File(out);
        if (!newdir.exists()) {
            newdir.mkdir();
        }

        File orgdir = new File(in);

        File[] files = orgdir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {

                copyFile(files[i].getPath(), newdir.getPath() + "\\" + files[i].getName());
            }
        }
    }

    public static String[] getLines(String filePath) throws IOException {
        try (BufferedReader input = new BufferedReader(new FileReader(filePath))) {
            ArrayList list = new ArrayList(5000);
            String line = null;
            while ((line = input.readLine()) != null) {
                list.add(line);
            }
            String[] lines = new String[list.size()];
            list.toArray(lines);
            return lines;
        }
    }

    public static void zip(String path) throws IOException {
        int point = path.lastIndexOf(".");
        zip(path, path.substring(0, point));
    }

    public static void zip(String path, String zipFileName) throws IOException {

        File file = new File(path);
        File zipFile = new File(zipFileName + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));

        if (file.isFile()) {
            addTargetFile(zos, file, file.getName());

        } else {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                int point = zipFile.getName().lastIndexOf(".");
                folderZip(fileList[i].getPath(), fileList[i].getName(), zos, zipFile.getName().substring(0, point));
            }
        }
        zos.close();

    }

    private static void folderZip(String path, String fileName, ZipOutputStream zos, String zipFileName) throws IOException {

        File file = new File(path);

        if (file.isFile()) {
            addTargetFile(zos, file, fileName);
        } else {
            int point = file.getPath().lastIndexOf(zipFileName);
            String zipfolderPath = file.getPath().substring(point + zipFileName.length());
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                folderZip(fileList[i].getPath(), zipfolderPath + "\\" + fileList[i].getName(), zos, zipFileName);
            }
        }
    }

    public static void addTargetFile(ZipOutputStream zos, File file, String fileName) throws IOException {
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            ZipEntry target = new ZipEntry(fileName);
            zos.putNextEntry(target);
            int EOF = -1;
            byte buf[] = new byte[1024];
            int count;
            while ((count = bis.read(buf, 0, 1024)) != EOF) {
                zos.write(buf, 0, count);
            }
            zos.closeEntry();
        }finally {
            assert bis != null;
            bis.close();
        }
    }

    public static String getPrefix(String fileName) {
        if (fileName == null) {
            return null;
        }
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(0, point);
        }
        return fileName;
    }

    public static String getFileSizeString(int fileSize) {
        int Mb = fileSize / 1024 / 1024;
        if (Mb > 0) {
            return String.valueOf((fileSize / 1024 / 1024)) + "MB";
        } else {
            return String.valueOf((fileSize / 1024)) + "KB";
        }
    }




    public static boolean convertBase64Image(String base64String, String filePath) throws IOException {
        if (base64String == null || "".equals(base64String)) {
            return false;
        }
        OutputStream outputStream = null;
        InputStream fis = null;
        try {
            byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64((base64String.substring(base64String.indexOf(",") + 1)).getBytes());
            fis = new ByteArrayInputStream(bI);
            // write the inputStream to a FileOutputStream
            File destFile = new File(filePath);
            outputStream = new FileOutputStream(destFile);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = fis.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            return true;
        } catch (IOException e) {
            return false;
        }finally {
            assert outputStream != null;
            outputStream.close();
            fis.close();
        }
    }

    protected Vector pathNames() {
        String[] list = dir.list();
        File[] files = new File[list.length];
        Vector vec = new Vector();
        for (int i = 0; i < list.length; i++) {

            String pathName = parentName
                    + File.separator + list[i];
            files[i] = new File(pathName);
            if (files[i].isFile()) {
                vec.addElement(files[i].getPath());
            }
        }

        return vec;
    }

    public static void checkExtensionType(MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
        }
        String extensionType = FilenameUtils.getExtension(file.getOriginalFilename()).toLowerCase();
        //validate extension
        String[] extensions = {"png", "pjp", "jpg", "jpeg", "jfif", "pjpeg", "ico"};
        if (!Arrays.asList(extensions).contains(extensionType)) {
            throw new ApplicationException(RestAPIStatus.ERR_INVALID_FILE);
        }
    }

    public static String getExtensionBase64(String base64) {

        String delims="[,]";
        String[] parts = base64.split(delims);
        String imageString = parts[0];

        String delimiter="[/]";
        String type = imageString.split(delimiter)[1].split("[;]")[0];
        return type;

    }

    public static void downloadTeamRecieptFromServer(HttpServletResponse response, String RealPath, Date createDate) throws IOException {
        InputStream inputStream = null;
        try {
            File initialFile = new File(RealPath);
            inputStream = new FileInputStream(initialFile);
            // Do Download
            int contentLength = inputStream.available();
            response.setContentLength(contentLength);
            response.addHeader("Content-Length", Long.toString(contentLength));
            //read from the file; write to the ServletOutputStream
            String ext = FilenameUtils.getExtension(RealPath);

            response.setContentType(getMineType(ext)); // Fixing bug: Lack extention on IE9
            response.setHeader("Content-Type", getMineType(ext));

            response.setHeader("Content-Disposition", "attachment; filename=\"receipt_" + createDate.toString() + "." + ext + "\""); // Fix bug: Lack file name on Firefox
            ServletOutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            try {
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (Exception e) {
                System.err.println(e);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileUtil.class.getName()).log(Level.INFO, null, ex);
        } finally {
            assert (inputStream != null);
            inputStream.close();
        }
    }

    public static String getMineType(String ext) {
        String mine = "application/octet-stream";
        for (Map.Entry<String, String> en : MIME_TYPE_TO_EXTENSION_MAP.entrySet()) {
            if (ext.equals(en.getValue())) {
                mine = en.getKey();
            }
        }
        return mine;
    }
}
