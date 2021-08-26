package com.example.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class AmazonS3Config {
	@Value("${aws.access.key.id}")
	private String awsKeyId;

	@Value("${aws.access.key.secret}")
	private String awsKeySecret;

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.s3.audio.bucket}")
	private String awsS3AudioBucket;

	@Bean(name = "awsKeyId")
	public String getAWSKeyId() {
		System.out.println("awsKeyId : " + awsKeyId);
		return awsKeyId;
	}

	@Bean(name = "awsKeySecret")
	public String getAWSKeySecret() {
		System.out.println("awsKeySecret : " + awsKeySecret);
		return awsKeySecret;
	}

	@Bean(name = "awsRegion")
	public Region getAWSPollyRegion() {
		return Region.getRegion(Regions.fromName(awsRegion));
	}

	@Bean(name = "awsCredentialsProvider")
	public AWSCredentialsProvider getAWSCredentials() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsKeyId, this.awsKeySecret);
		return new AWSStaticCredentialsProvider(awsCredentials);
	}

	@Bean(name = "awsS3AudioBucket")
	public String getAWSS3AudioBucket() {
		return awsS3AudioBucket;
	}

	@Bean
	public QueueMessagingTemplate queueMessagingTemplate() {
		return new QueueMessagingTemplate(amazonSqsAsync());
	}

	@Primary
	@Bean
	public AmazonSQSAsync amazonSqsAsync() {
		return AmazonSQSAsyncClientBuilder.standard().withRegion(this.awsRegion)
				.withCredentials(this.getAWSCredentials()).build();
	}

	
}