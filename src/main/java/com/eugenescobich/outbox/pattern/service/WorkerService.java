package com.eugenescobich.outbox.pattern.service;

import com.eugenescobich.outbox.pattern.worker.TimedWorker;
import com.eugenescobich.outbox.pattern.worker.Worker;
import com.eugenescobich.outbox.pattern.worker.WorkerByDivider;
import com.eugenescobich.outbox.pattern.worker.WorkerByDividerAndUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkerService {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private final TaskService taskService;
    private final TaskScheduler taskScheduler;

    public void startWorkers() {
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(new Worker(taskService));
        }
    }

    public void startSchedulers() {
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(new WorkerByDivider(taskService, 10, i));
        }
    }

    public void startSchedulers2() {
        for (int i = 0; i < 10; i++) {
            threadPoolTaskExecutor.execute(new WorkerByDividerAndUpdate(taskService, 10, i));
        }
    }
}
