package engine;

import config.EngineConfig;
import models.StakeMsg;
import models.ThresholdMsg;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.time.Duration;
import java.util.Properties;

public class Engine {

	public Engine(Properties kafkaStreamProps, EngineConfig engineConfig) {

		var threshold = engineConfig.getThreshold();
		var inputTopicName = engineConfig.getInput_topic();
		var outputTopicName = engineConfig.getOutput_topic();
		var windowSize = engineConfig.getWindow_size();

		KafkaStreams streams = new KafkaStreams(createTopology(threshold, inputTopicName, outputTopicName, windowSize), kafkaStreamProps);
		streams.start();
	}


	private Topology createTopology(int threshold, String inputTopicName, String outputTopicName, String windowSize) {
		var streamBuilder = new StreamsBuilder();

		KStream<String, StakeMsg> stream = streamBuilder.stream(inputTopicName);

		stream.groupBy((streamKey, stakeMsg) -> stakeMsg.getAccount())
			  .windowedBy(TimeWindows.of(Duration.parse(windowSize)))
			  .aggregate(
					  () -> 0,
					  (accountKey, stakeMsg, accumulatedStake) -> accumulatedStake + stakeMsg.getStake(),
					  Materialized.with(new Serdes.StringSerde(), new Serdes.IntegerSerde())
			  )
			  .filter((accountKey, accumulatedStake) -> accumulatedStake >= threshold)
			  .mapValues((accountKey, accumulatedStake) ->
					  new ThresholdMsg(accountKey.key(), accumulatedStake))
			  .toStream()
			  //TODO - serialize outgoing POJO into JSON
			  .to(outputTopicName);

		return streamBuilder.build();
	}
}
