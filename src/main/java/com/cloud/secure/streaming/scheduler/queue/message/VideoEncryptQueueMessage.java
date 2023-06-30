package com.cloud.secure.streaming.scheduler.queue.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 689Cloud
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VideoEncryptQueueMessage {

    private String messageId;
    private String fileId;
    private String fileName;
    private String filePath;

}
