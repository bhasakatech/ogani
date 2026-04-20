package com.bhasaka.ogani.core.services.Impl;
import com.bhasaka.ogani.core.services.S3UploadService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.bhasaka.ogani.core.config.S3Configuration;

@Component(service = S3UploadService.class, immediate = true)
@Designate(ocd = S3Configuration.class)
public class S3UploadServiceImpl implements S3UploadService {

    private static final Logger log =
            LoggerFactory.getLogger(S3UploadServiceImpl.class);

    private AmazonS3 s3Client;
    private String bucketName;
    private String region;
    private String baseFolder;
    private String accessKey;
    private String secretKey;

    @Activate
    @Modified
    protected void activate(S3Configuration config) {
        this.accessKey = config.accessKey();
        this.secretKey = config.secretKey();
        this.bucketName = config.bucketName();
        this.region = config.region();
        this.baseFolder = config.baseFolder();

        log.info("Initializing S3 service");
        log.info("Bucket Name: {}", bucketName);
        log.info("Region: {}", region);
        log.info("Base Folder: {}", baseFolder);

        BasicAWSCredentials credentials =
                new BasicAWSCredentials(accessKey, secretKey);

        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(
                        new AWSStaticCredentialsProvider(credentials))
                .build();
    }

@Override
public void uploadToS3(String newEntryJson) {

        String fileName = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
                + ".json";

        String s3Key = baseFolder + "/" + fileName;

        log.info("Uploading/updating S3 file: {}", s3Key);

        try {
            String existingJson = "[]";

            
            if (s3Client.doesObjectExist(bucketName, s3Key)) {

                log.info("File already exists. Reading existing data...");

                S3Object s3Object = s3Client.getObject(bucketName, s3Key);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                s3Object.getObjectContent(),
                                StandardCharsets.UTF_8));

                StringBuilder content = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }

                reader.close();

                existingJson = content.toString();
            }

            String updatedJson;

            
            if ("[]".equals(existingJson) || existingJson.trim().isEmpty()) {
                updatedJson = "[" + newEntryJson + "]";
            } else {
                
                updatedJson = existingJson.substring(0, existingJson.length() - 1)
                        + "," + newEntryJson + "]";
            }

            
            s3Client.putObject(bucketName, s3Key, updatedJson);

            log.info("Successfully updated S3 file: {}", s3Key);

        } catch (Exception e) {
            log.error("Failed to update S3 daily JSON file", e);
            throw new RuntimeException("S3 upload failed", e);
        }
    }
}