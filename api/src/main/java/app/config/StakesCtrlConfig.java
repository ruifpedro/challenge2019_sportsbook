package app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@ConfigurationProperties(prefix = "stakes-controller")
public class StakesCtrlConfig {
	private Properties producer;
	private String topicName;

	public Properties getProducer() {
		return producer;
	}

	public void setProducer(Properties producer) {
		this.producer = producer;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	@Override
	public String toString() {
		return "StakesCtrlConfig{" +
				"producer=" + producer +
				", topicName='" + topicName + '\'' +
				'}';
	}
}
