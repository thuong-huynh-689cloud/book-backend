package com.cloud.secure.streaming.scheduler.processor;

import com.cloud.secure.streaming.entities.MediaInfo;
import com.cloud.secure.streaming.services.MediaInfoService;
import com.cloud.secure.streaming.scheduler.queue.VideoEncryptQueueManager;
import com.cloud.secure.streaming.scheduler.queue.message.VideoEncryptQueueMessage;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.io.*;
import java.nio.file.*;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author 689Cloud
 */
public class EncryptionThread implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private VideoEncryptQueueMessage message; // Video encrypt message

    private String streamingPath; // Path to streaming folder
    private String ffmpegPath; // Path to ffmpeg execute file

    private String streamingApiPrefix;// Prefix of API streaming (without file Id)

    private MediaInfoService mediaInfoService;

    private MediaInfo mediaInfo; // File streaming will be processed in this thread

    private KeyGenerator keyGenerator; // Generator AES 128

    private static final String KEY_INFO_NAME = "enc.keyinfo";
    private static final String KEY_NAME_FORMAT = "segment%05d.key";
    private static final String SEGMENT_NAME_FORMAT = "segment%05d.ts";
    private static final String CHUNK_TIME = "10"; //by second

    private final boolean[] isSuccess = {true}; // for determine result of watch service

    private static int segment = 0;
    private static SecretKey originalKey;
    private static IvParameterSpec iv; // Iv for AES 128

    public EncryptionThread(VideoEncryptQueueMessage message, MediaInfoService mediaInfoService, String streamingPath,
                            String streamingApiPrefix, String ffmpegPath, String encodedKey ) {
        this.message = message;
        this.mediaInfoService = mediaInfoService;
        this.streamingPath = streamingPath;
        this.ffmpegPath = ffmpegPath;
        this.streamingApiPrefix = streamingApiPrefix;

        try {
            this.keyGenerator = KeyGenerator.getInstance("AES");
            this.keyGenerator.init(128);
            // decode the base64 encoded string
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
            // rebuild key using SecretKeySpec
            originalKey = new SecretKeySpec(decodedKey, "AES");
            iv = new IvParameterSpec(decodedKey);
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.error("Error when init key generator: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        doProcess();
    }

    private void doProcess() {

        // Add encrypting message to queue
        VideoEncryptQueueManager.getInstance().addEncryptingMessage(this.message);

        try {
            this.mediaInfo = mediaInfoService.getById(message.getFileId());
            processEncryptMessage();

            if (isSuccess[0]) {
                handleWhenSuccess();
            } else {
                handleWhenError();
            }
        } catch (Exception ex) {
            LOGGER.error("Error when process Encrypt Video: " + ex.getMessage());
            handleWhenError();
        }
        //Remove encrypting message from queue
        VideoEncryptQueueManager.getInstance().removeEncryptingMessage(message);
    }

    private void handleWhenError() {

        LOGGER.error("Error " + message.getFileName());

        String directoryPath = streamingPath + this.message.getFileId();
//        this.mediaInfo.setStatus(AppStatus.ERROR);
//        mediaInfoService.save(this.mediaInfo);

        try {
            FileUtils.deleteDirectory(new File(directoryPath));
        } catch (IOException e) {
            LOGGER.error("Could not delete folder " + directoryPath + " - " + e.getMessage());
        }
    }

    private void init() {

    }

    private void handleWhenSuccess() {

        LOGGER.info("Success " + message.getFileName());
//
//        this.mediaInfo.setStatus(AppStatus.ACTIVE);
//        mediaInfoService.save(this.mediaInfo);
    }

    private void processEncryptMessage() throws IOException {

        FFmpeg ffmpeg = new FFmpeg(this.ffmpegPath);

        //Create directory for store streaming file
        boolean isCreatedDirectory = createDirectory(streamingPath, message.getFileId());

        if (isCreatedDirectory) {

            // Path to streaming folder of this file
            final String fileStreamingPath = streamingPath + message.getFileId() + "/";
//            // Define key api prefix
//            final String keyApiPrefix = streamingApiPrefix + message.getFileId() + "/";
//            // Define segment api prefix
//            final String playListApiPrefix = streamingApiPrefix + message.getFileId() + "/";

            final String keyApiPrefix = "key://";
            final String playListApiPrefix = "file://";
            String firstKeyName = String.format(KEY_NAME_FORMAT, 0);

            // uploaded file path
            String inputFilePath = message.getFilePath();
            // Output m3u8 metadata file path
            String outputMetadataName = fileStreamingPath + "index.m3u8";
            // HLS key info file path
            String hlsKeyInfoPath = fileStreamingPath + KEY_INFO_NAME;

            //init first key file
            generateKeyInfo(fileStreamingPath, KEY_INFO_NAME, firstKeyName, keyApiPrefix);

            //Init ffmpeg command
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(inputFilePath)
                    .overrideOutputFiles(true)
                    .addOutput(outputMetadataName)
                    .addExtraArgs("-frames:v", "1", fileStreamingPath + "thumbnail.png")
                    .addExtraArgs("-hls_playlist_type"  , "vod")
                    .addExtraArgs("-hls_list_size", "0")
                    .addExtraArgs("-hls_segment_filename", fileStreamingPath + SEGMENT_NAME_FORMAT)
                    .addExtraArgs("-hls_flags", "periodic_rekey+split_by_time+round_durations")
                    .addExtraArgs("-hls_key_info_file", hlsKeyInfoPath)
                    .addExtraArgs("-hls_base_url", playListApiPrefix)

                    .addExtraArgs("-hls_time", CHUNK_TIME).done();

            //Init executor
            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);

            //Create Job base on Executor (ffmpeg)
            FFmpegJob encryptJob = executor.createJob(builder);

            //Register watchThread to detect and change key file for "hls periodic_rekey"
            Thread watchThread = new Thread(new Runnable() {

                public void run() {

                    try {

                        Path path = Paths.get(fileStreamingPath);
                        WatchService watcher = path.getFileSystem().newWatchService();

                        //Watch create event in folder
                        path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

                        //Segment counter (start from 1 cause we have init first key above)
                        segment = 1;

                        // listen to events (for 10 second attempt)
                        WatchKey watchKey = watcher.poll(10, TimeUnit.SECONDS);

                        while (!Thread.currentThread().isInterrupted()) {

                            for (WatchEvent<?> event : watchKey.pollEvents()) {
                                //check if the event refers to a new file created
                                if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {

                                    //When .ts file have created, change key info file
                                    if (event.context().toString().contains(".ts")) {

                                        String keyName = String.format(KEY_NAME_FORMAT, segment++);
                                        LOGGER.info("Generate segment file: " + keyName);
                                        generateKeyInfo(fileStreamingPath, KEY_INFO_NAME, keyName, keyApiPrefix);
                                    }
                                }
                            }
                        }

                        watcher.close();
                    } catch (Exception ex) {
                        LOGGER.error("Error in thread: " + ex.getCause() + " - " + ex.getMessage());
                        isSuccess[0] = false;
                    }
                }

            });

            //Start watch service in file streaming folder
            watchThread.start();

            //Run job
            encryptJob.run();

            //Interrupt watch service  after job running have done
            watchThread.interrupt();

            if (encryptJob.getState() == FFmpegJob.State.FINISHED) {
                //Interrupt watch service  after job running have done
                watchThread.interrupt();

                try {
                    encryptSegmentKey(fileStreamingPath, segment);
                    LOGGER.info("End of process: " + message.getMessageId());
                } catch (Exception e) {
                    LOGGER.info("Fail of process: " + e.getMessage());
                }
            }
            if (encryptJob.getState() == FFmpegJob.State.FAILED) {
                LOGGER.info("Fail of process: " + message.getMessageId());
            }

            LOGGER.info("End of process: " + message.getMessageId());
        }
    }


    /**
     * create folder
     *
     * @param path       is the path to folder
     * @param folderName is the folder name
     * @return created or not (true/false)
     */
    private boolean createDirectory(String path, String folderName) {

        return new File(path + "/" + folderName).mkdirs();
    }

    /**
     * @param path             is the path to encrypt folder
     * @param keyInfoName      is the name of key info
     * @param keyName          is the name of key inside key info
     * @param keyApiPathPrefix is the api path prefix of key
     * @return void
     */
    private void generateKeyInfo(String path, String keyInfoName, String keyName, String keyApiPathPrefix)
            throws IOException {

        OutputStream out = new FileOutputStream(path + "/" + keyName);
        byte[] bytes = keyGenerator.generateKey().getEncoded();
        out.write(bytes);
        out.close();

        PrintWriter keyInfoWriter = new PrintWriter(path + "/" + keyInfoName);
        keyInfoWriter.println(keyApiPathPrefix + keyName);
        keyInfoWriter.println(path + "/" + keyName);
        keyInfoWriter.close();

    }

    public static void encryptSegmentKey(String path, int segment) throws Exception {
        for (int i = 0; i < segment; i++) {
            System.out.println(path + String.format(KEY_NAME_FORMAT, i));
            File file = new File(path + String.format(KEY_NAME_FORMAT, i));
            if (!file.exists()) {
                continue;
            }

            byte[] byteArray = FileUtils.readFileToByteArray(file);
//            System.out.println(byteArray.length);
//            byte[] encodeBase64 = org.apache.commons.codec.binary.Base64.encodeBase64(byteArray);
//            System.out.println("not encrypt: " + new String(encodeBase64, StandardCharsets.US_ASCII));

            byte[] encryptBytes = encryptData("AES/CBC/PKCS5Padding", originalKey, iv, byteArray);
//            byte[] encrypt = org.apache.commons.codec.binary.Base64.encodeBase64(encryptBytes);
//            System.out.println("encrypt: " + new String(encrypt, StandardCharsets.US_ASCII));
//
//            byte[] plainText = decryptData("AES/CBC/PKCS5Padding", originalKey, iv, encryptBytes);
//            byte[] decoded = org.apache.commons.codec.binary.Base64.encodeBase64(plainText);
//            System.out.println("decrypt: " + new String(decoded, StandardCharsets.US_ASCII));

            OutputStream out = new FileOutputStream(file);
            out.write(encryptBytes);
            out.close();
        }

    }

    public static byte[] encryptData(String algorithm, SecretKey key, IvParameterSpec iv, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }

    public static byte[] decryptData(String algorithm, SecretKey key, IvParameterSpec iv, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(data);
    }


}