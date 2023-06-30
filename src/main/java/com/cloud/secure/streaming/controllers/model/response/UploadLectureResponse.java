package com.cloud.secure.streaming.controllers.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class UploadLectureResponse {

    private String filename;
    private long size;

    public UploadLectureResponse(String filename, MultipartFile multipartFile) {

        this.filename = filename;
        this.size = multipartFile.getSize();
    }
}
