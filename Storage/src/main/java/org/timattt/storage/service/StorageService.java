package org.timattt.storage.service;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.timattt.common.message.Message;
import org.timattt.common.service.ResultService;
import org.timattt.storage.structure.SerializationBasedKeyValueStorage;

@Service
@Log
public class StorageService {

    private final SerializationBasedKeyValueStorage<String, String> kvStorage;
    private final ResultService resultService;

    @Autowired
    public StorageService(SerializationBasedKeyValueStorage<String, String> kvStorage, ResultService resultService) {
        this.kvStorage = kvStorage;
        this.resultService = resultService;
    }

    public void processMessage(Message message) {
        String key = message.getKey();
        String value = message.getValue();

        switch (message.getOperationType()) {
            case info:
                log.info(kvStorage.readAll().toString());
                break;
            case read:
                value = kvStorage.read(key);
                break;
            case exists:
                value = Boolean.toString(kvStorage.exists(key));
                break;
            case write:
                kvStorage.write(key, value);
                value = "OK";
                break;
            case delete:
                kvStorage.delete(key);
                value = "OK";
                break;
        }

        resultService.putResult(message.getId(), message.getOperationType(), key, value);
    }

}
