package app;

import app.config.NotificationsCtrlConfig;
import app.config.StakesCtrlConfig;
import app.mongo.StakesRepository;
import app.mongo.ThresholdsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {

	@Autowired
	private StakesRepository stakesRepository;

	@Autowired
	private ThresholdsRepository thresholdsRepository;

	@Autowired
	private StakesCtrlConfig stakesCtrlConfig;

	@Autowired
	private NotificationsCtrlConfig notificationsCtrlConfig;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(App.class);
		app.run();
	}

	@Override
	public void run(String... args) {
	}
}
