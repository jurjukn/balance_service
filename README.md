# balance_service

Sorry for having to little unit tests. Ran out of time.

Endpoints:
  GET
    api/bank_accounts/{account_id}/balance
    api/bank_accounts/{account_id}/bank_statements/export?dateFrom=2015-01-01&dateTo=2018-01-15  -- exports to csv file. "dateFrom" and "dateTo" are optional
    api/bank_statements/export?dateFrom=2016-01-01&dateTo=2018-06-01 -- exports to csv file. "dateFrom" and "dateTo" are optional
    
  POST
    api/bank_accounts/{account_id}/bank_statements/import -- with parameter file of type file. Csv file can be attached
      
    api/bank_statements/import
