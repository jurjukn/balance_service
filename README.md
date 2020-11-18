# balance_service

Sorry for having so few and poor unit tests. Ran out of time.

Run:
Open Intellij, install dependencies and run Spring Boot project.
There are hardcoded bank accounts and currencies in files "FakeCurrenciesDataAccessService.java" and "FakeBankAccountsDataAccessService.java". This was done in order to be able to do testing. There are 2 valid bank account number 123 and 222. 
Make sure to do a POST request which would import bank statements. Otherwise other request will have nothing to return.   
For testing I suggest using Postman.

# Endpoints
- api/bank_accounts/{accountNumber}/balance?dateFrom=2015-01-01&dateTo=2018-01-15 
	- GET -- get balance. "dateFrom" and "dateTo" are optional
- api/bank_accounts/{accountNumber}/bank_statements/export?dateFrom=2015-01-01&dateTo=2018-01-15 
	- GET -- exports to csv file. "dateFrom" and "dateTo" are optional
- api/bank_statements/export?dateFrom=2016-01-01&dateTo=2018-06-01
	- GET -- exports to csv file. "dateFrom" and "dateTo" are optional
- api/bank_accounts/{accountNumber}/bank_statements/import -- with parameter file of type file. Csv file can be attached
  - POST with parameter file containing csv file.
- api/bank_statements/import
  - POST with parameter file containing csv file.
  
# Request examples and test data
https://we.tl/t-ytV248Lok4
Also attached with email


