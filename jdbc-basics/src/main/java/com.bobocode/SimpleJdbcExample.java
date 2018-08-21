package com.bobocode;

import com.bobocode.util.JdbcUtil;

import javax.sql.DataSource;
import java.sql.*;

public class SimpleJdbcExample {
    private static DataSource dataSource;

    public static void main(String[] args) throws SQLException { // exception handling is omitted due to simplicity
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
            String sql = getCreateMessageTablesSql();
            statement.execute(sql);
        }
    }

    private static String getCreateMessageTablesSql() {
        return "CREATE TABLE message(" +
                "body VARCHAR(255)," +
                "creation_date TIMESTAMP DEFAULT now()" +
                ");";
    }

    private static void saveSomeMessagesIntoDB() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO message(body) VALUES (?)");

            statement.setString(1, "Hello!");
            statement.executeUpdate();

            statement.setString(1, "How are you?");
            statement.executeUpdate();
        }

    }

    private static void printMessagesFromDB() throws SQLException {
        //try-with-resource will automatically close Connection resource
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            String selectMessagesQuery = getSelectAllMessgesSql();
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

    private static String getSelectAllMessgesSql() {
        return "SELECT * FROM message";
    }
}
