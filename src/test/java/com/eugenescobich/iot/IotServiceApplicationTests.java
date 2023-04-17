package com.eugenescobich.iot;

import com.eugenescobich.iot.initializer.TestApplicationContextInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = TestApplicationContextInitializer.class)
@ActiveProfiles("test")
class IotServiceApplicationTests {

	@Test
	void contextLoads() {
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
