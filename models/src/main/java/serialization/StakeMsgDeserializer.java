package serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.StakeMsg;
import org.apache.kafka.common.serialization.Deserializer;

public class StakeMsgDeserializer implements Deserializer<StakeMsg> {

	@Override
	public StakeMsg deserialize(String topic, byte[] data) {
		ObjectMapper mapper = new ObjectMapper();
		StakeMsg object = null;
		
		try {
			object = mapper.readValue(data, StakeMsg.class);
		} catch (Exception exception) {
			//TODO - logging
			System.out.println("Error in deserializing bytes " + exception);
		}
		return object;
	}
}