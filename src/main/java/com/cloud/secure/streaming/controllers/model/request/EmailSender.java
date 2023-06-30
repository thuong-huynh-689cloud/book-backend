package com.cloud.secure.streaming.controllers.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailSender {
    private String toEmail;
    private String subject;
    private String body;
    private String pathAttachment;
}
