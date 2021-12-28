package com.hattori.payment.services.impl;

import com.hattori.payment.services.SendSimpleMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class SendSimpleMessageImpl implements SendSimpleMessage {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendSimpleMessage(String to, String text) {

        log.info("IN SendSimpleMessageImpl sendSimpleMessage {}", to);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("example@gmail.com");
        message.setTo(to);
        message.setSubject("Код подтверждения");
        message.setText("Ваш код подтверждения: " + text);
        mailSender.send(message);
    }
}
