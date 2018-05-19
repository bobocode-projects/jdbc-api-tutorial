package com.bobocode;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Date;

public class SimpleJdbcExample {
    private static String database = "bobocode_db";
    private static String user = "bobouser";
    private static String pass = "bobodpass";
    private static DataSource dataSource;

    public static void main(String[] args) throws SQLException {
        initializeDataSource();

        createMessageTable();

        saveSomeMessagesIntoDB();

        printMessagesFromDB();
    }

    private static void initializeDataSource() {
        JdbcDataSource h2DataSource = new JdbcDataSource();
        h2DataSource.setUser(user);
        h2DataSource.setPassword(pass);
        h2DataSource.setUrl(String.format("jdbc:h2:mem:%s;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false", database));

        dataSource = h2DataSource;
    }

    private static void createMessageTable() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE message(" +
                    "body VARCHAR(255)," +
                    "creation_date TIMESTAMP DEFAULT now()" +
                    ");";
            statement.execute(sql);
        }
    }

    private static void saveSomeMessagesIntoDB() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO message(body) VALUES (?)");

            statement.setString(1, "Hello!");
            statement.executeUpdate();

            statement.setString(1,"How are you?");
            statement.executeUpdate();
        }

    }

    private static void printMessagesFromDB() throws SQLException {
        //try-with-resource will automatically close Connection resource
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String selectMessagesQuery = "SELECT * FROM message";
            ResultSet resultSet = statement.executeQuery(selectMessagesQuery);

            while (resultSet.next()) {
                String messageText = resultSet.getString(1);
                Timestamp timestamp = resultSet.getTimestamp(2);
                System.out.println(" - " + messageText + " [" + timestamp + "]");
            }
        }
    }
}
