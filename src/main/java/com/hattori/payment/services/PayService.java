package com.hattori.payment.services;

import com.hattori.payment.model.dto.PayDto;
import org.springframework.http.ResponseEntity;

public interface PayService {

    ResponseEntity<?> save(String token, PayDto payDto);
    ResponseEntity<?> check(String token, Long paymentId);
}
