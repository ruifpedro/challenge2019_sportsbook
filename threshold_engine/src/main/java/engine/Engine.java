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

	// Serdes
	private Serdes.WrapperSerde<ThresholdMsg> thresholdSerde = new Serdes.WrapperSerde<>(new ThresholdMsgJsonSerializer(), new ThresholdMsgJsonDeserializer());
	private Serdes.WrapperSerde<Windowed<String>> windowedSerde = new Serdes.WrapperSerde<>(new WindowedStringSerializer(), new WindowedStringDeserializer());
	private Serdes.StringSerde stringSerde = new Serdes.StringSerde();
	private Serdes.IntegerSerde integerSerde = new Serdes.IntegerSerde();

	/**
	 * Creates and starts an instance of the Threshold Engine
	 *
	 * @param kafkaStreamProps
	 * @param engineConfig
	 */
	public Engine(Properties kafkaStreamProps, EngineConfig engineConfig) {
		// get topology params
		var threshold = engineConfig.getThreshold();
		var inputTopicName = engineConfig.getInput_topic();
		var outputTopicName = engineConfig.getOutput_topic();
		var windowSize = engineConfig.getWindow_size();

		// create processing topology
		var topology = createTopology(threshold, inputTopicName, outputTopicName, windowSize);
		// use the topology to create the kafka stream
		var stream = new KafkaStreams(topology, kafkaStreamProps);
		// start the stream
		stream.start();
	}

	/**
	 * Creates our thresholding topology
	 *
	 * @param threshold
	 * @param inputTopicName
	 * @param outputTopicName
	 * @param windowSize
	 * @return
	 */
	private Topology createTopology(int threshold, String inputTopicName, String outputTopicName, String windowSize) {
		var streamBuilder = new StreamsBuilder();
		KStream<String, StakeMsg> stream = streamBuilder.stream(inputTopicName);

		stream
				// group stake messages by account
				.groupBy((streamKey, stakeMsg) -> stakeMsg.getAccount())
				// create non-overlapping time window
				.windowedBy(TimeWindows.of(Duration.parse(windowSize)))
				// aggregate stake values
				.aggregate(
						() -> 0,
						(accountKey, stakeMsg, accumulatedStake) -> accumulatedStake + stakeMsg.getStake(),
						Materialized.with(stringSerde, integerSerde)
				)
				.toStream()
				// apply threshold
				.filter((window, accumulatedStake) -> accumulatedStake >= threshold)
				// use stakes that breach the threshold to create a threshold message
				.mapValues((window, accumulatedStake) -> new ThresholdMsg(window.key(), accumulatedStake))
				// output threshold messages
				.to(outputTopicName, Produced.with(windowedSerde, thresholdSerde));

		return streamBuilder.build();
	}
}
