# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>Data Access Objects (DAO) tutorial

The tutorial on JDBC API and Data Access Objects

### Pre-conditions :heavy_exclamation_mark:
You're supposed to be familiar with SQL and relational databases, have basic knowledge of JDK and JUnit, and be able to write Java code. 
### Related exercises :muscle:
* [Product DAO](https://github.com/bobocode-projects/jdbc-api-exercises/tree/master/product-dao)
### See also :point_down:
* [Tutorial on JDBC API basics](https://github.com/bobocode-projects/jdbc-api-tutorial/tree/master/jdbc-basics)
##
***Data Access Object (DAO)*** is an object that encapsulates all **database configuration, data access and manipulation logic.**
It hides all database queries, and provides a convenient API based on object-oriented model. So you can access and manipulate
data in you business logic using your business objects (models).

***A model (entity)*** is a class that represents a **business entity**, **stores data** and **does not contain any business logic.**
 

### Best practices
* create a separate *DAO* for each *entity (model)*
* separate declaration (interface) and its implementation
* keep all database-related details behind the *DAO*
* wrap database-related exception with custom ones, providing more data and meaningful messages
* avoid mixing *Java* code with *SQL* 
