package engine;

import engine.config.EngineConfig;
import models.StakeMsg;
import models.ThresholdMsg;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import serialization.ThresholdMsgJsonDeserializer;
import serialization.ThresholdMsgJsonSerializer;

import java.time.Duration;
import java.util.Properties;

public class Engine {

	private Serdes.WrapperSerde<ThresholdMsg> thresholdSerde = new Serdes.WrapperSerde<>(new ThresholdMsgJsonSerializer(), new ThresholdMsgJsonDeserializer());

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

		// TODO - find and fix the bug that causes null messages to be sent
		stream.groupBy((streamKey, stakeMsg) -> stakeMsg.getAccount())
			  .windowedBy(TimeWindows.of(Duration.parse(windowSize)))
			  .aggregate(
					  () -> 0,
					  (accountKey, stakeMsg, accumulatedStake) -> accumulatedStake + stakeMsg.getStake(),
					  Materialized.with(new Serdes.StringSerde(), new Serdes.IntegerSerde())
			  )
			  .filter((accountKey, accumulatedStake) -> accumulatedStake >= threshold)
			  .mapValues((Windowed<String> accountKey, Integer accumulatedStake) ->
							  new ThresholdMsg(accountKey.key(), accumulatedStake),
					  //TODO - find a way to properly deal with a Serde for Windowed<String>
					  Materialized.with(new Serde<>() {
						  @Override
						  public Serializer<Windowed<String>> serializer() {
							  return (s, stringWindowed) -> stringWindowed.key().getBytes();
						  }

						  @Override
						  public Deserializer<Windowed<String>> deserializer() {
							  return (s, bytes) -> new Windowed<>(new String(bytes), new Window(0, 0) {
								  @Override
								  public boolean overlap(Window window) {
									  return false;
								  }
							  });
						  }
					  }, thresholdSerde)
			  )
			  .toStream()
			  .to(outputTopicName);

		return streamBuilder.build();
	}
}
