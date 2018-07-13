package com.bobocode;

import com.bobocode.dao.AccountDao;
import com.bobocode.dao.AccountDaoImpl;
import com.bobocode.util.DbUtil;

import javax.sql.DataSource;

public class Main {
    private static AccountDao accountDao;

    public static void main(String[] args) {
        init();
        accountDao.findAll().stream()
                .forEach(System.out::println);
    }

    private static void init(){
        DataSource dataSource = DbUtil.createDefaultPostgresDataSource();
        accountDao = new AccountDaoImpl(dataSource);
    }
}
