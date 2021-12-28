package com.hattori.payment.model.dto;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {

    Long id;
    @NotBlank
    String name;
    @NotBlank
    String login;
    boolean active;
    @NotBlank
    String email;
    Date endOfBlockDate;
}