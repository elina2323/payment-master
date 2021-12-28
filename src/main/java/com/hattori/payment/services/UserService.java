package com.hattori.payment.services;

import com.hattori.payment.model.dto.LoginDto;
import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.model.entity.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> saveUser(UserDto userDto);

    ResponseEntity<?> sendCode(String login);

    ResponseEntity<?> getToken(LoginDto loginDto);

    ResponseEntity<?> verifyLogin(String token);

    boolean userLockOutChecking(User user);

    UserDto findUserByLogin(String login);
}