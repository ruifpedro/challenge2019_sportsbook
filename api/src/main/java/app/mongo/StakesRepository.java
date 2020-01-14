package app.mongo;

import models.StakeMsg;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StakesRepository extends MongoRepository<StakeMsg, String> {
	public List<StakeMsg> findByAccount(String account);

	public StakeMsg findByUuid(UUID uuid);
}
