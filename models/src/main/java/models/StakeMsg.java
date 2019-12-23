package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class StakeMsg {
	private final UUID uuid;
	private long timestamp;
	private final String account;
	private final int stake;

	@JsonCreator
	public StakeMsg(@JsonProperty("account") String account, @JsonProperty("stake") int stake) {
		this.uuid = UUID.randomUUID();
		this.timestamp = System.currentTimeMillis();
		this.account = account;
		this.stake = stake;

	}

	public UUID getUuid() {
		return uuid;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getAccount() {
		return account;
	}

	public int getStake() {
		return stake;
	}

	@Override
	public String toString() {
		return "StakeMsg{" +
				"uuid=" + uuid +
				", timestamp=" + timestamp +
				", account='" + account + '\'' +
				", stake=" + stake +
				'}';
	}
}
