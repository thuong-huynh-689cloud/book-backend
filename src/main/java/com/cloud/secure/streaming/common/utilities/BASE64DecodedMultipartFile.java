package com.cloud.secure.streaming.common.utilities;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

public class BASE64DecodedMultipartFile implements MultipartFile {
    private byte[] imgContent;
    private String typeFile;
    private String name;

    public BASE64DecodedMultipartFile(String base64, String name) {
        this.name = name;
        splitBase64(base64);
    }

    public BASE64DecodedMultipartFile(String base64) {
        this.name = "file";
        splitBase64(base64);
    }

    private void splitBase64(String base64) {
        String[] arrayBase64 = base64.split(",", 2);
        byte[] imgContent = Base64.getDecoder().decode(arrayBase64[1]);
        this.imgContent = imgContent;
        this.typeFile = FileUtil.getExtensionBase64(base64);

    }

    @Override
    public String getName() {
        return this.name + "_" + System.currentTimeMillis() + "." + this.typeFile;
    }

    @Override
    public String getOriginalFilename() {
        return this.name + "_" + System.currentTimeMillis() + "." + this.typeFile;
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }

    @Override
    public long getSize() {
        return imgContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
}