package com.eugenescobich.outbox.pattern.controller;

import com.eugenescobich.outbox.pattern.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OutboxPatternServiceController {

    private final WorkerService workerService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Google Kubernetes Engine!";
    }

    @PostMapping("/run")
    public String run(@RequestBody String object) {
        log.info("/run endpoint was called");
        workerService.startSchedulers();
        return "OK";
    }

    @PostMapping("/run2")
    public String run2(@RequestBody String object) {
        log.info("/run2 endpoint was called");
        workerService.startSchedulers2();
        return "OK";
    }

    @PostMapping("/start-workers")
    public String startWorkers(@RequestBody String object) {
        log.info("/start-workers endpoint was called");
        workerService.startWorkers();
        return "OK";
    }
}
