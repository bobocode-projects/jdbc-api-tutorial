package com.bobocode.dao;

import com.bobocode.model.Account;

import java.util.List;

public interface AccountDao {
    void save(Account account);

    Account findOne(Long id);

    List<Account> findAll();

    void update(Account account);
}
