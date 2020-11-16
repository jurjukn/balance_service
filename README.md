# balance_service

Sorry for having so few unit tests. Ran out of time.

Run:
Open Intellij, install dependencies and run Spring Boot project.
For testing I suggest using Postman.


# Endpoints
- api/bank_accounts/{account_id}/balance
	- GET
- api/bank_accounts/{account_id}/bank_statements/export?dateFrom=2015-01-01&dateTo=2018-01-15 
	- GET -- exports to csv file. "dateFrom" and "dateTo" are optional
- api/bank_accounts/{account_id}/bank_statements/import -- with parameter file of type file. Csv file can be attached
  - POST with parameter file containing csv file.
- api/bank_statements/import
  - POST with parameter file containing csv file.
