package com.example.backend.service;


import com.example.backend.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppService{
    private final String LIGHT_TOPIC = "light/";
    private final String TEMPERATURE_TOPIC = "temperature/";
    private final DeviceRepository deviceRepository;

    public int countLightTopic(){
        return deviceRepository.countByTopic(LIGHT_TOPIC);
    }

    public int countTempTopic(){
        return deviceRepository.countByTopic(TEMPERATURE_TOPIC);
    }


}
