package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface AccountRepository extends ReactiveMongoRepository<Account, String>, ExtendAccountRepository {
    Flux<Account> findByUserId(String userId);
    Flux<Account> findByUserIdAndActive(String userId, boolean active);
}
