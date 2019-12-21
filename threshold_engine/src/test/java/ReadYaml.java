import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import engine.config.YamlConfig;

import java.io.File;
import java.io.IOException;

public class ReadYaml {


	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		mapper.findAndRegisterModules();
		String path = "threshold_engine/src/main/resources/app.config.yaml";
		File f = new File(path);
		YamlConfig conf = null;

		try {
			conf = mapper.readValue(f, YamlConfig.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(conf);
	}
}
