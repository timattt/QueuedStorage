package org.timattt.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private int id;
    private OperationType operationType;
    private String key;
    private String value;

    public Message(OperationType operationType, String key) {
        this.operationType = operationType;
        this.key = key;
    }

    public Message(int id, OperationType operationType, String key) {
        this.operationType = operationType;
        this.key = key;
        this.id = id;
    }

}