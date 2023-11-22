package com.example.backend.controller;


import com.example.backend.dto.LastMessages;
import com.example.backend.dto.Message;
import com.example.backend.mqtt.MqttService;
import com.example.backend.service.AppService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin
public class AppController {

    private final MqttService mqttService;
    private final AppService appService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");

    @PostMapping("publish")
    public void sub(@RequestBody Message message) throws MqttException, JsonProcessingException {
        mqttService.publishToTemperatureTopic(message);
    }


    @GetMapping("latestMessage")
    public ResponseEntity<LastMessages> getLatestMessage() {
        LocalDateTime date = LocalDateTime.now();
        String currentTime = formatter.format(date);
        LastMessages latestResponse = LastMessages
                .builder()
                .nbMessages(mqttService.getLastMsgCount())
                .date(currentTime)
                .build();
        return ResponseEntity.ok().body(latestResponse);
    }

    @GetMapping("lightTopic")
    public int getCountSensor() {
        return appService.countLightTopic();
    }

    @GetMapping("tempTopic")
    public int getCountEvent() {
        return appService.countTempTopic();
    }




}
