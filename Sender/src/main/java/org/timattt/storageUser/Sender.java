package org.timattt.storageUser;

import lombok.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.timattt.common.message.Message;
import org.timattt.common.message.OperationType;
import org.timattt.common.service.ResultService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log
public class Sender {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ResultService resultService;

    @Value("${topic.queuedStorage}")
    private String topic;

    @PostMapping("/{operation}")
    public ResponseEntity<?> makeQuery(@PathVariable OperationType operation,
                                       @RequestParam String key,
                                       @RequestParam(required = false) String value) {
        int id = resultService.createEmptyResult(operation);
        Message message = new Message(id, operation, key, value);
        kafkaTemplate.send(topic, message);
        return ResponseEntity.ok(Map.of("id", id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> checkResult(@PathVariable int id) {
        if (!resultService.checkResultReady(id)) {
            return ResponseEntity.ok(Map.of("status", "not ready"));
        } else {
            return ResponseEntity.ok(Map.of("status", "ready", "data", resultService.getResult(id)));
        }
    }

    @ExceptionHandler
    public ResponseEntity<?> handleError(Exception e) {
        log.warning(e.getMessage());
        return ResponseEntity.internalServerError().build();
    }

}
