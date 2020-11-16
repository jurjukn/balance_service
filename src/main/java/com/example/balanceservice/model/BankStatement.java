package com.example.balanceservice.model;

import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankStatement {

    private String accountNumber;
    private LocalDateTime dateTime;
    private String beneficiary;
    private String comment;
    private BigDecimal amount;
    private String currency;

    public BankStatement(String accountNumber, LocalDateTime dateTime, String beneficiary, String comment, BigDecimal amount, String currency) {
        this.accountNumber = accountNumber;
        this.dateTime = dateTime;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }

    public BankStatement(CSVRecord csvRecord) {
        this.accountNumber = csvRecord.get("AccountNumber");
        this.dateTime = LocalDateTime.parse(csvRecord.get("Date"));
        this.beneficiary = csvRecord.get("Beneficiary");
        this.comment = csvRecord.get("Comment");
        this.amount = new BigDecimal(csvRecord.get("Amount"));
        this.currency = csvRecord.get("Currency");
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
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

    public boolean isValid() {
        return !this.accountNumber.isEmpty() && this.dateTime != null
                && !this.beneficiary.isEmpty() && this.amount != null
                && this.currency != null;
    }
}
