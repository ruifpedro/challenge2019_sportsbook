package serialization;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.streams.kstream.Windowed;

public class WindowedStringDeserializer implements Deserializer<Windowed<String>> {
	private Gson gson = new Gson();

	@Override
	public Windowed<String> deserialize(String s, byte[] data) {
		return gson.fromJson(new String(data), Windowed.class);
	}
}
