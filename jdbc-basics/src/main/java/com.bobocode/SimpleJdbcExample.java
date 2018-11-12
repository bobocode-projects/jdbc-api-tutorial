package com.bobocode;

import com.bobocode.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.*;

public class SimpleJdbcExample {
    private static final String CREATE_TABLE_SQL = "CREATE TABLE message(" +
            "body VARCHAR(255)," +
            "creation_date TIMESTAMP DEFAULT now()" +
            ");";
    private static final String INSERT_SQL = "INSERT INTO message(body) VALUES (?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM message";


    private static DataSource dataSource;

    public static void main(String[] args) throws SQLException { // exception handling is omitted
        init();
        createMessageTable();
        saveSomeMessagesIntoDB();
        printMessagesFromDB();
    }

    private static void init() {
        dataSource = JdbcUtil.createDefaultInMemoryH2DataSource();
    }

    private static void createMessageTable() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE_SQL);
        }

    }

    private static void saveSomeMessagesIntoDB() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_SQL);
            insertSomeMessages(insertStatement);
        }
    }

    private static void insertSomeMessages(PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, "Hello!");
        insertStatement.executeUpdate();

        insertStatement.setString(1, "How are you?");
        insertStatement.executeUpdate();
    }

    private static void printMessagesFromDB() throws SQLException {
        //try-with-resource will automatically close Connection resource
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_SQL);
            printAllMessages(resultSet);
        }
    }

    private static void printAllMessages(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            String messageText = resultSet.getString(1);
            Timestamp timestamp = resultSet.getTimestamp(2);
            System.out.println(" - " + messageText + " [" + timestamp + "]");
        }
    }
}
