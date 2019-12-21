package app;

import app.config.NotificationsCtrlConfig;
import app.models.WebHookMsg;
import com.google.common.base.Preconditions;
import models.ThresholdMsg;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

	private Map<String, String> hooks = new ConcurrentHashMap<>();

	private final String topicName;
	private KafkaConsumer<String, ThresholdMsg> consumer;

	@Autowired
	public NotificationsController(NotificationsCtrlConfig notificationsCtrlConfig) {
		this.topicName = notificationsCtrlConfig.getTopicName();

		var properties = notificationsCtrlConfig.getConsumer();
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

		this.consumer = new KafkaConsumer<>(properties);
		// TODO - fix topicName being a string not a list
		this.consumer.subscribe(Collections.singleton(this.topicName));

		//TODO - add to config
		var timeout = 100;

		// TODO - change into sys shutdown hook
		var running = true;

		// TODO - maybe extract into a class
		new Runnable() {

			@Override
			public void run() {
				while (running) {
					consumer.poll(timeout).forEach(record -> {
						//TODO - implement business logic
					});

				}
			}
		};
	}

	@PostMapping(path = "registerWebhook")
	@ResponseStatus(HttpStatus.OK)
	public void registerWebhook(@RequestBody WebHookMsg webHookMsg) {
		Preconditions.checkNotNull(webHookMsg);

		//TODO implement webhook dispatcher interaction

		// send it back to the user
		var hookID = UUID.randomUUID().toString();

		// register hook
		if (hooks.containsValue(webHookMsg.getUrl())){
			//hook URL already registed
			//TODO - send an error message back
		}else{
			hooks.put(hookID, webHookMsg.getUrl());
		}

	}
}
