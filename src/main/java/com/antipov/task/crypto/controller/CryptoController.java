package com.antipov.task.crypto.controller;

import com.antipov.task.crypto.dto.UserRequestDto;
import com.antipov.task.crypto.exception.ValidationChecker;
import com.antipov.task.crypto.exception.ValidationException;
import com.antipov.task.crypto.service.CryptoCurrencyGetter;
import com.antipov.task.crypto.service.UserRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crypto")
public class CryptoController {

    private final CryptoCurrencyGetter cryptoCurrencyGetter;
    private final UserRequestService userRequestService;
    private final ValidationChecker validationChecker;

    public CryptoController(CryptoCurrencyGetter cryptoCurrencyGetter, UserRequestService userRequestService,
                            ValidationChecker validationChecker) {
        this.cryptoCurrencyGetter = cryptoCurrencyGetter;
        this.userRequestService = userRequestService;
        this.validationChecker = validationChecker;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll (){
        return ResponseEntity.ok().body(cryptoCurrencyGetter.getAllCrypto());
    }

    @GetMapping("/{symbol}")
    public  ResponseEntity<?> getOne (@PathVariable("symbol") String symbol){
        return ResponseEntity.ok().body(cryptoCurrencyGetter.getCryptoCurrency(symbol));
    }

    @PostMapping("/notify")
    public ResponseEntity<?> subscribe (@RequestBody UserRequestDto userRequestDto) throws ValidationException {
        validationChecker.checkPageParams(userRequestDto);
        userRequestService.registerRequest(userRequestDto);
        return ResponseEntity.ok().build();
    }



}
