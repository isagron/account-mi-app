package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.domain.models.Income;
import com.isagron.accountmi_server.services.utils.DateUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ExtendIncomeRepositoryImpl implements ExtendIncomeRepository {

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Income> find(String accountId, String type, Integer month, Integer year) {
        final Query query = new Query();


        List<Criteria> criteriaList = new ArrayList<>();

        if (accountId != null) {
            criteriaList.add(Criteria.where(Income.Fields.accountId).is(accountId));
        }
        if (type != null) {
            criteriaList.add(Criteria.where(Income.Fields.type).is(type));
        }

        if (month != null && year != null) {
            DateRange dateRange = new DateRange(DateUtils.startOfMonth(month, year), DateUtils.endOfMonth(month, year));
            criteriaList.add(Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()));
            criteriaList.add(Criteria.where(Expense.Fields.date).lte(dateRange.getTo()));
        } else if (month != null) {
            //need to return all the result for specific month
            return findByMonth(month, criteriaList);
        } else if (year != null) {
            //need to return all the result for specific year
            DateRange dateRange = DateUtils.yearRange(year);
            criteriaList.add(Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()));
            criteriaList.add(Criteria.where(Expense.Fields.date).lte(dateRange.getTo()));
        }

        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])));
        return mongoTemplate.find(query, Income.class);
    }

    @Override
    public Mono<PageSupport<Income>> find(String accountId, String category, Integer month, Integer year, Pageable pageable) {
        return find(accountId, category, month, year)
                .collectList()
                .map(list -> {
                    double totalAmount = list.stream().mapToDouble(Income::getAmount).sum();

                    return new PageSupport<>(
                            list
                                    .stream()
                                    .skip(pageable.getPageNumber() * pageable.getPageSize())
                                    .limit(pageable.getPageSize())
                                    .collect(Collectors.toList()),
                            pageable.getPageNumber(), pageable.getPageSize(), list.size(), totalAmount);
                });


    }


    @Override
    public Flux<Income> findByMonth(int month, List<Criteria> criterion) {
        List<Criteria> criteriaList = new ArrayList<>(criterion);
        criteriaList.add(Criteria.where("month").is(month));

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project(Income.Fields.id, Income.Fields.accountId, Income.Fields.type,
                        Income.Fields.amount, Income.Fields.date).andExpression("month(date)").as("month"),
                Aggregation.match(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])))
        );
        return mongoTemplate.aggregate(agg, "income", Income.class);
    }

    @Override
    public Flux<String> findAllTypeByAccountId(String accountId) {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(Expense.Fields.accountId).is(accountId)),
                Aggregation.project(Income.Fields.type)
        );
        return mongoTemplate.aggregate(agg, "income", Document.class)
                .map(doc -> doc.getString(Income.Fields.type));
    }

    @Override
    public Flux<Integer> findAllYearsByAccountId(String accountId) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(Expense.Fields.accountId).is(accountId)),
                Aggregation.project().andExpression("year(date)").as("year")
        );
        return mongoTemplate.aggregate(agg, "income", Document.class).map(doc -> doc.getInteger("year"))
                .distinct();
    }

    @Override
    public Mono<Double> getTotalIncomeDateRange(String accountId, DateRange dateRange) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria
                                .where(Income.Fields.accountId).is(accountId)
                                .andOperator(
                                        Criteria.where(Income.Fields.date).gte(dateRange.getFrom()),
                                        Criteria.where(Income.Fields.date).lte(dateRange.getTo())
                                )
                ),
                Aggregation.group().sum(Income.Fields.amount).as("total")
        );
        return mongoTemplate.aggregate(agg, "income", Document.class)
                .map(doc -> doc.getDouble("total")).next();
    }
}
