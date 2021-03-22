package com.isagron.accountmi_server.domain.dao;


import com.isagron.accountmi_server.domain.models.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    @Query(value = "{email: ?0}", exists = true)
    Mono<Boolean> isUserExistByEmail(String email);
}
