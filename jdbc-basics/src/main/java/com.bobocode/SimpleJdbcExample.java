package com.bobocode;

import com.bobocode.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.*;

public class SimpleJdbcExample {
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
            String createMessageTableQuery = getCreateMessageTablesSql();
            statement.execute(createMessageTableQuery);
        }
    }

    private static String getCreateMessageTablesSql() {
        throw new UnsupportedOperationException("It's your task to write a proper SQL statement");
    }

    private static void saveSomeMessagesIntoDB() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            String insertQuery = getInsertMessageSql();
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertSomeMessages(insertStatement);
        }
    }

    private static void insertSomeMessages(PreparedStatement insertStatement) throws SQLException {
        insertStatement.setString(1, "Hello!");
        insertStatement.executeUpdate();

        insertStatement.setString(1, "How are you?");
        insertStatement.executeUpdate();
    }

    private static String getInsertMessageSql() {
        throw new UnsupportedOperationException("It's your task to write a proper SQL statement");
    }

    private static void printMessagesFromDB() throws SQLException {
        //try-with-resource will automatically close Connection resource
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String selectMessagesQuery = getSelectAllMessagesSql();
            ResultSet resultSet = statement.executeQuery(selectMessagesQuery);
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

    private static String getSelectAllMessagesSql() {
        throw new UnsupportedOperationException("It's your task to write a proper SQL statement");
    }
}
