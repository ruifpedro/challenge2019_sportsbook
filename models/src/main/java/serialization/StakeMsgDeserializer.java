package serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.StakeMsg;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class StakeMsgDeserializer implements Deserializer<StakeMsg> {
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
	}

	@Override
	public StakeMsg deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		StakeMsg object = null;
		try {
			object = mapper.readValue(data, StakeMsg.class);
		} catch (Exception exception) {
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}

	@Override
	public void close() {
	}
}