package controller;

import com.google.common.base.Preconditions;
import models.WebHookMsg;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
	@PostMapping(path = "registerWebhook")
	@ResponseStatus(HttpStatus.OK)
	public void registerWebhook(@RequestBody WebHookMsg webHookMsg) {
		Preconditions.checkNotNull(webHookMsg);
		//TODO implement webhook dispatcher interaction
	}
}
