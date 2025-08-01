package com.dhanashri.Notification_Service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/ping")
    public String ping() {
        return "Notification service is alive!";
    }
}
