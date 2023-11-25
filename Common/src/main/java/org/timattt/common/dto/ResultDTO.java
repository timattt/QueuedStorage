package org.timattt.common.dto;

import lombok.Data;
import org.timattt.common.model.Result;
import org.timattt.common.model.ResultData;

@Data
public class ResultDTO {

    private int id;
    private ResultData resultData;

    public ResultDTO(Result result) {
        this.id = result.getId();
        this.resultData = result.getResultData();
    }

}
