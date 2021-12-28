package com.hattori.payment.model.dto;

import com.hattori.payment.model.dto.UserDto;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PayDto {

    Long id;

    @NotEmpty(message = "название услуги платежа не может быть пустым")
    String serviceName;

    Date addDate;

    Date editDate;

    @NotNull(message = "нет данных о пользователе, проверьте корректность внесенных данных")
    UserDto user;

}
