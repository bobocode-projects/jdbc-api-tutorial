# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>JDBC transaction tutorial

The tutorial on JDBC API transaction management features

### Pre-conditions :heavy_exclamation_mark:
You're supposed to be familiar with SQL and relational databases, have basic knowledge of JDK, and be able to write Java code. 
##

#### SQL
Relational databases provide a simple API to handle transactions. There are three main self-descriptive SQL commands:

```sql
START TRANSACTION
```
```sql
COMMIT 
```
```sql
ROLLBACK 
```

**All SQL queries that you perform after transaction started and before it was closed, is done in the scope of one transaction.**

#### JDBC API
JDBC encapsulates transaction management in class `java.sql.Connection`. **By default, it uses auto-commit mode.** E.g. 
all changes are committed automatically when you execute your statement.

In order, to handle transaction manually you need to **turn off auto-commit mode**.
```java
    connection.setAutoCommit(false);
```
it will start new transaction, and all following statements will be executed in the scope of that transaction, 
until you call one of the following methods:

```java
    connection.commit();
```
```java
    connection.rollback();
```

### Best practices
* prefer *try-with-resources* to `final` in order to close the resource
* avoid using `auto-commit` mode
* always catch exception and `rollback` a transaction as early as possible in case of error
* avoid mixing transaction management with other logic 

