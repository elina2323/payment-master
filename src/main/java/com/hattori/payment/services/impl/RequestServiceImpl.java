package com.hattori.payment.services.impl;

import com.hattori.payment.dao.RequestRepo;
import com.hattori.payment.mapper.CodeMapper;
import com.hattori.payment.model.dto.CodeDto;
import com.hattori.payment.model.entity.Request;
import com.hattori.payment.services.RequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class RequestServiceImpl implements RequestService {

    private final RequestRepo requestRepo;

    public RequestServiceImpl(RequestRepo requestRepo) {
        this.requestRepo = requestRepo;
    }

    @Override
    public void saveRequest(CodeDto checkUserCode, boolean value) {

        log.info("IN RequestServiceImpl saveRequest {}", checkUserCode);

        Request saveRequest = new Request();
        saveRequest
                .setCode(
                        CodeMapper
                                .INSTANCE
                                .mapToCode(checkUserCode));
        saveRequest.setSuccess(value);
        requestRepo.save(saveRequest);
    }

    @Override
    public int countFailedAttempts(CodeDto codeDto) {

        log.info("IN RequestServiceImpl saveRequest {}", codeDto);

        return requestRepo
                .countByCodeAndSuccess(
                        CodeMapper
                                .INSTANCE
                                .mapToCode(codeDto)
                        , false);
    }
}
