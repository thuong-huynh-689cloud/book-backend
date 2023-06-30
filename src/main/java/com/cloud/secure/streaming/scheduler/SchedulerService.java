package com.cloud.secure.streaming.scheduler;

import com.cloud.secure.streaming.scheduler.worker.VideoEncryptWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


/**
 * @author 689Cloud
 */
@Service
@Slf4j
public class SchedulerService {

    private VideoEncryptWorker videoEncryptWorker ;

    public SchedulerService(VideoEncryptWorker videoEncryptWorker) {
        this.videoEncryptWorker = videoEncryptWorker;
    }


    @Scheduled(fixedDelay = 5000)  // 5s
    public void encryptVideoFile() { videoEncryptWorker.doWork();}
}
