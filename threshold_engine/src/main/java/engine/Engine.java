package engine;

import engine.config.EngineConfig;
import models.StakeMsg;
import models.ThresholdMsg;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import serialization.ThresholdMsgJsonDeserializer;
import serialization.ThresholdMsgJsonSerializer;
import serialization.WindowedStringDeserializer;
import serialization.WindowedStringSerializer;

import java.time.Duration;
import java.util.Properties;

public class Engine {

	private Serdes.WrapperSerde<ThresholdMsg> thresholdSerde = new Serdes.WrapperSerde<>(new ThresholdMsgJsonSerializer(), new ThresholdMsgJsonDeserializer());
	private Serdes.WrapperSerde<Windowed<String>> windowedSerde = new Serdes.WrapperSerde<>(new WindowedStringSerializer(), new WindowedStringDeserializer());
	private Serdes.StringSerde stringSerde = new Serdes.StringSerde();
	private Serdes.IntegerSerde integerSerde = new Serdes.IntegerSerde();

	public Engine(Properties kafkaStreamProps, EngineConfig engineConfig) {
		var threshold = engineConfig.getThreshold();
		var inputTopicName = engineConfig.getInput_topic();
		var outputTopicName = engineConfig.getOutput_topic();
		var windowSize = engineConfig.getWindow_size();

		KafkaStreams stream = new KafkaStreams(createTopology(threshold, inputTopicName, outputTopicName, windowSize), kafkaStreamProps);
		stream.start();
	}

	private Topology createTopology(int threshold, String inputTopicName, String outputTopicName, String windowSize) {
		var streamBuilder = new StreamsBuilder();
		KStream<String, StakeMsg> stream = streamBuilder.stream(inputTopicName);

		stream.groupBy((streamKey, stakeMsg) -> stakeMsg.getAccount())
			  .windowedBy(TimeWindows.of(Duration.parse(windowSize)))
			  .aggregate(
					  () -> 0,
					  (accountKey, stakeMsg, accumulatedStake) -> accumulatedStake + stakeMsg.getStake(),
					  Materialized.with(stringSerde, integerSerde)
			  )
			  .toStream()
			  .filter((window, accumulatedStake) -> accumulatedStake >= threshold)
			  .mapValues((window, accumulatedStake) -> new ThresholdMsg(window.key(), accumulatedStake))
			  .to(outputTopicName, Produced.with(windowedSerde, thresholdSerde));

		return streamBuilder.build();
	}
}
