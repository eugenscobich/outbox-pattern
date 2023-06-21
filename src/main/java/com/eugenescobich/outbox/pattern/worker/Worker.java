package com.eugenescobich.outbox.pattern.worker;

import com.eugenescobich.outbox.pattern.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class Worker implements Runnable {

    private final TaskService taskService;

    @Override
    public void run() {
        while (true) {
            try {
                taskService.handleTask();
            } catch (RuntimeException ex) {
                log.error(ex.getMessage());
            }
        }
    }
}
