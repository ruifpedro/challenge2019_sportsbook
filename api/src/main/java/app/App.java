package app;

import app.config.NotificationsCtrlConfig;
import app.config.StakesCtrlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {

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
		System.out.println(stakesCtrlConfig);
		System.out.println(notificationsCtrlConfig);
	}
}
