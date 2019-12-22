package serialization;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.kstream.Windowed;

import java.nio.charset.StandardCharsets;

public class WindowedStringSerializer implements Serializer<Windowed<String>> {
	private Gson gson = new Gson();

	@Override
	public byte[] serialize(String s, Windowed<String> stringWindowed) {
		return gson.toJson(stringWindowed).getBytes(StandardCharsets.UTF_8);
	}
}
