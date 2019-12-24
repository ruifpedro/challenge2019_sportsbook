package engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import engine.config.EngineConfig;
import engine.config.YamlConfig;
import org.apache.commons.cli.*;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import serdes.StakeMsgSerde;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EngineLauncher {

	public static void main(String[] args) {
		//set cli options
		Options opt = new Options();
		opt.addRequiredOption("c", "config", true, "configuration file");

		//parser cmd line
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			 cmd = parser.parse(opt, args);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(0);
		}

		//retrieve app.config file path from cmd args
		String configFilepath = cmd.getOptionValue("c");

		//create yaml object mapper
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();

		//read yaml config
		File f = new File(configFilepath);
		YamlConfig conf = null;
		try {
			conf = mapper.readValue(f, YamlConfig.class);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}

		//kafka stream config
		Properties kafkaStreamProps = conf.getKafkaStreamConfig();
		//specific non user configurable properties
		kafkaStreamProps.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class.getName());
		kafkaStreamProps.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, StakeMsgSerde.class.getName());

		//engine config
		EngineConfig engineProps = conf.getEngineConfig();

		//start engine
		new Engine(kafkaStreamProps, engineProps);
	}
}
