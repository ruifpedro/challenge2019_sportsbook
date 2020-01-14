package app.mongo;

import models.ThresholdMsg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThresholdsRepository extends MongoRepository<ThresholdMsg, String> {
	public List<ThresholdMsg> findByAccount(String account);

	public ThresholdMsg findByUuid(UUID uuid);
}
