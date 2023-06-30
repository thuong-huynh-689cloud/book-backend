package com.cloud.secure.streaming.scheduler.processor;

import com.cloud.secure.streaming.scheduler.queue.message.VideoEncryptQueueMessage;


public abstract class VideoEncryptProcessor {

    public abstract void doProcess(VideoEncryptQueueMessage message);

}
