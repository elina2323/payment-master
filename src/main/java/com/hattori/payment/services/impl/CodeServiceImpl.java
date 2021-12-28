package com.hattori.payment.services.impl;

import com.hattori.payment.dao.CodeRepo;
import com.hattori.payment.mapper.CodeMapper;
import com.hattori.payment.mapper.UserMapper;
import com.hattori.payment.model.enums.CodeStatus;
import com.hattori.payment.model.dto.CodeDto;
import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.model.entity.Code;
import com.hattori.payment.services.CodeService;
import com.hattori.payment.services.SendSimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;

@Service
@Slf4j
public class CodeServiceImpl implements CodeService {


    private final CodeRepo codeRepo;

    private final SendSimpleMessage sendMessage;

    public CodeServiceImpl(CodeRepo codeRepo, SendSimpleMessage sendMessage) {
        this.codeRepo = codeRepo;
        this.sendMessage = sendMessage;
    }

    @Override
    public void saveCode(CodeDto codeDto) {

        log.info("IN CodeServiceImpl saveCode {}", codeDto);

        codeRepo.save(CodeMapper.INSTANCE.mapToCode(codeDto));
    }

    @Override
    public Code findLastCode(UserDto userDto) {

        log.info("IN CodeServiceImpl findLastCode {}", userDto);

        return codeRepo
                .findByUserAndCodeStatus
                        (UserMapper.INSTANCE.mapToUser(userDto), CodeStatus.NEW);
    }

    @Override
    public void sendCode(UserDto userDto) {

        log.info("IN CodeServiceImpl sendCode {}", userDto);

        Code lastCode =
                codeRepo
                        .findByUserAndCodeStatus(UserMapper
                                .INSTANCE.mapToUser(userDto), CodeStatus.NEW);

        if (Objects.nonNull(lastCode)) {

            lastCode.setCodeStatus(CodeStatus.CANCELLED);

            codeRepo.save(lastCode);
        }

        int code = (int) ((Math.random() * 9000) + 1000);

        String hashedCode =
                BCrypt
                        .hashpw(
                                Integer
                                        .toString(code)
                                , BCrypt.gensalt());

        Calendar endOfCodeAction = Calendar.getInstance();
        endOfCodeAction.add(Calendar.MINUTE, 3);

        Code saveCode = new Code();
        saveCode.setCode(hashedCode);
        saveCode.setEndDate(endOfCodeAction.getTime());
        saveCode.setCodeStatus(CodeStatus.NEW);
        saveCode.setUser(UserMapper.INSTANCE.mapToUser(userDto));
        codeRepo.save(saveCode);

        sendMessage
                .sendSimpleMessage(
                        userDto.getEmail()
                        , Integer.toString(code));
    }
}
