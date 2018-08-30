package com.bobocode;

import com.bobocode.exception.JdbcTransactionExampleException;
import com.bobocode.util.JdbcUtil;
import org.apache.commons.lang3.RandomStringUtils;

import javax.sql.DataSource;
import java.sql.*;

public class JdbcTransactionManagementExample {
    private static final String CREATE_POST_TABLE_SQL = "CREATE TABLE post(id SERIAL PRIMARY KEY, message VARCHAR(255) NOT NULL)";
    private static final String INSERT_POST_SQL = "INSERT INTO post(message) VALUES (?)";
    private static final String COUNT_POSTS_SQL = "SELECT count(*) FROM post";
    private static final int POSTS_TO_INSERT_PER_OPERATION = 10;
    private static DataSource dataSource;

    public static void main(String[] args) {
        init();
        savePostsWithCommit(); // will generate posts insert them into the db, and commit changes
        savePostsWithRollBack(); // will generate posts insert them into the db, and revert changes
        printNumberOfPostsInThDb();
    }

    /**
     * Creates default in-memory H2 database and creates post table
     */
    private static void init() {
        dataSource = JdbcUtil.createDefaultInMemoryH2DataSource();
        createPostTable();
    }

    private static void createPostTable() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_POST_TABLE_SQL);
        } catch (SQLException e) {
            throw new JdbcTransactionExampleException("Error creating post database", e);
        }
    }

    /**
     * Generates posts and inserts it to the database. It turns of auto-commit mode at the beginning. It means that each
     * insert statement is performed in the scope of a single transaction. When all statements are executed it commits
     * the transaction
     */
    private static void savePostsWithCommit() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_POST_SQL)) {
                insertRandomPosts(insertStatement);
            }
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            throw new JdbcTransactionExampleException("Error saving random posts with commit", e);
        }
    }

    private static void insertRandomPosts(PreparedStatement insertStatement) throws SQLException {
        for (int i = 0; i < POSTS_TO_INSERT_PER_OPERATION; i++) {
            String randomText = RandomStringUtils.randomAlphabetic(20);
            insertStatement.setString(1, randomText);
            insertStatement.executeUpdate();
        }
    }

    /**
     * Generates posts and inserts it to the database. It turns of auto-commit mode at the beginning. It means that each
     * insert statement is performed in the scope of a single transaction. When all statements are executed it rollbacks
     * the transaction. The rollback will revert all changes made in the scope of this transaction, so no posts will
     * be stored.
     */
    private static void savePostsWithRollBack() {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement insertStatement = connection.prepareStatement(INSERT_POST_SQL)) {
                insertRandomPosts(insertStatement);
            }
            connection.rollback();
        } catch (SQLException e) {
            throw new JdbcTransactionExampleException("Error saving random posts with rollback", e);
        }
    }

    /**
     * Call the database to get the number of records in the post table. Prints the number of posts
     */
    private static void printNumberOfPostsInThDb() {
        try (Connection connection = dataSource.getConnection()) {
            int postsCount = countPosts(connection);
            System.out.printf("Number of posts in the database is %d%n", postsCount);
        } catch (SQLException e) {
            throw new JdbcTransactionExampleException("Error selecting number of posts in the database", e);
        }
    }

    private static int countPosts(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet countResultSet = statement.executeQuery(COUNT_POSTS_SQL);
        countResultSet.next();
        return countResultSet.getInt(1);
    }
}
