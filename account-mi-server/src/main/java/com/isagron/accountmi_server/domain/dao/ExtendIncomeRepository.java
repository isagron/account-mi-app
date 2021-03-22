package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.domain.models.Income;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ExtendIncomeRepository {

    Flux<Income> find(String accountId, String type, Integer month, Integer year);

    Mono<PageSupport<Income>> find(String accountId, String type, Integer month, Integer year, Pageable pageable);

    Flux<Income> findByMonth(int month, List<Criteria> criterion);

    Flux<String> findAllTypeByAccountId(String accountId);

    Flux<Integer> findAllYearsByAccountId(String accountId);

    Mono<Double> getTotalIncomeDateRange(String accountId, DateRange dateRange);
}
