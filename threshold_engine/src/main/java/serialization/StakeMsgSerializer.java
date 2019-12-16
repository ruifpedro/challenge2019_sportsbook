package serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.StakeMsg;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class StakeMsgSerializer implements Serializer<StakeMsg> {
	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public byte[] serialize(String s, StakeMsg stakeMsg) {
		byte[] retVal = null;

		ObjectMapper objectMapper = new ObjectMapper();

		try {

			retVal = objectMapper.writeValueAsString(stakeMsg).getBytes();

		} catch (Exception exception) {

			System.out.println("Error in serializing object" + stakeMsg);

		}

		return retVal;
	}

	@Override
	public void close() {

	}
}
