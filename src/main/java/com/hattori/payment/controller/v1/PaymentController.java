package com.hattori.payment.controller.v1;

import com.hattori.payment.model.dto.PayDto;
import com.hattori.payment.services.PayService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final PayService payService;

    public PaymentController(PayService payService) {
        this.payService = payService;
    }

    @PostMapping(value="/pay", produces=MediaType.APPLICATION_XML_VALUE)
    ResponseEntity<?> pay(@RequestHeader String token, @RequestBody @Valid PayDto payDto){
        return payService.save(token,payDto);
    }

    @PostMapping(value="/check", produces=MediaType.APPLICATION_XML_VALUE)
    ResponseEntity<?> check(@RequestHeader String token, @RequestParam Long id){
        return payService.check(token, id);
    }


}
