package com.hattori.payment.model.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDto {

    Long id;
    boolean success;
    Date add_date;
    CodeDto code;
}
