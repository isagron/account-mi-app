package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.services.elements.ExpenseCategoryToAmount;
import com.isagron.accountmi_server.services.utils.DateUtils;
import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ExtendExpenseRepositoryImpl implements ExtendExpenseRepository {
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Expense> find(String accountId, String category, Integer month, Integer year) {
        final Query query = new Query();


        List<Criteria> criteriaList = new ArrayList<>();

        if (accountId != null) {
            criteriaList.add(Criteria.where(Expense.Fields.accountId).is(accountId));
        }
        if (category != null) {
            criteriaList.add(Criteria.where(Expense.Fields.category).is(category));
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
        return mongoTemplate.find(query, Expense.class);
    }


    @Override
    public Mono<PageSupport<Expense>> find(String accountId, String category, Integer month, Integer year, Pageable pageable) {
        return find(accountId, category, month, year)
                .collectList()
                .map(list -> new PageSupport<>(
                        list
                                .stream()
                                .skip(pageable.getPageNumber() * pageable.getPageSize())
                                .limit(pageable.getPageSize())
                                .collect(Collectors.toList()),
                        pageable.getPageNumber(), pageable.getPageSize(), list.size()));


    }

    @Override
    public Flux<Expense> findByMonth(int month, List<Criteria> criterion) {
        List<Criteria> criteriaList = new ArrayList<>(criterion);
        criteriaList.add(Criteria.where("month").is(month));

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project(Expense.Fields.id, Expense.Fields.accountId, Expense.Fields.category,
                        Expense.Fields.amount, Expense.Fields.date).andExpression("month(date)").as("month"),
                Aggregation.match(new Criteria().andOperator(criteriaList.toArray(new Criteria[criteriaList.size()])))
        );
        return mongoTemplate.aggregate(agg, "expense", Expense.class);
    }

    @Override
    public Flux<String> findAllStores(String accountId) {
        Query query = new Query(Criteria.where(Expense.Fields.accountId).is(accountId));
        return mongoTemplate.findDistinct(query, Expense.Fields.store, Expense.class, String.class);
    }

    @Override
    public Flux<Integer> getExpensesYears(String accountId) {

        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(Criteria.where(Expense.Fields.accountId).is(accountId)),
                Aggregation.project().andExpression("year(date)").as("year")
        );
        return mongoTemplate.aggregate(agg, "expense", Document.class)
                .map(doc -> doc.getInteger("year"))
                .filter(year -> year != null)
                .distinct();

    }

    @Override
    public Mono<Double> getTotalExpenseDateRange(String accountId, DateRange dateRange) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(
                        Criteria
                                .where(Expense.Fields.accountId).is(accountId)
                                .andOperator(
                                        Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()),
                                        Criteria.where(Expense.Fields.date).lte(dateRange.getTo())
                                )
                ),
                Aggregation.group().sum(Expense.Fields.amount).as("total")
        );
        return mongoTemplate.aggregate(agg, "expense", Document.class)
                .map(doc -> doc.getDouble("total")).next();
    }

    @Override
    public Flux<ExpenseCategoryToAmount> getTotalExpensePerCategoryForMonth(String accountId, int month, int year) {
        DateRange monthRange = DateUtils.getMonthDateRange(month, year);
        return getTotalExpensePerCategoryForMonth(accountId, monthRange);
    }



    @Override
    public Flux<ExpenseCategoryToAmount> getTotalExpensePerCategoryForYear(String accountId, int year) {
        DateRange dateRange = DateUtils.yearRange(year);
        return getTotalExpensePerCategoryForMonth(accountId, dateRange);
    }

    @Override
    public Flux<ExpenseCategoryToAmount> getTotalExpensePerCategoryForMonth(String accountId, DateRange dateRange) {


        GroupOperation groupByCategoryTotalAmount = Aggregation.group(Expense.Fields.category)
                .sum(Expense.Fields.amount).as(ExpenseCategoryToAmount.Fields.total);
        MatchOperation match = Aggregation.match(
                Criteria
                        .where(Expense.Fields.accountId).is(accountId)
                        .andOperator(
                                Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()),
                                Criteria.where(Expense.Fields.date).lte(dateRange.getTo())
                        )
        );
        Aggregation aggregation = Aggregation.newAggregation(
                match, groupByCategoryTotalAmount
        );
        return mongoTemplate.aggregate(aggregation, "expense", ExpenseCategoryToAmount.class);
    }

    @Override
    public Flux<DBObject> getTotalAmountPerCategoryAndMonthForYear(String accountId, int year){

        DateRange dateRange = DateUtils.yearRange(year);

        return getTotalAmountPerCategoryAndMonth(accountId, dateRange);

    }

    @Override
    public Flux<DBObject> getTotalAmountPerCategoryAndMonth(String accountId, DateRange dateRange){
        MatchOperation match = Aggregation.match(
                Criteria
                        .where(Expense.Fields.accountId).is(accountId)
                        .andOperator(
                                Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()),
                                Criteria.where(Expense.Fields.date).lte(dateRange.getTo())
                        )
        );

        ProjectionOperation project = Aggregation.project(Expense.Fields.category, Expense.Fields.amount)
                .andExpression("month(date)").as("month");

        GroupOperation groupByCategoryAvg = Aggregation.group(Expense.Fields.category, "month")
                .sum(Expense.Fields.amount).as(ExpenseCategoryToAmount.Fields.total);

        Aggregation agg = Aggregation.newAggregation(match, project, groupByCategoryAvg);

        return mongoTemplate.aggregate(agg, "expense", DBObject.class);


    }

    @Override
    public Flux<Document> getTotalSpentForCategoryPerMonth(String accountId, String category, DateRange dateRange) {
        MatchOperation match = Aggregation.match(
                Criteria
                        .where(Expense.Fields.accountId).is(accountId)
                        .andOperator(
                                Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()),
                                Criteria.where(Expense.Fields.date).lte(dateRange.getTo()),
                                Criteria.where(Expense.Fields.category).is(category)
                        )
        );

        ProjectionOperation project = Aggregation.project(Expense.Fields.category, Expense.Fields.amount).andExpression("year(date)").as("year")
                .andExpression("month(date)").as("date");

        GroupOperation groupAndSum = Aggregation.group(Expense.Fields.category, "month", "year")
                .sum(Expense.Fields.amount).as(ExpenseCategoryToAmount.Fields.total);

        Aggregation agg = Aggregation.newAggregation(match, project, groupAndSum);

        return mongoTemplate.aggregate(agg, "expense", Document.class);

    }

    @Override
    public Flux<Document> getTotalSpentPerMonth(String accountId, DateRange dateRange) {
        MatchOperation match = Aggregation.match(
                Criteria
                        .where(Expense.Fields.accountId).is(accountId)
                        .andOperator(
                                Criteria.where(Expense.Fields.date).gte(dateRange.getFrom()),
                                Criteria.where(Expense.Fields.date).lte(dateRange.getTo())
                        )
        );

        ProjectionOperation project = Aggregation.project(Expense.Fields.amount).andExpression("year(date)").as("year")
                .andExpression("month(date)").as("month");

        GroupOperation groupAndSum = Aggregation.group("month", "year")
                .sum(Expense.Fields.amount).as(ExpenseCategoryToAmount.Fields.total);

        Aggregation agg = Aggregation.newAggregation(match, project, groupAndSum);

        return mongoTemplate.aggregate(agg, "expense", Document.class);
    }


}