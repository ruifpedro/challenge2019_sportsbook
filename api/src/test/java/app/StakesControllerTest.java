package app;

import app.config.NotificationsCtrlConfig;
import app.config.StakesCtrlConfig;
import app.mongo.StakesRepository;
import app.mongo.ThresholdsRepository;
import com.google.gson.Gson;
import models.StakeMsg;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@WebMvcTest(StakesController.class)
public class StakesControllerTest {
	private Gson gson = new Gson();

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StakesController stakesController;

	@MockBean
	private StakesRepository stakesRepository;
	@MockBean
	private ThresholdsRepository thresholdsRepository;
	@MockBean
	private StakesCtrlConfig stakesCtrlConfig;
	@MockBean
	private NotificationsCtrlConfig notificationsCtrlConfig;


	@Autowired
	protected WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = standaloneSetup(this.stakesController).build();
	}

	@Test
	public void postStake200OK() throws Exception {
		StakeMsg stakeMsg = new StakeMsg("testAC", 100);
		mockMvc.perform(
				post(URI.create("/stakes/placeStake"))
						.content(gson.toJson(stakeMsg))
						.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());
	}
}
