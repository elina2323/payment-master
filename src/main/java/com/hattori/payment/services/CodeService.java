package com.hattori.payment.services;

import com.hattori.payment.model.dto.CodeDto;
import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.model.entity.Code;

public interface CodeService {
    void saveCode(CodeDto codeDto);

    Code findLastCode(UserDto userDto);

    void sendCode(UserDto userDto);

}
