package com.pl.jellytech.javeloper.spring.kafka.producer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducerService {
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

	private final KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String topic, String msg) {
		this.kafkaTemplate.send(topic, msg);
	}

	public void sendMessageAndWaitForResult(String topic, String message) {
		ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(topic, message);

		future.addCallback(new ListenableFutureCallback<>() {
			@Override
			public void onSuccess(SendResult<String, String> result) {
				logger.info("Sent message=[{}}] with offset=[{}}]", message, result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				logger.error("Sending message [{}] failed. Error: {}", message, ex.getMessage());;
			}
		});
	}
}
