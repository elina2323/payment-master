package com.hattori.payment.services.impl;

import com.hattori.payment.dao.PayRepo;
import com.hattori.payment.exception.UserNotFoundException;
import com.hattori.payment.mapper.PayMapper;
import com.hattori.payment.model.dto.PayDto;
import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.model.entity.Pay;
import com.hattori.payment.model.response.ErrorResponse;
import com.hattori.payment.services.PayService;
import com.hattori.payment.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PayServiceImpl implements PayService {

    private  PayRepo payRepo;

    private final UserService userService;

    @Value("${jwtSecret}")
    private String secretKey;

    public PayServiceImpl(PayRepo payRepo, UserService userService) {
        this.payRepo = payRepo;
        this.userService = userService;
    }

    @Override
    public ResponseEntity<?> save(String token, PayDto payDto) {

        log.info("IN PayServiceImpl saveRequest {}", token);

       ResponseEntity<?> responseEntity =
                userService
                        .verifyLogin(token);

        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {

            return responseEntity;
        }

        UserDto userDto =
                userService
                        .findUserByLogin(responseEntity.getBody().toString());

        if (Objects.isNull(userDto)){
            throw new UserNotFoundException();
        }
        Pay pay = PayMapper.INSTANCE.mapToPay(payDto);
        pay = payRepo.saveAndFlush(pay);
        return ResponseEntity
                .ok(PayMapper
                        .INSTANCE
                        .mapToPayDto(pay));
    }

    @Override
    public ResponseEntity<?> check(String token, Long paymentId) {

        ResponseEntity<?> responseEntity =
                userService
                        .verifyLogin(token);

        if (!responseEntity.getStatusCode().equals(HttpStatus.OK)) {

            return responseEntity;
        }

        Pay pay =
                payRepo.findPayById(paymentId);

        if (Objects.isNull(pay)) {
            return new ResponseEntity<>(
                    new ErrorResponse("Не найден платеж!"
                            , "Вы ввели некорректный ID платежа!")
                    , HttpStatus.NOT_FOUND);
        }
        return ResponseEntity
                .ok(PayMapper
                        .INSTANCE
                        .mapToPayDto(pay)
                );
    }
}
