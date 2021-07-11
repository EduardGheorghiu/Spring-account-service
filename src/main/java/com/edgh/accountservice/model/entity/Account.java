package com.edgh.accountservice.model.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Table(name = "Account")
@Entity(name = "Account")
@SequenceGenerator(name = "seq_account", sequenceName = "seq_account", allocationSize = 1)
public class Account implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "seq_plan", strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "iban")
    private String IBAN;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "last_update_date", nullable = true, columnDefinition = "TIMESTAMP")
    private Timestamp last_update_date;

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"IBAN\":\"" + IBAN + '\"' +
                ", \"currency\":\"" + currency + '\"' +
                ", \"balance\":" + balance +
                ", \"last_update_date\":\"" + last_update_date + '\"' +
                '}';
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
