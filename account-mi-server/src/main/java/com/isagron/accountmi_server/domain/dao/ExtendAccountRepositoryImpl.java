package com.isagron.accountmi_server.domain.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class ExtendAccountRepositoryImpl implements ExtendAccountRepository{

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public void addExpenseCategory(String accountId, String category) {
        Update update = new Update();
        update.addToSet("expenseCategories", category);
        Criteria criteria = Criteria.where("_id").is(accountId);
        mongoTemplate.updateFirst(Query.query(criteria), update, "account");
    }
}
