package app;

import app.config.StakesCtrlConfig;
import com.google.common.base.Preconditions;
import models.StakeMsg;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import serialization.StakeMsgSerializer;

import java.util.Properties;

@RestController
@RequestMapping("/stakes")
public class StakesController {

	private KafkaProducer<String, StakeMsg> producer;
	private String topicName;

	@Autowired
	public StakesController(StakesCtrlConfig stakesCtrlConfig) {
		this.topicName = stakesCtrlConfig.getTopicName();

		Properties producerProps = stakesCtrlConfig.getProducer();
		producerProps.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		producerProps.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StakeMsgSerializer.class.getName());

		this.producer = new KafkaProducer<>(producerProps);
	}

	@PostMapping(path = "placeStake", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void placeStake(@RequestBody StakeMsg stakeMsg) {
		Preconditions.checkNotNull(stakeMsg);
		//TODO - send to kafka
		ProducerRecord<String, StakeMsg> record = new ProducerRecord<>(this.topicName, stakeMsg);
		producer.send(record);
		producer.flush();
	}
}
