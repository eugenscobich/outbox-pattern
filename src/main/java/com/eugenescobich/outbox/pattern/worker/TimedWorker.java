package com.eugenescobich.outbox.pattern.worker;

import com.eugenescobich.outbox.pattern.service.TaskService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TimedWorker implements Runnable {

    private final TaskService taskService;
    private final Integer timeout;
    private final Integer divideBy;
    private final Integer restOfDivision;

    @Override
    public void run() {
        log.info("Start timed worker for divideBy: {} and restOfDivision: {}", divideBy, restOfDivision);
        Instant now = Instant.now();
        var startMillis = now.getEpochSecond() * 1000 + now.getNano() / 1000000;
        var currentMillis = startMillis;
        while (startMillis + timeout > currentMillis) {
            try {
                taskService.handleTask(divideBy, restOfDivision);
            } catch (RuntimeException ex) {
                log.error(ex.getMessage());
            }
            Instant nextNow = Instant.now();
            currentMillis = nextNow.getEpochSecond() * 1000 + nextNow.getNano() / 1000000;
        }
    }
}
