package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.model.Account;
import com.bobocode.util.DbUtil;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());

//        List<Account> accounts = TestDataProvider.generateAccountList(100).stream()
//                .peek(accountDao::save)
//                .collect(toList());

        accountDao.findAll().forEach(System.out::println);

        Account tempAccount = accountDao.find(100L);
        System.out.println("\nFind last entity: " + tempAccount);

        tempAccount.setFirstName("Kyiv");
        accountDao.update(tempAccount);

        tempAccount = accountDao.find(tempAccount.getId());
        System.out.println("\nFind update entity: " + tempAccount);
    }
}
