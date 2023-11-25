package org.timattt.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.timattt.common.message.OperationType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultData {

    private OperationType operationType;
    private String key;
    private String value;

}
