package com.eugenescobich.iot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IotServiceController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Google Kubernetes Engine!";
    }
}
