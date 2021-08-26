package com.example.s3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.example.s3.sample.PresignedUrlSample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService 
{
    private String awsS3AudioBucket;
    private AmazonS3 amazonS3;
    private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);

    private String awsKeyId;
    private String awsKeySecret;
    private Region awsRegion;
    
    public AmazonS3ClientServiceImpl(Region awsRegion, String awsKeyId, String awsKeySecret, String awsS3AudioBucket) {
    	this.awsKeyId = awsKeyId;
    	this.awsKeySecret = awsKeySecret;
    	this.amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
        this.awsRegion = awsRegion;
    }
   /* @Autowired
    public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3AudioBucket) 
    {
		
		  this.amazonS3 = AmazonS3ClientBuilder.standard()
		  .withCredentials(awsCredentialsProvider)
		  .withRegion(awsRegion.getName()).build();
		 
    	this.amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
        this.awsS3AudioBucket = awsS3AudioBucket;
    } */

    @Async
    public String uploadFileToS3Bucket(MultipartFile multipartFile, boolean enablePublicReadAccess) 
    {
        String fileName = multipartFile.getOriginalFilename();
        String preSignedUrl = null;

        try {
            //creating the file in the server (temporarily)
            File file = new File(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            PutObjectRequest putObjectRequest = new PutObjectRequest(this.awsS3AudioBucket, fileName, file);

            if (enablePublicReadAccess) {
                putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
            }
            amazonS3.putObject(this.awsS3AudioBucket, fileName, file);
            
            //retrive objects pre singed URL file from s3
            preSignedUrl = PresignedUrlSample.getPresignedUrlToS3Object(awsS3AudioBucket, fileName, awsRegion.getName(), awsKeyId, awsKeySecret);
            
            //this.amazonS3.putObject(putObjectRequest);
            //removing the file created in the server
            file.delete();
            return preSignedUrl;
        } catch (IOException | AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
        }
        return preSignedUrl;
    }

    @Async
    public void deleteFileFromS3Bucket(String fileName) 
    {
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
        } catch (AmazonServiceException ex) {
            logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
        }
    }
}