-- Select everything (each row) from account table in public schema
SELECT * FROM account;

-- Select only first name of all accounts
SELECT first_name FROM account;

-- Select email, and balance of all account
SELECT email, balance FROM account;

-- Select all account where balance is more that 100000
SELECT * FROM account WHERE balance > 100000.0;

-- Select all account where balance is more that 100000 and email is powered by Google
SELECT * FROM account WHERE balance > 100000.0 AND email LIKE '%gmail.com';

