package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.util.DbUtil;

public class Main {
    public static void main(String[] args) {
        AccountDao accountDao = new AccountDaoImpl(DbUtil.getDataSource());

        accountDao.findAll().stream()
                .forEach(System.out::println);
    }
}
