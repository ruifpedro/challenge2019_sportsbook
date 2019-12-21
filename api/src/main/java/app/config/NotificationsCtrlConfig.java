package app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "notifications-controller")
public class NotificationsCtrlConfig {
	private Properties consumer;
	private String topicName;

	public Properties getConsumer() {
		return consumer;
	}

	public void setConsumer(Properties consumer) {
		this.consumer = consumer;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public String toString() {
		return "NotificationsCtrlConfig{" +
				"consumer=" + consumer +
				", topicName='" + topicName + '\'' +
				'}';
	}
}
