package com.example.backend.mqtt;

import com.example.backend.dto.Message;
import com.example.backend.entity.DeviceData;
import com.example.backend.repository.DeviceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

/**
 *  This mqttService is responsible for setting the implementation of publishement and callback information received the MQTT Broker
 *  For future use we can add more implementation ( like the subscription ..)
 */

@Component
@RequiredArgsConstructor
public class MqttService {
    private static final Logger logger = LoggerFactory.getLogger(MqttService.class);

    private final String LIGHT_TOPIC = "light/";
    private final String TEMPERATURE_TOPIC = "temperature/";

    private final int MIN = 1;
    private final int MAX = 10;
    private final Random random = new Random();
    private final int randomInterval = new Random().nextInt((1000/MIN) - (1000/MAX) + 1) + (1000/MAX);

    private int messagesCount = 0;

    private final ObjectMapper objectMapper;
    private final DeviceRepository deviceRepository;


    /**
     *  The light topic is scheduled to run as background job in a random interval between 100ms and 1000s,
     *  based on the randomInterval variable mentioned above.
     *  Running the method each 100ms : means publish 10 messages per second
     *  running the method each 1000s : means publish 1 message per second
     *  minimum and maximum numbers of messages are modifiable above.
     */
    @Scheduled(fixedRate = 100)
    public void publishToLightTopic() throws MqttException, JsonProcessingException, InterruptedException {

        Thread.sleep(randomInterval);
        int deviceId = random.nextInt(100000)+1;
        String payload = UUID.randomUUID().toString().replace("-","");

        Message newMessage = Message.builder()
                .deviceId(deviceId)
                .payload(payload)
                .build();

        MqttMessage mqttMessage = new MqttMessage(objectMapper.writeValueAsBytes(newMessage));
        MqttConfig.getInstance().publish(LIGHT_TOPIC,mqttMessage);
        System.out.println("new message published to light topic ");

    }

    /**
     *  The temperature topic takes the message as request payload rather than being run as background job
     */

    public void publishToTemperatureTopic(Message message) throws JsonProcessingException, MqttException {
        MqttMessage mqttMessage = new MqttMessage(objectMapper.writeValueAsBytes(message));
        MqttConfig.getInstance().publish(TEMPERATURE_TOPIC, mqttMessage);
    }


    /**
     *  The callback function will help us access the information received after we subscribed to a given topic
     */
    @PostConstruct
    public void initCallBack(){
        MqttConfig.getInstance().setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.info("Connection lost : " + throwable.getMessage());

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                logger.info("new msg :" + mqttMessage.toString() + " topic " + topic);
                messagesCount++;
                saveReceivedMessages(topic,mqttMessage);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                logger.info("Delivery Completed :" + iMqttDeliveryToken.toString());
            }
        });
    }

    /**
     *  We should store the data received from the broker for later use/analysis..
     */
    public void saveReceivedMessages(String topic, MqttMessage mqttMessage) throws JsonProcessingException {
        String data = new String(mqttMessage.getPayload());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode deviceData = mapper.readTree(data);

        if (deviceData.has("deviceId") && deviceData.has("payload")) {
            int deviceId = deviceData.get("deviceId").asInt();
            String payload = deviceData.get("payload").asText();
            DeviceData savedDevice = DeviceData.builder().deviceId(deviceId).payload(payload).topic(topic).build();
            deviceRepository.save(savedDevice);

        }

    }

    /**
     *  this method provides number of messages received in last second.
     */

    public int getLastMsgCount(){
        return messagesCount;
    }



}
