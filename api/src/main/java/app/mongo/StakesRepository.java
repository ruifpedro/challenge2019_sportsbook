package app.mongo;

import models.StakeMsg;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface StakesRepository extends MongoRepository<StakeMsg, String> {
	public List<StakeMsg> findByAccount(String account);

	public StakeMsg findByUuid(UUID uuid);
}
