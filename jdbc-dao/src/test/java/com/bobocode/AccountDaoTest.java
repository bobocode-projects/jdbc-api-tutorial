package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.data.Accounts;
import com.bobocode.model.Account;
import com.bobocode.util.JdbcUtil;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class AccountDaoTest {

    private static AccountDao accountDao;

    @BeforeClass
    public static void init() throws SQLException {
        DataSource h2DataSource = JdbcUtil.createDefaultInMemoryH2DataSource();
        createAccountTable(h2DataSource);
        accountDao = new AccountDaoImpl(h2DataSource);
    }

    private static void createAccountTable(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement createTableStatement = connection.createStatement();
            createTableStatement.execute("CREATE TABLE IF NOT EXISTS account (\n" +
                    "  id            SERIAL NOT NULL,\n" +
                    "  first_name     VARCHAR(255) NOT NULL,\n" +
                    "  last_name     VARCHAR(255) NOT NULL,\n" +
                    "  email         VARCHAR(255) NOT NULL,\n" +
                    "  birthday      TIMESTAMP NOT NULL,\n" +
                    "  sex      TINYINT NOT NULL DEFAULT 0 CHECK (sex >= 0 AND sex <=2) ,\n" +
                    "  balance       DECIMAL(19, 4),\n" +
                    "  creation_time TIMESTAMP NOT NULL DEFAULT now(),\n" +
                    "\n" +
                    "  CONSTRAINT account_pk PRIMARY KEY (id)\n" +
                    ");\n" +
                    "\n");
        }
    }

    @Test
    public void testSave() {
        Account account = Accounts.generateAccount();

        int accountsCountBeforeInsert = accountDao.findAll().size();
        accountDao.save(account);
        List<Account> accountList = accountDao.findAll();

        assertNotNull(account.getId());
        assertEquals(accountsCountBeforeInsert + 1, accountList.size());
        assertTrue(accountList.contains(account));
    }

    @Test
    public void testFindAll() {
        List<Account> generatedAccounts = Accounts.generateAccountList(10);
        generatedAccounts.stream().forEach(accountDao::save);

        List<Account> accountList = accountDao.findAll();

        assertTrue(accountList.containsAll(generatedAccounts));
    }

    @Test
    public void testFindById() {
        Account generatedAccount = Accounts.generateAccount();
        accountDao.save(generatedAccount);

        Account account = accountDao.findOne(generatedAccount.getId());

        assertEquals(generatedAccount, account);
    }

    @Test
    public void testUpdate() {
        Account generateAccount = Accounts.generateAccount();
        accountDao.save(generateAccount);
        BigDecimal balanceBeforeUpdate = generateAccount.getBalance();
        BigDecimal newBalance = balanceBeforeUpdate.add(BigDecimal.valueOf(15000)).setScale(2);

        generateAccount.setBalance(newBalance);
        accountDao.update(generateAccount);
        Account account = accountDao.findOne(generateAccount.getId());

        assertEquals(newBalance, generateAccount.getBalance().setScale(2));
        assertEquals(newBalance, account.getBalance().setScale(2));
    }
}
