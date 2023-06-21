package com.eugenescobich.outbox.pattern.service;

import com.eugenescobich.outbox.pattern.persistance.model.Task;
import com.eugenescobich.outbox.pattern.persistance.repository.TaskRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.postgresql.copy.CopyIn;
import org.postgresql.copy.CopyManager;
import org.postgresql.jdbc.PgConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Lazy
    @Autowired
    private TaskService selfReference;

    @Transactional
    public void insertTasks() {
        Session session = taskRepository.getEntityManager().unwrap(Session.class);
        session.doWork(connection -> {
            PgConnection copyOperationConnection = connection.unwrap(PgConnection.class);
            CopyManager copyManager = new CopyManager(copyOperationConnection);
            CopyIn copyIn = copyManager.copyIn("COPY schema_outbox_pattern_service.t_task FROM STDIN WITH DELIMITER ','");
            try {

                for (long i = 0; i < 10_000_000; i++) {
                    var now = LocalDateTime.now();
                    var time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String rowTemplate = "%d,%d,'%s',%d,'%s',true\n";
                    String row = String.format(rowTemplate, i, i, time, 0, time);
                    byte[] bytes = row.getBytes();
                    copyIn.writeToCopy(bytes, 0, bytes.length);
                }

                for (long i = 10_000_000; i < 10_000_000 + 100_000; i++) {
                    var now = LocalDateTime.now();
                    var time = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String rowTemplate = "%d,%d,'%s',%d,'%s',false\n";
                    String row = String.format(rowTemplate, i, i, time, 0, time);
                    byte[] bytes = row.getBytes();
                    copyIn.writeToCopy(bytes, 0, bytes.length);
                }

                var result = copyIn.endCopy();
                log.info("Saved tasks: {}", result);
            } finally {
                if (copyIn.isActive()) {
                    copyIn.cancelCopy();
                }
            }
        });
    }

    /*
        @Transactional
        public void handleTask() {
            var  pageable = PageRequest.of(0,1);
            var tasks = taskRepository.findTask(pageable, LocalDateTime.now(), 5);
            if (tasks.isEmpty()) {
                log.info("Could not find tasks");
            } else {
                Task task = tasks.get(0);
                var orderedTasks = taskRepository.findOrderedTask(pageable, task.getEntityId());
                if (orderedTasks.isEmpty()) {
                    log.info("Could not find ordered tasks");
                } else {
                    Task orderedTask = orderedTasks.get(0);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    orderedTask.setSent(true);
                }
            }
        }

    */
    @Transactional
    public void handleTask() {
        var pageable = PageRequest.of(0, 1);
        var tasks = taskRepository.findTask(pageable, LocalDateTime.now(), 5);
        if (tasks.isEmpty()) {
            log.info("Could not find tasks");
        } else {
            Task task = tasks.get(0);
            var orderedTasks = taskRepository.findOrderedTask(pageable, task.getEntityId());
            if (orderedTasks.isEmpty()) {
                log.info("Could not find ordered tasks");
            } else {
                Task orderedTask = orderedTasks.get(0);
                saveTask(orderedTask.getId());
            }
        }

    }

    @Transactional
    public void handleTask(Integer divideBy, Integer restOfDivision) {
        var pageable = PageRequest.of(0, 10000);
        var tasks = taskRepository.findTaskByDivision(pageable, divideBy, restOfDivision);
        if (tasks.isEmpty()) {
            log.info("Could not find tasks");
        } else {
            for (Task task : tasks) {
                selfReference.saveTask(task.getId());
            }
        }
        log.info("========================================");
    }

    @Transactional
    public void saveTask(Long taskId) {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        taskRepository.saveByUpdate(taskId);
    }

    public void handleTaskForUpdate(Integer divideBy, Integer restOfDivision) {
        for (long i = 10_000_000 + restOfDivision; i < 10_000_000 + 100_000; i += divideBy) {
            selfReference.saveTask(i);
        }
        log.info("========================================");
    }
}
