package com.example.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

	private Logger LOG = LoggerFactory.getLogger(MessageController.class);
	
	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;
	
	@Value("${cloud.aws.end-point.uri}")
	private String endPoint;
	
	@PostMapping("/send")
	public void sendMesssage(@RequestBody Msg msg) {
		
		queueMessagingTemplate.convertAndSend(endPoint, msg);
		/**queueMessagingTemplate.send(endPoint, MessageBuilder.withPayload(msg)
															.setHeader("contentType", "application/json")
															.build()
															);*/
		
	}
	
	@SqsListener("First-queue") 
	public void loadMessageFromSQS(Msg msg) {
		LOG.info("Message from SQS Queue : {}",msg.getBody());
	}
	

}
