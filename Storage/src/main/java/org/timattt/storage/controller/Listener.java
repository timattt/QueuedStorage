package org.timattt.storage.controller;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.timattt.common.message.Message;
import org.timattt.storage.service.StorageService;

@Service
@Log
public class Listener {

    private StorageService storageService;

    @Autowired
    public Listener(StorageService storageService) {
        this.storageService = storageService;
    }

    @KafkaListener(
            topics = "${topic.queuedStorage}",
            groupId = "${spring.kafka.consumer.group-id}",
            properties = {"spring.json.value.default.type=org.timattt.common.message.Message"}
    )
    public void receiveReadMessage(Message message) {
        log.info("Received event: " + message);
        storageService.processMessage(message);
    }

}
