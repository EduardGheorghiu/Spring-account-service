package com.edgh.accountservice.controller;


import com.edgh.accountservice.model.DTO.ErrorDTO;
import com.edgh.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping(path = "/account")
public class AccountController {

    @Resource
    AccountService accountService;


    @GetMapping(path = "/get")
    public ResponseEntity<String> getAccount(@RequestParam(value = "iban") String iban) {
        try {
            String response = accountService.getAccountByIBAN(iban).toString();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ErrorDTO(e), HttpStatus.NOT_FOUND);
        }

    }
}
