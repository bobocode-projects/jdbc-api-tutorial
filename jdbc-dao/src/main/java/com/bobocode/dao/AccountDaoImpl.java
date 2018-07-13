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
            PreparedStatement insertStatement = prepareInsertStatement(connection, account);

            executeInsert(insertStatement);
            Long id = fetchGeneratedId(insertStatement);
            account.setId(id);
        } catch (SQLException e) {
            throw new DaoOperationException(e.getMessage());
        }
    }

    private PreparedStatement prepareInsertStatement(Connection connection, Account account) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO" +
                        " account(first_name,last_name,email,birthday,balance,creation_date) VALUES(?,?,?,?,?,?);"
                , PreparedStatement.RETURN_GENERATED_KEYS);

        preparedStatement.setString(1, account.getFirstName());
        preparedStatement.setString(2, account.getLastName());
        preparedStatement.setString(3, account.getEmail());
        preparedStatement.setDate(4, Date.valueOf(account.getBirthday()));
        preparedStatement.setBigDecimal(5, account.getBalance());
        preparedStatement.setDate(6, Date.valueOf(account.getCreationDate().toLocalDate()));

        return preparedStatement;
    }

    private void executeInsert(PreparedStatement insertStatement) throws SQLException {
        int rowsAffected = insertStatement.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Account was not created");
        }
    }

    private Long fetchGeneratedId(PreparedStatement insertStatement) throws SQLException {
        ResultSet generatedKeys = insertStatement.getGeneratedKeys();

        if (generatedKeys.next()) {
            return generatedKeys.getLong(1);
        } else {
            throw new SQLException("Can not obtain an account ID");
        }
    }

    @Override
    public Account findOne(Long id) {
        throw new UnsupportedOperationException("Method is not implemented yet. It's your homework");
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
        throw new UnsupportedOperationException("Method is not implemented yet. It's your homework");
    }

    private Account parseRow(ResultSet rs) throws SQLException {
        Account account = new Account();

        account.setId(rs.getLong(1));
        account.setFirstName(rs.getString(2));
        account.setLastName(rs.getString(3));
        account.setEmail(rs.getString(4));
        account.setBirthday(rs.getDate(5).toLocalDate());
        account.setBalance(rs.getBigDecimal(6));
        account.setCreationDate(rs.getTimestamp(7).toLocalDateTime());

        return account;
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
