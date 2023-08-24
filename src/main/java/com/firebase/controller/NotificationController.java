package com.firebase.controller;

import com.firebase.entity.NotificationMessage;
import com.firebase.service.FirebaseMessagingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifs")
@Slf4j
public class NotificationController {
    @Autowired
    private FirebaseMessagingService service;

    @Value("${web.url}")
    private String link;

    @PostMapping(value = "topic/{topic}")
    public ResponseEntity<?> sendNotifByTopic(@PathVariable String topic) {

        try {
            service.sendMessageByTopic(topic);
        } catch (Exception e) {
            log.error("sendNotif error:", e);
        }

        return ResponseEntity.ok("Success Send Notification");
    }

    @PostMapping(value = "/token/{token}")
    public ResponseEntity<?> sendNotifByToken(@PathVariable String token) {

        try {
            service.sendSingleDevice(token);
        } catch (Exception e) {
            log.error("sendNotif error:", e);
        }

        return ResponseEntity.ok("Success Send Notification");
    }
    @PostMapping
    public ResponseEntity<?> sendNotificationByToken(@RequestBody NotificationMessage notificationMessage) {
        Map<String, String> dataPayload = new HashMap<String, String>();
        dataPayload.put("click_action", link);
        dataPayload.put("link",link);
        notificationMessage.setData(dataPayload);
        return ResponseEntity.ok(service.sendNotificationByToken(notificationMessage));
    }

}
