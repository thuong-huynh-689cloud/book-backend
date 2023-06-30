package com.cloud.secure.streaming.scheduler.queue;

import com.cloud.secure.streaming.scheduler.queue.message.VideoEncryptQueueMessage;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 689Cloud
 */
@Service
public class VideoEncryptQueueManager {


    private final int MAXIMUM_THREAD = 2;

    // Singleton Instance
    private static VideoEncryptQueueManager instance;

    /**
     * Singleton getter
     * @return VideoEncryptQueueManager instance
     */
    public static synchronized VideoEncryptQueueManager getInstance() {
        if (instance == null) {
            instance = new VideoEncryptQueueManager();
        }
        return instance;
    }

    private static Queue<VideoEncryptQueueMessage> videoEncryptQueue;

    private static Set<VideoEncryptQueueMessage> encryptingList;

    private VideoEncryptQueueManager() {

        videoEncryptQueue = new LinkedList<>();

        encryptingList = new HashSet<>();
    }

    /**
     * Add VideoEncryptQueueMessage to list message storage
     *
     * @param videoEncryptQueueMessage indicate the message
     */
    public synchronized void addVideoEncryptMessage(VideoEncryptQueueMessage videoEncryptQueueMessage) {
        videoEncryptQueue.add(videoEncryptQueueMessage);
    }

    /**
     * Get last video encrypt message
     * @return VideoEncryptQueueMessage
     */
    public synchronized VideoEncryptQueueMessage getVideoEncryptMessage() {

        if(videoEncryptQueue.isEmpty())
            return null;

        return videoEncryptQueue.remove();
    }
    /**
     * Add Encrypting message to list message storage
     *
     * @param videoEncryptQueueMessage indicate the message
     */
    public synchronized void addEncryptingMessage(VideoEncryptQueueMessage videoEncryptQueueMessage) {

        encryptingList.add(videoEncryptQueueMessage);
    }

    /**
     * remove Encrypting message message
     * @return true/false (success/failed)
     */
    public synchronized void removeEncryptingMessage(VideoEncryptQueueMessage videoEncryptQueueMessage) {

        encryptingList.remove(videoEncryptQueueMessage);
    }

    public boolean isAcceptedThread(){

        return encryptingList.size() < MAXIMUM_THREAD;

    }

    public int numberOfThread(){

        return encryptingList.size();

    }

}
