package app.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.util.Collections;

@Configuration
@ConfigurationProperties(prefix = "mongo")
public class MongoConfig extends AbstractMongoConfiguration {

	private String database;

	private String host;

	private int port;

	private String username;

	private String password;


	@Override
	public MongoClient mongoClient() {
		MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
		ServerAddress svAddr = new ServerAddress(host, port);
		return new MongoClient(svAddr, Collections.singletonList(credential));
	}

	@Override
	protected String getDatabaseName() {
		return database;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
