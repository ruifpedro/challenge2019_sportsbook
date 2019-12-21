package serdes;

import models.StakeMsg;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import serialization.StakeMsgDeserializer;
import serialization.StakeMsgSerializer;

import java.util.Map;

public class StakeMsgSerde implements Serde<StakeMsg> {
	private StakeMsgDeserializer deserializer = new StakeMsgDeserializer();
	private StakeMsgSerializer serializer = new StakeMsgSerializer();

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {

	}

	@Override
	public void close() {
		serializer.close();
		deserializer.close();
	}

	@Override
	public Serializer<StakeMsg> serializer() {
		return serializer;
	}

	@Override
	public Deserializer<StakeMsg> deserializer() {
		return deserializer;
	}
}
