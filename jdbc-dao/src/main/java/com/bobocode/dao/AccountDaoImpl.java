package com.bobocode.dao;

import com.bobocode.exception.DaoOperationException;
import com.bobocode.model.Account;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    private DataSource dataSource;

    public AccountDaoImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Account account) {
        try (Connection connection = dataSource.getConnection()) {
            saveAccount(account, connection);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage(), e);
        }
    }

    private void saveAccount(Account account, Connection connection) throws SQLException {
        PreparedStatement insertStatement = prepareInsertStatement(connection, account);
        executeUpdate(insertStatement, "Account was not created");
        Long id = fetchGeneratedId(insertStatement);
        account.setId(id);
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Account account) {
        try {
            String insertQuery = getInsertAccountSql();
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            return fillStatementWithAccountData(insertStatement, account);
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to insert account", e);
        }
    }

    private PreparedStatement fillStatementWithAccountData(PreparedStatement insertStatement, Account account)
            throws SQLException {
        insertStatement.setString(1, account.getFirstName());
        insertStatement.setString(2, account.getLastName());
        insertStatement.setString(3, account.getEmail());
        insertStatement.setDate(4, Date.valueOf(account.getBirthday()));
        insertStatement.setBigDecimal(5, account.getBalance());
        return insertStatement;
    }

    private String getInsertAccountSql() {
        return "INSERT INTO account(first_name, last_name, email, birthday, balance) VALUES(?,?,?,?,?);";
    }

    private void executeUpdate(PreparedStatement insertStatement, String errorMessage) throws SQLException {
        int rowsAffected = insertStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new DaoOperationException(errorMessage);
        }
    }

    private Long fetchGeneratedId(PreparedStatement insertStatement) throws SQLException {
        ResultSet generatedKeys = insertStatement.getGeneratedKeys();

        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new DaoOperationException("Can not obtain an account ID");
        }
    }

    @Override
    public Account findOne(Long id) {
        try (Connection connection = dataSource.getConnection()) {
            return findAccountById(id, connection);
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot find Account by id = %d", id), e);
        }
    }

    private Account findAccountById(Long id, Connection connection) throws SQLException {
        PreparedStatement selectByIdStatement = prepareSelectByIdStatement(id, connection);
        ResultSet resultSet = selectByIdStatement.executeQuery();
        resultSet.next();
        return parseRow(resultSet);
    }

    private PreparedStatement prepareSelectByIdStatement(Long id, Connection connection) {
        try {
            String selectByIdQuery = getSelectByIdSlq();
            PreparedStatement selectByIdStatement = connection.prepareStatement(selectByIdQuery);
            selectByIdStatement.setLong(1, id);
            return selectByIdStatement;
        } catch (SQLException e) {
            throw new DaoOperationException("Cannot prepare statement to select account by id", e);
        }
    }

    private String getSelectByIdSlq() {
        return "SELECT * FROM account WHERE account.id = ?;";
    }

    private Account parseRow(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong(1));
        account.setFirstName(rs.getString(2));
        account.setLastName(rs.getString(3));
        account.setEmail(rs.getString(4));
        account.setBirthday(rs.getDate(5).toLocalDate());
        account.setBalance(rs.getBigDecimal(6));
        account.setCreationTime(rs.getTimestamp(7).toLocalDateTime());
        return account;
    }

    @Override
    public List<Account> findAll() {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM account;");
            return collectToList(rs);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    @Override
    public void update(Account account) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement updateStatement = prepareUpdateStatement(account, connection);
            executeUpdate(updateStatement, "Account was not updated");
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot update Account with id = %d", account.getId()), e);
        }
    }

    private PreparedStatement prepareUpdateStatement(Account account, Connection connection) {
        try {
            String updateQuery = getUpdateAccountSql();
            PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
            return fillStatementWithAccountData(updateStatement, account);
        } catch (SQLException e) {
            throw new DaoOperationException(String.format("Cannot prepare update statement for account id = %d", account.getId()), e);
        }
    }

    private String getUpdateAccountSql() {
        return "UPDATE account SET first_name =?, last_name = ?, email = ?, birthday = ?, balance = ?;";
    }

    private List<Account> collectToList(ResultSet rs) throws SQLException {
        List<Account> accountList = new ArrayList<>();
        while (rs.next()) {
            Account account = parseRow(rs);
            accountList.add(account);
        }

        return accountList;
    }
}
