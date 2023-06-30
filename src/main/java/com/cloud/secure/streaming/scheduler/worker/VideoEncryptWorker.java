package com.cloud.secure.streaming.scheduler.worker;

import com.cloud.secure.streaming.scheduler.processor.VideoEncryptProcessorImpl;
import com.cloud.secure.streaming.scheduler.queue.message.VideoEncryptQueueMessage;

import com.cloud.secure.streaming.scheduler.queue.VideoEncryptQueueManager;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * @author Huy Pham
 */

@Component
@Slf4j
public class VideoEncryptWorker {



    private VideoEncryptProcessorImpl videoEncryptProcessor;

    public VideoEncryptWorker(VideoEncryptProcessorImpl videoEncryptProcessor) {
        this.videoEncryptProcessor = videoEncryptProcessor;
    }

    public void doWork(){

        if(VideoEncryptQueueManager.getInstance().isAcceptedThread()) {

            VideoEncryptQueueMessage message = VideoEncryptQueueManager.getInstance().getVideoEncryptMessage();

            if (message != null) {

                log.info("Start encrypt video process for: " + message.getFileName());

                videoEncryptProcessor.doProcess(message);

            }
        }
    }

}
