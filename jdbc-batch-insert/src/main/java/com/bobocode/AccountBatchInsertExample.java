package com.bobocode;

import com.bobocode.exception.AccountBatchInsertException;
import com.bobocode.model.Account;
import com.bobocode.model.Gender;
import com.bobocode.util.JdbcAccountUtil;
import com.bobocode.util.JdbcUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/**
 * {@link AccountBatchInsertExample} provides an example of BATCH INSERT using JDBC API.
 * <p>
 * {@link AccountBatchInsertExample#init()} creates in-memory database.
 * <p>
 * Then there are two methods that do completely the
 * same. Both methods store generated accounts into the database. The first one {@link AccountBatchInsertExample#saveAccountsUsingRegularInsert()}
 * is a simple insert that call the database each time to perform INSERT query for each account.
 * <p>
 * The second one {@link AccountBatchInsertExample#saveAccountsUsingBatchInsert()} stores generated accounts using
 * BATCH INSERT. E.g. it creates batch insert query, and sends it to the database every each {@link AccountBatchInsertExample#BATCH_SIZE}
 * accounts.
 * <p>
 * It means that if ACCOUNT_NUMBER = 100_000, and BATCH_SIZE = 1000,
 * regular insert will execute 100_000 SQL queries,
 * batch insert will execute 100 SQL queries
 */
public class AccountBatchInsertExample {
    private static DataSource dataSource;
    private static final int ACCOUNT_NUMBER = 100_000;
    private static final int BATCH_SIZE = 1000;
    private static final String ACCOUNT_INSERT_SQL =
            "INSERT INTO account(first_name, last_name, email, birthday, sex, balance) VALUES(?,?,?,?,?,?);";

    public static void main(String[] args) throws SQLException {
        init();
        saveAccountsUsingRegularInsert();
        saveAccountsUsingBatchInsert();
    }

    public static void init() {
        dataSource = JdbcUtil.createDefaultInMemoryH2DataSource();
        JdbcAccountUtil.createAccountTable(dataSource);
    }

    private static void saveAccountsUsingRegularInsert() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            System.out.printf("Insert %d accounts into the database using regular INSERT: ", ACCOUNT_NUMBER);
            Runnable saveAccountUsingRegularInsert = saveAccountsUsingRegularInsertRunnable(connection);
            long millis = performCountingTimeInMillis(saveAccountUsingRegularInsert);
            System.out.printf("%d ms%n", millis);
        }
    }

    /**
     * Creates a {@link Runnable} that holds the logic of generating and saving accounts using regular insert.
     * We need {@link Runnable} to pass it to {@link AccountBatchInsertExample#performCountingTimeInMillis(Runnable)}
     * because we want to calculate the execution time.
     *
     * @return runnable object that does regular insert
     */
    private static Runnable saveAccountsUsingRegularInsertRunnable(Connection connection) {
        return () -> {
            try {
                PreparedStatement insertStatement = connection.prepareStatement(ACCOUNT_INSERT_SQL);
                performRegularInsert(insertStatement);
            } catch (SQLException e) {
                throw new AccountBatchInsertException("Cannot perform regular account insert", e);
            }

        };
    }

    private static long performCountingTimeInMillis(Runnable runnable) {
        long startingTime = System.nanoTime();
        runnable.run();
        return (System.nanoTime() - startingTime) / 1_000_000;
    }

    private static void performRegularInsert(PreparedStatement insertStatement) throws SQLException {
        for (int i = 0; i < ACCOUNT_NUMBER; i++) {
            Account account = generateAccount();
            fillStatementParameters(insertStatement, account);
            insertStatement.executeUpdate(); // on each step we call the database sending INSERT query
        }
    }

    private static Account generateAccount() {
        Account fakeAccount = new Account();
        fakeAccount.setFirstName(RandomStringUtils.randomAlphabetic(20));
        fakeAccount.setLastName(RandomStringUtils.randomAlphabetic(20));
        fakeAccount.setEmail(RandomStringUtils.randomAlphabetic(20));
        fakeAccount.setGender(Gender.values()[RandomUtils.nextInt(1, 3)]);
        fakeAccount.setBalance(BigDecimal.valueOf(RandomUtils.nextInt(500, 200_000)));
        fakeAccount.setBirthday(LocalDate.now().minusDays(RandomUtils.nextInt(6000, 18000)));
        return fakeAccount;
    }

    private static void fillStatementParameters(PreparedStatement ps, Account account) throws SQLException {
        ps.setString(1, account.getFirstName());
        ps.setString(2, account.getLastName());
        ps.setString(3, account.getEmail());
        ps.setDate(4, Date.valueOf(account.getBirthday()));
        ps.setInt(5, account.getGender().getValue());
        ps.setBigDecimal(6, account.getBalance());
    }

    private static void saveAccountsUsingBatchInsert() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            System.out.printf("Insert %d accounts into the database using BATCH INSERT: ", ACCOUNT_NUMBER);
            Runnable saveAccountUsingBatch = saveAccountsUsingBatchInsertRunnable(connection);
            long millis = performCountingTimeInMillis(saveAccountUsingBatch);
            System.out.printf("%d ms%n", millis);
        }

    }

    /**
     * Creates a {@link Runnable} that holds the logic of generating and saving accounts using batch insert.
     * We need {@link Runnable} to pass it to {@link AccountBatchInsertExample#performCountingTimeInMillis(Runnable)}
     * because we want to calculate the execution time.
     *
     * @return runnable object that does regular insert
     */
    private static Runnable saveAccountsUsingBatchInsertRunnable(Connection connection) {
        return () -> {
            try {
                PreparedStatement insertStatement = connection.prepareStatement(ACCOUNT_INSERT_SQL);
                performBatchInsert(insertStatement);
            } catch (SQLException e) {
                throw new AccountBatchInsertException("Cannot perform account batch insert", e);
            }
        };
    }

    private static void performBatchInsert(PreparedStatement insertStatement) throws SQLException {
        for (int i = 1; i <= ACCOUNT_NUMBER; i++) {
            Account account = generateAccount();
            fillStatementParameters(insertStatement, account);
            insertStatement.addBatch(); // on each step we add a new account to the batch (IT DOESN'T CALL THE DATABASE)

            if (i % BATCH_SIZE == 0) { // every BATCH_SIZE accounts
                insertStatement.executeBatch(); // we perform the real SQL query to insert all account to the database
            }
        }
        executeRemaining(insertStatement);
    }

    private static void executeRemaining(PreparedStatement insertStatement) throws SQLException {
        if (ACCOUNT_NUMBER % BATCH_SIZE != 0) {
            insertStatement.executeBatch();
        }
    }

}
