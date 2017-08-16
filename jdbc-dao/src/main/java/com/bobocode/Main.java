package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.model.Account;
import com.bobocode.util.DbUtil;
import com.bobocode.util.TestDataProvider;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());
        Account account = TestDataProvider.generateFakeAccount();

        accountDao.save(account);
        System.out.println(account);
    }
}
