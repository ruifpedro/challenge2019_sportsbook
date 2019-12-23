package app;

import app.config.NotificationsCtrlConfig;
import app.models.WebHookMsg;
import app.mongo.ThresholdsRepository;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import models.ThresholdMsg;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serialization.ThresholdMsgJsonDeserializer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {

	private Gson gson = new Gson();
	private final HttpClient httpClient = HttpClient.newBuilder()
													.version(HttpClient.Version.HTTP_2)
													.build();

	private Map<String, String> hooks = new ConcurrentHashMap<>();

	private final List<String> topics;
	private KafkaConsumer<String, ThresholdMsg> consumer;

	private Runnable webHookHandler;
	private AtomicBoolean running = new AtomicBoolean(true);
	private int timeout;

	private ThresholdsRepository thresholdsRepository;

	@Autowired
	public NotificationsController(NotificationsCtrlConfig notificationsCtrlConfig, ThresholdsRepository thresholdsRepository) {
		this.topics = notificationsCtrlConfig.getTopics();
		this.timeout = notificationsCtrlConfig.getTimeout();
		this.thresholdsRepository = thresholdsRepository;

		var properties = notificationsCtrlConfig.getConsumer();
		properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ThresholdMsgJsonDeserializer.class.getName());

		// create kafka consumer
		this.consumer = new KafkaConsumer<>(properties);
		// subscribe to kafka topics
		this.consumer.subscribe(this.topics);

		// define webhook handler logic
		webHookHandler = () -> {
			while (running.get()) {
				consumer.poll(timeout).forEach(record -> {
					// save threshold msg do mongo db
					thresholdsRepository.save(record.value());

					// send threshold msg to all registered hooks
					hooks.values().forEach(url -> {
						HttpRequest request = HttpRequest.newBuilder()
														 .POST(recordToJson(record))
														 .uri(URI.create(url))
														 .setHeader("User-Agent", "SportsBook WH Bot")
														 .header("Content-Type", "application/json")
														 .build();
						try {
							HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
							//TODO - change into logging
							System.out.println(response.statusCode());
						} catch (IOException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					});
				});
			}
		};

		// start the webhook handler thread
		new Thread(webHookHandler).start();
	}

	private HttpRequest.BodyPublisher recordToJson(ConsumerRecord<String, ThresholdMsg> record) {
		var msg = gson.toJson(record.value());
		return HttpRequest.BodyPublishers.ofString(msg);
	}

	@PostMapping(path = "registerWebhook")
	public ResponseEntity<String> registerWebhook(@RequestBody WebHookMsg webHookMsg) {
		Preconditions.checkNotNull(webHookMsg);

		// send it back to the user
		var hookID = UUID.randomUUID().toString();

		// register hook
		if (hooks.containsValue(webHookMsg.getUrl())) {
			//hook URL already registered
			return new ResponseEntity<>("URL already registered", HttpStatus.NOT_ACCEPTABLE);
		} else {
			hooks.put(hookID, webHookMsg.getUrl());
			return new ResponseEntity<>(hookID, HttpStatus.OK);
		}
	}

	@PutMapping(path = "deleteWebHook/{hookID}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteWebHook(@PathVariable String hookID) {
		Preconditions.checkNotNull(hookID);
		//delete hook
		hooks.remove(hookID);
	}
}
