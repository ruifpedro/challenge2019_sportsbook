package serialization;

import com.google.gson.Gson;
import models.ThresholdMsg;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ThresholdMsgJsonDeserializer implements Deserializer<ThresholdMsg> {
	private Gson gson = new Gson();
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public ThresholdMsg deserialize(String topic, byte[] data) {
		return gson.fromJson(new String(data), ThresholdMsg.class);
	}

	@Override
	public void close() {
	}
}