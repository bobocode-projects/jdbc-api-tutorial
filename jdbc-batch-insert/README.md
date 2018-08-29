# <img src="https://raw.githubusercontent.com/bobocode-projects/resources/master/image/logo_transparent_background.png" height=50/>BATCH INSERT tutorial

The tutorial on SQL BATCH INSERT using JDBC API

### Pre-conditions :heavy_exclamation_mark:
You're supposed to be familiar with SQL and relational databases, and be able to write Java code. 
##

**Batch insert** it's an operation that allows to **insert more than one row using one INSERT statement.** :star:

#### SQL
A typical insert sql query looks like this: 

```sql
INSERT INTO products(name, producer) VALUES('Snickers', 'Mars Inc');
```

We usually perform one INSERT statement to store one row. However, in some cases such approach is not efficient :-1:

**Suppose you need to store large amount of products** at the same time. Like you need to store 100 000 products. It means 
that you need to call database 100 000 which is super inefficient, because each SQL query needs to at least go through 
the network from the server to the database, and it needs to do it 100 000 times :scream_cat:

In order to make such operation more efficient relational database and SQL provide an ability to **insert multiple rows 
in one INSERT statement.** :thumbsup: The sql query looks like the following:

```sql
INSERT INTO products(name, producer) VALUES ('Snickers', 'Mars Inc'), ('Fanta', 'The Coca-Cola company'), ('Bueno', 'Ferrero S.p.A.');
```

This approach allow to tremendously reduce the amount of database calls :+1:

Using batch INSERT you can split all products, and insert them using batches. In case a **batch size is 1000**, the **number
of database calls would be only 100** :smiley_cat: 

#### JDBC API
JDBC API provides two basic methods to perform batch insert:
- `PreparedStatement#addBatch()` that adds new row data to the existing batch
- `Statement#executeBatch()` that calls the database to perform an SQL and insert all data stored in the batch

Instead of executing query each time
```java
for (int i = 0; i < PRODUCTS_NUMBER; i++) {
    // get prepared statement        
    // get product 
    // set prepared statemnt parameters
    preparedStatemnt.executeUpdate(); // calls the database       
}
```

You can add new data to the batch:
```java
for (int i = 0; i < PRODUCTS_NUMBER; i++) {
    // get prepared statement        
    // get product 
    // set prepared statemnt parameters
    preparedStatemnt.addBatch(); // doesn't call the database
    
    if (i % BATCH_SIZE == 0) { 
        insertStatement.executeBatch(); // calls the database
    }       
}

// in case batch in not empty at the end of the for loop
if (i % BATCH_SIZE == 0) { 
    insertStatement.executeBatch(); // calls the database
}
```

### Best practices
* use batch INSERT for saving large amount of data 
* always measure performance 
* change batch size depending on situation