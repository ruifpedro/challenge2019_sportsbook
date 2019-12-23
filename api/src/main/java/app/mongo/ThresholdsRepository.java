package app.mongo;

import models.ThresholdMsg;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ThresholdsRepository extends MongoRepository<ThresholdMsg, String> {
	public List<ThresholdMsg> findByAccount(String account);

	public ThresholdMsg findByUuid(UUID uuid);
}
