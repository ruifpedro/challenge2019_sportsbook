package engine;

import models.StakeMsg;
import models.ThresholdMsg;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import serdes.StakeMsgSerde;

import java.time.Duration;
import java.util.Properties;

public class Engine {


	public Engine() {
		//TODO - move into external config
		Properties properties = new Properties();
		properties.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
		properties.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, "test-kafka-streams");
		properties.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		properties.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StakeMsgSerde.class.getName());

		KafkaStreams streams = new KafkaStreams(createTopology(), properties);
		streams.start();
	}


	private Topology createTopology() {
		var streamBuilder = new StreamsBuilder();

		//TODO - move into config
		var threshold = 100;
		var topicName = "test_in";

		KStream<String, StakeMsg> stream = streamBuilder.stream(topicName);

		stream.groupBy((streamKey, stakeMsg) -> stakeMsg.getAccount())
			  //TODO - move into config
			  .windowedBy(TimeWindows.of(Duration.ofSeconds(5)))
			  .aggregate(
					  () -> 0,
					  (accountKey, stakeMsg, accumulatedStake) -> accumulatedStake + stakeMsg.getStake(),
					  Materialized.with(new Serdes.StringSerde(), new Serdes.IntegerSerde())
			  )
			  .filter((accountKey, accumulatedStake) -> accumulatedStake >= threshold)
			  .mapValues((accountKey, accumulatedStake) ->
					  new ThresholdMsg(accountKey.key(), accumulatedStake))
			  .toStream()
			  //TODO - move into config
			  .to("test_out");


		return streamBuilder.build();
	}
}
