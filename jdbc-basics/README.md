# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>JDBC API basics tutorial

The tutorial on JDBC API essential features and basic configurations

### Pre-conditions :heavy_exclamation_mark:
You're supposed to be familiar with SQL and relational databases, have basic knowledge of JDK and JUnit, and be able to write Java code. 
##
***JDBC API*** is a single part of *JDK* that provides an ability to **connect to a relational database from Java.** 
Since it's just an API, in order to call a real database, you need a specific implementation of that API for each database 

### Best practices
* use *try-with-resources* to handle database connection 
* prefer `PreparedStatement` for *SQL* queries with parameters
* avoid mixing SQL queries with Java code withing one method
* avoid returning JDBC API classes (like `Connection`, or `ResultSet`) form `public` methods
