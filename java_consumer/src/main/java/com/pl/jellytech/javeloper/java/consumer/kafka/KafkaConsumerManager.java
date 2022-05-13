package com.pl.jellytech.javeloper.java.consumer.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KafkaConsumerManager {
	private final static Logger logger = LogManager.getLogger(KafkaConsumerManager.class);

	private final static String BOOTSTRAP_SERVERS = "localhost:9092";

	private static Map<String, Object> getProperties(){
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

		return props;
	}

	public static KafkaConsumer<String, String> createConsumer(String groupId){
		logger.debug("Creating new consumer");
		Map<String, Object> props = getProperties();
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

		return new KafkaConsumer<>(props);
	}

	public static void continuouslyPollMessages(KafkaConsumer<String, String> consumer, List<String> topics){
		consumer.subscribe(topics);
		while(true){
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
			records.forEach(record -> {
				logger.debug("Record retrieved: Key: {}, value: {}, partition: {}, offset: {})",
						record.key(), record.value(),
						record.partition(), record.offset());
				logger.info("Record retrieved: Key: {}, value: {}", record.key(), record.value());
			});
		}
	}
}
