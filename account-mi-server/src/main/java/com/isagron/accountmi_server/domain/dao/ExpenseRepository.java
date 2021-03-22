package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.models.Expense;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ExpenseRepository  extends ReactiveMongoRepository<Expense, String>, ExtendExpenseRepository{

    Mono<Void> deleteByAccountIdAndCategory(String accountId, String categoryName);
}
