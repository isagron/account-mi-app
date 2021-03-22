package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.models.Income;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IncomeRepository extends ReactiveMongoRepository<Income, String>, ExtendIncomeRepository {
}
