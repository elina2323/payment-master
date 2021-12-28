package com.hattori.payment.controller.v1;


import com.hattori.payment.model.dto.LoginDto;
import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.services.UserService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @PostMapping("/sendCode")
    public ResponseEntity<?> sendCode(@RequestParam String login) {
        return userService.sendCode(login);
    }

    @PostMapping("/login")
    public ResponseEntity<?> getToken(@RequestBody LoginDto loginDto) {
        return userService.getToken(loginDto);
    }

    @GetMapping(value = "/verify", produces= MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<?> verifyLogin(@RequestHeader String token) {
        return userService.verifyLogin(token);
    }
}