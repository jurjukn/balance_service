package com.example.balanceservice.model;

import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;

public class BankStatement {

    private String accountNumber;
    private String date;
    private String beneficiary;
    private String comment;
    private BigDecimal amount;
    private String currency;

    public BankStatement(String accountNumber, String date, String beneficiary,
                         String comment, BigDecimal amount, String currency) {
        this.accountNumber = accountNumber;
        this.date = date;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }

    public BankStatement(CSVRecord csvRecord) {
        this.accountNumber = csvRecord.get("AccountNumber");
        this.date = csvRecord.get("Date");
        this.beneficiary = csvRecord.get("Beneficiary");
        this.comment = csvRecord.get("Comment");
        this.amount = new BigDecimal(csvRecord.get("Amount"));
        this.currency = csvRecord.get("Currency");
    }

    public String getAccountNumber() {
        return accountNumber;
    }

}
