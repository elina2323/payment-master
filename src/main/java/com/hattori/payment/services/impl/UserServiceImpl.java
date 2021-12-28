package com.hattori.payment.services.impl;

import com.hattori.payment.dao.UserRepo;
import com.hattori.payment.mapper.CodeMapper;
import com.hattori.payment.mapper.UserMapper;
import com.hattori.payment.model.dto.CodeDto;
import com.hattori.payment.model.dto.LoginDto;
import com.hattori.payment.model.dto.UserDto;
import com.hattori.payment.model.entity.User;
import com.hattori.payment.model.enums.CodeStatus;
import com.hattori.payment.model.response.ErrorResponse;
import com.hattori.payment.model.response.OkResponse;
import com.hattori.payment.model.response.SuccessLogin;
import com.hattori.payment.services.CodeService;
import com.hattori.payment.services.RequestService;
import com.hattori.payment.services.UserService;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    private final CodeService codeService;

    private final RequestService requestService;

    @Value("${jwtSecret}")
    private String secretKey;

    public UserServiceImpl(UserRepo userRepo, CodeService codeService, RequestService requestService) {
        this.userRepo = userRepo;
        this.codeService = codeService;
        this.requestService = requestService;
    }

    @Override
    public ResponseEntity<?> saveUser(UserDto userDto) {

        log.info("IN UserServiceImpl saveUser {}", userDto);

        User user = UserMapper.INSTANCE.mapToUser(userDto);

        if (Objects.isNull(userRepo.findByLogin(user.getLogin()))) {
            userRepo.save(user);
        } else {
            return new ResponseEntity<>(
                    new ErrorResponse("Пользователь уже существует", null)
                    , HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(
                UserMapper.INSTANCE.mapToUserDto(user));
    }

    @Override
    public boolean userLockOutChecking(User user) {

        log.info("IN UserServiceImpl userLockOutChecking {}", user);

        if (Objects.nonNull(user.getEndOfBlockDate())) {

            if (new Date().before(user.getEndOfBlockDate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public UserDto findUserByLogin(String login) {

        log.info("IN UserServiceImpl findUserByLogin {}", login);

        User user = userRepo.findByLogin(login);
        return UserMapper.INSTANCE.mapToUserDto(user);
    }

    @Override
    public ResponseEntity<?> sendCode(String login) {

        log.info("IN UserServiceImpl sendCode {}", login);

        User user = userRepo.findByLogin(login);

        if (Objects.isNull(user)) {
            return new ResponseEntity<>(
                    new ErrorResponse("Некорректный логин!", null)
                    , HttpStatus.NOT_FOUND);
        }

        boolean check = userLockOutChecking(user);

        if (check) {
            SimpleDateFormat formatToShowEndOfBlockDate =
                    new SimpleDateFormat("hh:mm a");

            return new ResponseEntity<>(" Превышено количество попыток входа, вы заблокированы. Повторите попытку в " +
                    formatToShowEndOfBlockDate
                            .format(
                                    user.getEndOfBlockDate()), HttpStatus.CONFLICT);
        }

        codeService.sendCode(
                UserMapper
                        .INSTANCE
                        .mapToUserDto(user));

        return ResponseEntity.ok(
                new OkResponse("Код подтверждения успешно отправлен!", null));
    }

    @Override
    public ResponseEntity<?> getToken(LoginDto loginDto) {

        log.info("IN UserServiceImpl getToken {}", loginDto);

        User user = userRepo.findByLogin(loginDto.getLogin());

        if (Objects.isNull(user)) {

            return new ResponseEntity<>(
                    new ErrorResponse("Некорректный логин!", null)
                    , HttpStatus.NOT_FOUND);
        }

        boolean check = userLockOutChecking(user);

        if (check) {
            SimpleDateFormat formatToShowEndOfBlockDate =
                    new SimpleDateFormat("hh:mm a");

            return new ResponseEntity<>(" Превышено количество попыток входа, вы заблокированы. Повторите попытку в " +
                    formatToShowEndOfBlockDate
                            .format(
                                    user.getEndOfBlockDate()), HttpStatus.CONFLICT);
        }

        CodeDto checkUserCode =
                CodeMapper
                        .INSTANCE
                        .mapToCodeDto(
                                codeService
                                        .findLastCode(
                                                UserMapper
                                                        .INSTANCE
                                                        .mapToUserDto(user)));


        if (new Date().after(checkUserCode.getEndDate())) {
            return new ResponseEntity<>(
                    new ErrorResponse(
                            "Время действия кода подтверждения истек!"
                            , "Вам нужно получить код подтверждения повторно!")
                    , HttpStatus.CONFLICT
            );
        }

        if (!BCrypt.checkpw(loginDto.getCode(), checkUserCode.getCode())) {

            requestService.saveRequest(checkUserCode, false);

            if (requestService.countFailedAttempts(checkUserCode) >= 3) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR, 1);


                user.setEndOfBlockDate(calendar.getTime());
                userRepo.save(user);

                checkUserCode.setCodeStatus(CodeStatus.FAILED);
                codeService.saveCode(checkUserCode);
            }

            return new ResponseEntity<>(
                    new ErrorResponse("Авторизация не пройдена!", "Вы ввели некорректный код подтверждения!")
                    , HttpStatus.NOT_FOUND);

        }

        requestService.saveRequest(checkUserCode, true);

        Calendar tokensTimeLive =
                Calendar.getInstance();
        tokensTimeLive
                .add(Calendar.HOUR, 3);


        String token =
                Jwts.builder()
                        .claim("login", loginDto.getLogin())
                        .setExpiration(
                                tokensTimeLive
                                        .getTime())
                        .signWith(
                                SignatureAlgorithm.HS256
                                , secretKey)
                        .compact();

        checkUserCode.setCodeStatus(CodeStatus.APPROVED);
        codeService.saveCode(checkUserCode);

        SuccessLogin successLogin = new SuccessLogin("Вы успешно ввели пароль!", token);

        return ResponseEntity.ok(successLogin);
    }

    @Override
    public ResponseEntity<?> verifyLogin(String token) {

        log.info("IN UserServiceImpl verifyLogin {}", token);

        String repeatLogin = "Пройдите авторизацию снова";
        try {
            Jws<Claims> jwt = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            return ResponseEntity.ok(jwt.getBody().get("login"));
        } catch (ExpiredJwtException jwtException) {

            return new ResponseEntity<>(new ErrorResponse("Время действия токена истекло. ",repeatLogin), HttpStatus.CONFLICT);
        } catch (UnsupportedJwtException jwtException) {

            return new ResponseEntity<>(new ErrorResponse("Неподерживаемый токен. ",repeatLogin), HttpStatus.CONFLICT);
        } catch (MalformedJwtException jwtException) {

            return new ResponseEntity<>(new ErrorResponse("Некорректный токен. ",repeatLogin), HttpStatus.CONFLICT);
        } catch (SignatureException signatureException) {

            return new ResponseEntity<>(new ErrorResponse("Некорректная подпись в токене! ",repeatLogin), HttpStatus.CONFLICT);
        } catch (Exception exception) {

            return new ResponseEntity<>(new ErrorResponse("Произошла системная ошибка, повторите запрос ","При повторной ошибке обратитесь в службу поддержки"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
