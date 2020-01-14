package app;

import app.config.NotificationsCtrlConfig;
import app.config.StakesCtrlConfig;
import app.models.WebHookMsg;
import app.mongo.StakesRepository;
import app.mongo.ThresholdsRepository;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@WebMvcTest(StakesController.class)
public class NotificationsControllerTest {
	private Gson gson = new Gson();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private NotificationsController notificationsController;

	@MockBean
	private StakesRepository stakesRepository;
	@MockBean
	private ThresholdsRepository thresholdsRepository;
	@MockBean
	private StakesCtrlConfig stakesCtrlConfig;
	@MockBean
	private NotificationsCtrlConfig notificationsCtrlConfig;
	@MockBean
	private StakesController stakesController;

	@Autowired
	protected WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = standaloneSetup(this.notificationsController).build();
	}

	@Test
	public void registerWebHook200OK() throws Exception {
		WebHookMsg webHookMsg = new WebHookMsg("http://mock.test.webhooks/1");
		mockMvc.perform(
				post(URI.create("/notifications/registerWebhook"))
						.content(gson.toJson(webHookMsg))
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}

	@Test
	public void deleteWebHook200OK() throws  Exception {
		String hookID = UUID.randomUUID().toString();
		mockMvc.perform(
				put("/notifications/deleteWebHook/{hookID}", hookID)
		).andExpect(status().isOk());
	}
}
