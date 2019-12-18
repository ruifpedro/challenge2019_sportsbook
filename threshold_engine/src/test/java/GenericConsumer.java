import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

public class GenericConsumer {

	public static void main(String[] args) {
		var properties = new Properties();
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

//		consumer.subscribe(Collections.singleton("test_in"));
//
//
//		while (true){
//			ConsumerRecords<String, StakeMsg> records = consumer.poll(100);
//			var count = records.count();
//			if (count > 0)
//				System.out.println(records.count());
//		}
		consumer.subscribe(Collections.singleton("test_out"));


		while (true){
			ConsumerRecords<String, String> records = consumer.poll(100);
			records.iterator().forEachRemaining(s -> System.out.println(s.value()));
		}
	}
}
