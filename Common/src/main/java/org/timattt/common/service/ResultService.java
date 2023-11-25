package org.timattt.common.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.timattt.common.dto.ResultDTO;
import org.timattt.common.message.OperationType;
import org.timattt.common.model.Result;
import org.timattt.common.model.ResultData;
import org.timattt.common.repository.ResultRepository;

@Service
public class ResultService {

    private final ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Transactional
    public void putResult(int id, OperationType type, String key, String value) {
        Result result = resultRepository.getReferenceById(id);
        result.setResultData(new ResultData(type, key, value));
    }

    @Transactional
    public int createEmptyResult(OperationType type) {
        Result result = resultRepository.save(new Result());
        result.setResultData(new ResultData(type, null, null));
        return result.getId();
    }

    @Transactional
    public boolean checkResultReady(int id) {
        return resultRepository.existsById(id);
    }

    @Transactional
    public ResultDTO getResult(int id) {
        return new ResultDTO(resultRepository.getReferenceById(id));
    }

}
