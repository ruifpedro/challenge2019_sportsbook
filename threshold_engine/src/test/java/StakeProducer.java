import models.StakeMsg;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import serialization.StakeMsgSerializer;

import java.util.Properties;

public class StakeProducer {
	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StakeMsgSerializer.class.getName());

		KafkaProducer<String, StakeMsg> producer = new KafkaProducer<>(properties);


		ProducerRecord<String, StakeMsg> record = new ProducerRecord<>("test_in", new StakeMsg("A", 100));

		producer.send(record);

		producer.close();
	}
}
