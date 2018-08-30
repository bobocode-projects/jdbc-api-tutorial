# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>JDBC API basics tutorial

The tutorial on JDBC API essential features and basic configurations

### Pre-conditions :heavy_exclamation_mark:
You're supposed to be familiar with SQL and relational databases, have basic knowledge of JDK, and be able to write Java code. 
##
***JDBC API*** is the only part of *JDK* that provides an ability to **connect to a relational database from Java.** 
Since it's just an API, in order to call a real database, you need a specific implementation of that API for each database.
*JDBC API* implementation is called **JDBC Driver**. Each driver is provided by its database vendor.

The basic flow of working with database is **getting connection, performing SQL query, and getting results.**
Here's the list of most important *JDBC API* classes needed for calling db and getting results:

 JDBC API class | Description
          --- | --- 
 `DataSource` | Represents a concrete database server  
 `Connection` | Represents a real physical network connection to the db 
 `Statement`  | Represents a SQL query
 `ResultSet`  | Represents a query result received from the db

Check out the `SimpleJdbcExample.java` class to see the real working example that uses all of the classes listed above.

### Best practices
* use *try-with-resources* to handle database connection 
* prefer `PreparedStatement` for *SQL* queries with parameters
* avoid mixing SQL queries with Java code withing one method
* avoid returning JDBC API classes (like `Connection`, or `ResultSet`) form `public` methods
