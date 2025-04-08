package com.sivagtr.springbootproducer.producer.kafkamsg;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.sivagtr.springbootproducer.producer.config.PropertiesConfig;
import com.sivagtr.springbootproducer.producer.model.InformationModel;

import lombok.extern.slf4j.Slf4j;

/**
 * Producer using Apache configuration for sending messages
 */
@Component
@Slf4j
public class ProducerApacheKafkaMsgSender {

	KafkaProducer<String,String> msgKafkaProducer;

	@Autowired
	PropertiesConfig config;

	public void sendMsg(String msg) {
		log.info("Msg before Kafka sending " + msg);
		String topic = config.getMsgTopic();
		log.info("Topic : " + topic);

		ProducerRecord<String,String> msgProducer = new ProducerRecord<>(topic,topic,msg);
		msgKafkaProducer.send(msgProducer, (metadata, exception) -> {
			if (exception == null) {
				log.info("Message sent successfully to topic {} partition {} at offset {}", 
					metadata.topic(), metadata.partition(), metadata.offset());
			} else {
				log.error("Error sending message to Kafka", exception);
			}
		});
	}

	public void initializeKafkaPrducer(){
		if(msgKafkaProducer == null) {
			Properties properties = getProperties();
			msgKafkaProducer = new KafkaProducer<String, String>(properties);
		}
	}

	private Properties getProperties(){
		Properties properties = new Properties();
		properties.put("bootstrap.servers", config.getKafkaServer());
		properties.put("key.serializer", config.getKey());
		properties.put("value.serializer", config.getMsgValue());
		
		// Critical settings for reliability in a broker cluster:
		properties.put("acks", "all");                // Wait for all in-sync replicas to acknowledge
		properties.put("retries", "3");               // Retry failed sends
		properties.put("linger.ms", "1");             // Small delay to batch messages
		
		// Additional properties for better producer reliability:
		properties.put("enable.idempotence", "true"); // Prevent duplicate messages
		properties.put("max.in.flight.requests.per.connection", "5"); // Limit parallel requests
		properties.put("delivery.timeout.ms", "120000"); // Total time to attempt delivery
		properties.put("request.timeout.ms", "30000");   // Time to wait for ack from broker
		
		return properties;
	}

	public void sendJson(InformationModel informationModel){
		initializeKafkaPrducer();
		log.info("Msg before Kafka sending " + informationModel.toString());
		String topic = config.getMsgTopic();
		log.info("Topic : " + topic);

		ProducerRecord<String,String> msgProducer = new ProducerRecord<>(topic,topic,informationModel.toString());
		msgKafkaProducer.send(msgProducer, (metadata, exception) -> {
			if (exception == null) {
				log.info("JSON Message sent successfully to topic {} partition {} at offset {}", 
					metadata.topic(), metadata.partition(), metadata.offset());
			} else {
				log.error("Error sending JSON message to Kafka", exception);
			}
		});
	}
}
