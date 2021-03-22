package com.isagron.accountmi_server.domain.dao;

public interface ExtendAccountRepository {

    void addExpenseCategory(String accountId, String category);
}
