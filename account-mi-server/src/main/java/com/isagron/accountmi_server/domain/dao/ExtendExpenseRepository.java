package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.services.elements.ExpenseCategoryToAmount;
import com.mongodb.DBObject;
import com.mongodb.internal.async.client.AsyncMongoIterable;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExtendExpenseRepository {

    Flux<Expense> find(String accountId, String category, Integer month, Integer year);

    Mono<PageSupport<Expense>> find(String accountId, String category, Integer month, Integer year, Pageable pageable);
    Flux<Expense> findByMonth(int month, List<Criteria> criterion);

    List<String> findAllStores(String accountId);

    Flux<Integer> getExpensesYears(String accountId);
    Mono<Double> getTotalExpenseDateRange(String accountId, DateRange dateRange);


    Flux<ExpenseCategoryToAmount> getTotalExpensePerCategoryForMonth(String accountId, int month, int year);

    Flux<ExpenseCategoryToAmount> getTotalExpensePerCategoryForMonth(String accountId, DateRange dateRange);

    Flux<DBObject> getTotalAmountPerCategoryAndMonthForYear(String accountId, int year);

    Flux<DBObject> getTotalAmountPerCategoryAndMonth(String accountId, DateRange dateRange);

    Flux<ExpenseCategoryToAmount> getTotalExpensePerCategoryForYear(String accountId, int year);

    Flux<Document> getTotalSpentForCategoryPerMonth(String accountId, String category, DateRange dateRange);
    Flux<Document> getTotalSpentPerMonth(String accountId, DateRange dateRange);
}
