package serialization;

import com.google.gson.Gson;
import models.ThresholdMsg;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ThresholdMsgJsonSerializer implements Serializer<ThresholdMsg> {
	private Gson gson = new Gson();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public byte[] serialize(String s, ThresholdMsg stakeMsg) {
		return gson.toJson(stakeMsg).getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public void close() {
	}
}
