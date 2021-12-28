package com.hattori.payment.model.dto;

import com.hattori.payment.model.enums.CodeStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CodeDto {

    Long id;
    String code;
    Date startDate;
    Date endDate;
    CodeStatus codeStatus;
    UserDto user;
}
