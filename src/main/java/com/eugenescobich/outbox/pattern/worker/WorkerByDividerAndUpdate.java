package com.eugenescobich.outbox.pattern.worker;

import com.eugenescobich.outbox.pattern.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WorkerByDividerAndUpdate implements Runnable {

    private final TaskService taskService;
    private final Integer divideBy;
    private final Integer restOfDivision;

    @Override
    public void run() {
        while (true) {
            try {
                taskService.handleTaskForUpdate(divideBy, restOfDivision);
            } catch (RuntimeException ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
