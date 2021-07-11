package com.edgh.accountservice.model.repository;

import com.edgh.accountservice.constants.Constants;
import com.edgh.accountservice.model.entity.Account;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByIBAN(String iban);
}
