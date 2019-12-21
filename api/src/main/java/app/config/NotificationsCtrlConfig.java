package app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "notifications-controller")
public class NotificationsCtrlConfig {
	private Properties consumer;
	private List<String> topics;
	private int timeout;

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Properties getConsumer() {
		return consumer;
	}

	public void setConsumer(Properties consumer) {
		this.consumer = consumer;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	@Override
	public String toString() {
		return "NotificationsCtrlConfig{" +
				"consumer=" + consumer +
				", topics=" + topics +
				", timeout=" + timeout +
				'}';
	}

}
