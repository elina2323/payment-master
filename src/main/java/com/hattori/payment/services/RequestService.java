package com.hattori.payment.services;


import com.hattori.payment.model.dto.CodeDto;

public interface RequestService {

    void saveRequest(CodeDto codeDto, boolean value);

    int countFailedAttempts(CodeDto codeDto);
}