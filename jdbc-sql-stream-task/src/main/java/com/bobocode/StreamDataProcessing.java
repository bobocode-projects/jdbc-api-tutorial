package com.bobocode;


import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.model.Account;
import com.bobocode.util.DbUtil;

import java.util.List;

public class StreamDataProcessing {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());
        List<Account> accounts = accountDao.findAll();

        System.out.println("1 - Print all accounts");
        accounts.stream()
                .forEach(System.out::println);

        System.out.println("\n2 - Print all emails");
        accounts.stream()
                .map(Account::getEmail)
                .forEach(System.out::println);

        System.out.println("\n3 - Print all accounts, with balance more than 150000");
        accounts.stream()
                .filter(a -> a.getBalance().doubleValue() > 150000)
                .forEach(System.out::println);

    }
}
