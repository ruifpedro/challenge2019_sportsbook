package config;

import java.util.Properties;

public class YamlConfig {
	private EngineConfig engine;
	private Properties kafkastream;

	public YamlConfig(EngineConfig engine, Properties kafkastream) {
		this.engine = engine;
		this.kafkastream = kafkastream;
	}

	public EngineConfig getEngineConfig() {
		return engine;
	}

	public Properties getKafkaStreamConfig() {
		return kafkastream;
	}
}
