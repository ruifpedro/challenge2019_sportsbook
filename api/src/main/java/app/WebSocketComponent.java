package app;

import models.ThresholdMsg;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

//@Component
//@EnableBinding(Sink.class)
@Service
public class WebSocketComponent {

	private final SimpMessagingTemplate template;

	public WebSocketComponent(SimpMessagingTemplate template) {
		this.template = template;
	}

	@KafkaListener(topics = "#{'${kafka.topics}'.split(',')}")
	public void handleNotification(@Payload ThresholdMsg thresholdMsg) {
		template.convertAndSend("/topic/notifications", thresholdMsg);
	}
}
