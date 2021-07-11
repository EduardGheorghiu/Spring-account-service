package com.edgh.accountservice.service;

import com.edgh.accountservice.model.entity.Account;

public interface AccountService {
    Account getAccountByIBAN(String iban) throws  Exception;
}
