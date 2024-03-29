# Assignment
You have to build a service which sole purpose is to manage bank account balance via Rest API.

Create endpoints to:
- Import bank statement for one or several bank accounts via CSV.
- Export bank statement for one or several bank accounts via CSV.
- Calculate account balance for given date.

Notes:
- Information imported/exported via CSV:
	- Account number, mandatory
	- Operation date/time, mandatory
	- Beneficiary, mandatory
	- Comment, optional
	- Amount, mandatory
	- Currency, mandatory
- When exporting CSV, accept:
	- date from, optional
	- date to, optional
- When calculating account balance accept:
	- account number, mandatory
	- date from, optional
	- date to, optional

# balance_service

Run:
Open Intellij, install dependencies and run Spring Boot project.
There are hardcoded currencies and bank accounts in files "FakeCurrenciesRepository.java" and "FakeBankRepository.java". This was done in order to be able to do debugging. There are 2 valid bank accounts (AccoutNumber: 123 and 222) and currencies (USD and EUR). 
Make sure to do a POST request before doing GET requests. Otherwise GET requests will have nothing to return.   
For testing I suggest using Postman.

# Endpoints
- api/bankAccounts/{accountNumber}/balance?dateFrom=2015-01-01&dateTo=2018-01-15 
	- GET -- gets balance of a account. "dateFrom" and "dateTo" are optional
- api/bankAccounts/{accountNumber}/bank_statements/export?dateFrom=2015-01-01&dateTo=2018-01-15 
	- GET -- exports bank statements of a account to a csv file. "dateFrom" and "dateTo" are optional
- api/bankStatements/export?dateFrom=2016-01-01&dateTo=2018-06-01
	- GET -- exports all bank statements to csv file. "dateFrom" and "dateTo" are optional
- api/bankAccounts/{accountNumber}/bankStatements/import -- with parameter file of type file. Csv file can be attached
  - POST with parameter file containing csv file. Imports statements for a bank account.
- api/bankStatements/import
  - POST with parameter file containing csv file. Imports statements for multiple bank accounts.
  
# Request examples and test data

Csv file containing multiple bank accounts' bank statements:
```
AccountNumber,Date,Beneficiary,Comment,Amount,Currency
222,2018-07-14T17:45:55.9483536,James Bond,transaction to my friend,-140.09,EUR
123,2014-05-11T18:35:25.9483536,Home Simpson,payment for work,39.13,USD
222,2011-05-11T18:35:25.9483536,Home Simpson,payment for work,50.56,USD
```

Csv file with single bank account bank statements:
```
AccountNumber,Date,Beneficiary,Comment,Amount,Currency
123,2018-07-14T17:45:55.9483536,James Bond,transaction to my friend,10,EUR
123,2010-07-14T17:45:55.9483536,Will Smith,bill for car,-100.52,EUR
123,2012-07-14T17:45:55.9483536,Home Simpson,payment for rent,-10,EUR
```

Export single bank account bank statements
![image](https://user-images.githubusercontent.com/30880633/145724614-70470b45-24e0-4416-93ee-0c4747fc042b.png)

Export multiple bank accounts statements
![image](https://user-images.githubusercontent.com/30880633/145724647-8b20a7b6-a94e-4454-9534-79cac69880c0.png)

Check bank account balance
![image](https://user-images.githubusercontent.com/30880633/145724687-44527361-2393-4b03-8fab-3202edf51c51.png)

Import bank statements for multiple bank accounts
![image](https://user-images.githubusercontent.com/30880633/145724698-a6d4f1db-dba0-4cc6-b24f-d6e64f858c26.png)

Import bank statement for bank account
![image](https://user-images.githubusercontent.com/30880633/145724708-73c291c6-1af3-4e6f-81b1-a47f77e54c0f.png)




