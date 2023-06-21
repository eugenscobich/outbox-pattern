package com.eugenescobich.outbox.pattern;

import com.eugenescobich.outbox.pattern.initializer.TestApplicationContextInitializer;
import com.eugenescobich.outbox.pattern.service.TaskService;
import com.eugenescobich.outbox.pattern.utils.RestUtils;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@SpringBootTest(classes = OutboxPatternServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@ActiveProfiles("test")
class OutboxPatternServiceApplicationTests {

	public static final String SERVER_URL_TEMPLATE = "http://localhost:%s";

	@LocalServerPort
	private int localServerPort;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private RestUtils restUtils;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@Test
	void startWorkers() {

		Instant start = Instant.now();
		taskService.insertTasks();
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		log.debug("Insertion time: {}", duration.toString());

		restUtils.setPort(localServerPort);
		restUtils.doPost("test", String.class, "/start-workers");
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}



	@Test
	void startWorkers2() {

		Instant start = Instant.now();
		taskService.insertTasks();
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		log.debug("Insertion time: {}", duration.toString());

		restUtils.setPort(localServerPort);
		restUtils.doPost("test", String.class, "/run");
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void startWorkers3() {

		Instant start = Instant.now();
		taskService.insertTasks();
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		log.debug("Insertion time: {}", duration.toString());

		restUtils.setPort(localServerPort);
		restUtils.doPost("test", String.class, "/run2");
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
