package models;

import java.util.UUID;

public class ThresholdMsg {
	private final UUID uuid;
	private long timestamp;
	private final String account;
	private final int accumulatedStake;

	public ThresholdMsg(String account, int accumulatedStake) {
		this.uuid = UUID.randomUUID();
		this.timestamp = System.currentTimeMillis();
		this.account = account;
		this.accumulatedStake = accumulatedStake;
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

	public int getAccumulatedStake() {
		return accumulatedStake;
	}
}
