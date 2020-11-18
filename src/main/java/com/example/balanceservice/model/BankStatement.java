package com.example.balanceservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankStatement {

    private String accountNumber;
    private LocalDateTime localDateTime;
    private String beneficiary;
    private BigDecimal amount;
    private String currency;
    private String comment;

    public BankStatement(String accountNumber,
                         LocalDateTime localDateTime,
                         String beneficiary,
                         String comment,
                         BigDecimal amount,
                         String currency) {
        this.accountNumber = accountNumber;
        this.localDateTime = localDateTime;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public String getComment() {
        return comment;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public static class Builder {
        final private String accountNumber;
        private LocalDateTime localDateTime;
        private String beneficiary;
        private String comment;
        private BigDecimal amount;
        private String currency;

        public Builder(String accountNumber, LocalDateTime localDateTime,
                       String beneficiary, BigDecimal amount, String currency) {
            this.accountNumber = accountNumber;
            this.localDateTime = localDateTime;
            this.beneficiary = beneficiary;
            this.amount = amount;
            this.currency = currency;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public BankStatement build() {
            return new BankStatement(
                    this.accountNumber,
                    this.localDateTime,
                    this.beneficiary,
                    this.comment,
                    this.amount,
                    this.currency
            );
        }
    }
}
