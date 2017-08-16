package com.bobocode;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Basic JDBC API example. This is the simples example of using JDBC API. Please note that Java SE includes JDBC API,
 * but you have to add appropriate JDBC Driver to run this code.
 *
 * Minimal required properties
 * 1. Database url
 * 2. Username
 * 3. Password
 *
 * The shortest flow of calling database using JDBC API
 * - Create DataSource
 * - Get connection
 * - Create statement
 * - Execute statement
 */
public class PrintDatabaseTables {
    //todo: don't forget to specify your db properties
    static String url = "jdbc:postgresql://localhost:5432/bobocode_db";
    static String user = "postgres";
    static String pass = "qwerty";

    public static void main(String[] args) throws SQLException {

        DataSource dataSource = createDataSource(url, user, pass);

        printPublicTables(dataSource);
    }

    private static void printPublicTables(DataSource dataSource) throws SQLException {
        //try-with-resource will automatically close Connection resource
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE';";

            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        }
    }

    private static DataSource createDataSource(String url, String username, String pass) {
        // Creates simple basic data source with one physical connection
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(username);
        dataSource.setPassword(pass);
        return dataSource;
    }
}
