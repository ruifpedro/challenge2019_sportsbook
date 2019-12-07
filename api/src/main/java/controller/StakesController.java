package controller;

import com.google.common.base.Preconditions;
import models.StakeMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stakes")
public class StakesController {
	@PostMapping(path = "placeStake", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public void placeStake(@RequestBody StakeMsg stakeMsg){
		Preconditions.checkNotNull(stakeMsg);
		//TODO - send to kafka
	}
}
