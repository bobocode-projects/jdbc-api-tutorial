package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.model.Account;
import com.bobocode.util.DbUtil;
import com.bobocode.util.TestDataProvider;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());

        //Add new fake account
        for(int i = 0; i < 4; i ++) {
            Long newAccountId = accountDao.save(TestDataProvider.generateFakeAccount());
            System.out.println("New " + accountDao.find(newAccountId).get());
            System.out.println("--------------------------------");
        }

        //Update existing account
        Long userIdForUpdate = 1L;
        Account accountForUpdate = accountDao.find(userIdForUpdate).get();
        accountForUpdate.setFirstName("NewName");
        accountDao.update(accountForUpdate);
        System.out.println("Updated " + accountDao.find(userIdForUpdate).get());
        System.out.println("--------------------------------");

        //Find specific account by user id
        System.out.println("Found by Id " + accountDao.find(50L).isPresent());
        System.out.println("--------------------------------");

        //Find all accounts
        accountDao.findAll().stream()
                .forEach(System.out::println);
        System.out.println("--------------------------------");
    }
}
