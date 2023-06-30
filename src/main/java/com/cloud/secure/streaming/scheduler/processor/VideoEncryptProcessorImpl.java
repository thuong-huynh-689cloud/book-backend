package com.cloud.secure.streaming.scheduler.processor;

import com.cloud.secure.streaming.amazon.s3.AmazonS3Util;
import com.cloud.secure.streaming.common.enums.AppStatus;
import com.cloud.secure.streaming.common.utilities.ApplicationConfigureValues;
import com.cloud.secure.streaming.controllers.helper.UploadHelper;
import com.cloud.secure.streaming.entities.MediaInfo;
import com.cloud.secure.streaming.scheduler.queue.message.VideoEncryptQueueMessage;
import com.cloud.secure.streaming.services.MediaInfoService;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author 689Cloud
 */
@Component
@Slf4j
public class VideoEncryptProcessorImpl extends VideoEncryptProcessor {

    private ApplicationConfigureValues appConfig;
    private MediaInfoService mediaInfoService;
    private AmazonS3Util amazonS3Util;
    private UploadHelper uploadHelper;

    public VideoEncryptProcessorImpl(ApplicationConfigureValues appConfig, MediaInfoService mediaInfoService, AmazonS3Util amazonS3Util, UploadHelper uploadHelper) {
        this.appConfig = appConfig;
        this.mediaInfoService = mediaInfoService;
        this.amazonS3Util = amazonS3Util;
        this.uploadHelper = uploadHelper;
    }

    @Override
    @Async
    public void doProcess(VideoEncryptQueueMessage message) {
        MediaInfo mediaInfo = mediaInfoService.getById(message.getFileId());
        try {
            String keyName = appConfig.uploadPath + "/" + appConfig.encrypted + appConfig.pathVideoInput + "/" + message.getFileName();
            this.amazonS3Util.downLoadToLocaServer(message.getFilePath() + "/", keyName, message.getFileName());
            message.setFilePath(message.getFilePath() + message.getFileName());
            FFprobe ffprobe;
            FFmpegProbeResult probeResult;
            ffprobe = new FFprobe(appConfig.ffprobePath);
            probeResult = ffprobe.probe(message.getFilePath());

            long size = Files.size(Paths.get(message.getFilePath()));
            uploadHelper.checkPromotionVideoFileSize(size);
            mediaInfo.setFileSize(size);

            double duration = probeResult.getStreams().get(0).duration;
            mediaInfo.setDuration((long) duration);

            //Init thread
            EncryptionThread encryptionThread = new EncryptionThread(message, mediaInfoService,
                    appConfig.encryptVideoPath, appConfig.streamingApiPrefix, appConfig.ffmpegPath, appConfig.encodedKey);
            Thread thread = new Thread(encryptionThread);
            thread.start();
            thread.join();
            boolean isSuccess = amazonS3Util.uploadFolder(appConfig.encryptVideoPath + message.getFileId(), appConfig.encrypted + message.getFileId(), false);
            if (isSuccess) {
                mediaInfo.setStatus(AppStatus.ACTIVE);
            } else {
                mediaInfo.setStatus(AppStatus.ERROR);
            }
            mediaInfoService.save(mediaInfo);
            String directoryPath = appConfig.encryptVideoPath + message.getFileId();
            FileUtils.deleteDirectory(new File(directoryPath));
            new File(message.getFilePath()).delete();

        } catch (Exception e) {
            mediaInfo.setStatus(AppStatus.ERROR);
            mediaInfoService.save(mediaInfo);
            File file = new File(message.getFilePath());
            if (file.exists()) {
                file.delete();
            }
            log.error(e.getMessage());
        }
    }
}
