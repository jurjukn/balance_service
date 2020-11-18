# balance_service

Run:
Open Intellij, install dependencies and run Spring Boot project.
There are hardcoded currencies and bank accounts in files "FakeCurrenciesRepository.java" and "FakeBankRepository.java". This was done in order to be able to do debugging. There are 2 valid bank accounts (AccoutNumber: 123 and 222) and currencies (USD and EUR). 
Make sure to do a POST request before doing GET requests. Otherwise GET requests will have nothing to return.   
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


