package com.example.backend.mqtt;

import org.eclipse.paho.client.mqttv3.*;


/**
 *  This configuration is responsible for connecting the mqtt broker using Mosquitto
 *  We also created an Mqtt Client that subscribe to our topics
 *  We used the singleton design pattern for different reason:
 *  - for this task we can have only one instance of mqtt client to achieve the work
 *  -the connection is centralized in this config file
 *  -the instance will be easier to use for different operations later in the code
 */
public class MqttConfig {

    private static final String MQTT_PUBLISHER_ID = "mqttclient";
    private static final String MQTT_SERVER_ADDRESS= "tcp://mosquitto:1883";
    private static IMqttClient instance;


    public static IMqttClient getInstance() {
        try {
            if (instance == null) {
                instance = new MqttClient(MQTT_SERVER_ADDRESS, MQTT_PUBLISHER_ID);
            }

            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setMaxInflight(3000);
            options.setConnectionTimeout(10);

            if (!instance.isConnected()) {
                instance.connectWithResult(options);
                instance.subscribe("#");

            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

        return instance;
    }

    private MqttConfig() {
    }
}
