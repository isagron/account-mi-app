package com.isagron.accountmi_server.test_cases_data;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public class DataLoader {


    public static <T, ID> void loadData(ReactiveMongoRepository<T, ID> expenseRepository, List<T> data) {
        expenseRepository.deleteAll()
                .thenMany(Flux.fromIterable(data))
                .flatMap(expenseRepository::save)
                .doOnNext(expense -> System.out.println("Inserted expense is: " + expense))
                .blockLast();
    }
}
