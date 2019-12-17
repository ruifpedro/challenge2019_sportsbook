package config;

public class EngineConfig {
	private int threshold;
	private String input_topic;
	private String output_topic;
	private String window_size;

	public EngineConfig(int threshold, String input_topic, String output_topic, String window_size) {
		this.threshold = threshold;
		this.input_topic = input_topic;
		this.output_topic = output_topic;
		this.window_size = window_size;
	}

	public int getThreshold() {
		return threshold;
	}

	public String getInput_topic() {
		return input_topic;
	}

	public String getOutput_topic() {
		return output_topic;
	}

	public String getWindow_size() {
		return window_size;
	}
}
