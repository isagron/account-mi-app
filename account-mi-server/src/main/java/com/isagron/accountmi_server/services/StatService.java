package com.isagron.accountmi_server.services;

import com.isagron.accountmi_api.dtos.account.AccountBalanceDto;
import com.isagron.accountmi_api.dtos.account.SingleBalanceMetricDto;
import com.isagron.accountmi_api.dtos.goal.GoalStatusDto;
import com.isagron.accountmi_api.dtos.statistics.CategoryToAmountDto;
import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.models.Goal;
import com.isagron.accountmi_server.services.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatService {

    private final static String ALL_CATEGORIES = "ALL";

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private AccountService accountService;

    public Mono<AccountBalanceDto> getAccountBalance(String accountId) {
        DateRange currentMonthDateRange = DateUtils.getCurrentMonthDateRange();
        DateRange prevMonthDateRange = DateUtils.getPrevMonthDateRange();
        DateRange currentYearDateRange = DateUtils.getCurrentYearDateRange();

        Mono<Double> totalIncomesForCurrentMonth = incomeService.getTotalIncomeDateRange(accountId, currentMonthDateRange);
        Mono<Double> totalIncomesForPrevMonth = incomeService.getTotalIncomeDateRange(accountId, prevMonthDateRange);
        Mono<Double> totalIncomeThisYear = incomeService.getTotalIncomeDateRange(accountId, currentYearDateRange);


        Mono<SingleBalanceMetricDto> incomesMetrics = Mono.zip(totalIncomesForCurrentMonth, totalIncomesForPrevMonth, totalIncomeThisYear)
                .map(totalExpenseInfo -> SingleBalanceMetricDto.builder()
                        .currentMonth(totalExpenseInfo.getT1())
                        .previousMonth(totalExpenseInfo.getT2())
                        .totalThisYear(totalExpenseInfo.getT3())
                        .build())
                .subscribeOn(Schedulers.elastic());


        Mono<Double> totalExpensesForCurrentMonth = expenseService.getTotalExpenseDateRange(accountId, currentMonthDateRange);
        Mono<Double> totalExpensesForPrevMonth = expenseService.getTotalExpenseDateRange(accountId, prevMonthDateRange);
        Mono<Double> totalExpenseThisYear = expenseService.getTotalExpenseDateRange(accountId, currentYearDateRange);

        Mono<SingleBalanceMetricDto> expenseMetrics = Mono.zip(totalExpensesForCurrentMonth, totalExpensesForPrevMonth, totalExpenseThisYear)
                .map(totalExpenseInfo -> SingleBalanceMetricDto.builder()
                        .currentMonth(totalExpenseInfo.getT1())
                        .previousMonth(totalExpenseInfo.getT2())
                        .totalThisYear(totalExpenseInfo.getT3())
                        .build())
                .subscribeOn(Schedulers.elastic());


        Mono<Double> balanceForCurrentMonth = totalIncomesForCurrentMonth
                .zipWith(totalExpensesForCurrentMonth)
                .map(incomeExpenseTuple -> {
                    Double first = incomeExpenseTuple.getT1() != null ? incomeExpenseTuple.getT1() : 0.0;
                    Double second = incomeExpenseTuple.getT2() != null ? incomeExpenseTuple.getT2() : 0.0;
                    return first - second;
                });

        Mono<Double> balanceForPrevMonth = totalIncomesForPrevMonth
                .zipWith(totalExpensesForPrevMonth)
                .map(incomeExpenseTuple -> {
                    Double first = incomeExpenseTuple.getT1() != null ? incomeExpenseTuple.getT1() : 0.0;
                    Double second = incomeExpenseTuple.getT2() != null ? incomeExpenseTuple.getT2() : 0.0;
                    return first - second;
                });

        Mono<Double> balanceForYear = totalIncomeThisYear
                .zipWith(totalExpenseThisYear)
                .map(incomeExpenseTuple -> {
                    Double first = incomeExpenseTuple.getT1() != null ? incomeExpenseTuple.getT1() : 0.0;
                    Double second = incomeExpenseTuple.getT2() != null ? incomeExpenseTuple.getT2() : 0.0;
                    return first - second;
                });

        Mono<SingleBalanceMetricDto> balanceMetrics = Mono.zip(balanceForCurrentMonth, balanceForPrevMonth, balanceForYear)
                .map(totalExpenseInfo -> SingleBalanceMetricDto.builder()
                        .currentMonth(totalExpenseInfo.getT1())
                        .previousMonth(totalExpenseInfo.getT2())
                        .totalThisYear(totalExpenseInfo.getT3())
                        .build())
                .subscribeOn(Schedulers.elastic());


        return Mono.zip(incomesMetrics, expenseMetrics, balanceMetrics)
                .map(accountBalanceData -> {
                    return AccountBalanceDto.builder()
                            .incomes(accountBalanceData.getT1())
                            .outcomes(accountBalanceData.getT2())
                            .balance(accountBalanceData.getT3())
                            .build();
                });
    }

    public Mono<SingleBalanceMetricDto> getAccountBalanceForMonth(String accountId, int month, int year) {
        DateRange currentMonthDateRange = DateUtils.getMonthDateRange(month, year);

        DateRange prevMonthDateRange = DateUtils.getPrevMonthDateRange(month, year);
        DateRange currentYearDateRange = DateUtils.yearRange(year);

        Mono<Double> totalExpensesForCurrentMonth = expenseService.getTotalExpenseDateRange(accountId, currentMonthDateRange)
                .switchIfEmpty(Mono.just(0.0));
        Mono<Double> totalExpensesForPrevMonth = expenseService.getTotalExpenseDateRange(accountId, prevMonthDateRange)
                .switchIfEmpty(Mono.just(0.0));
        Mono<Double> totalExpenseThisYear = expenseService.getTotalExpenseDateRange(accountId, currentYearDateRange)
                .switchIfEmpty(Mono.just(0.0));

        return Mono.zip(totalExpensesForCurrentMonth, totalExpensesForPrevMonth, totalExpenseThisYear)
                .map(totalExpenseInfo -> SingleBalanceMetricDto.builder()
                        .currentMonth(totalExpenseInfo.getT1())
                        .previousMonth(totalExpenseInfo.getT2())
                        .totalThisYear(totalExpenseInfo.getT3())
                        .build())
                .subscribeOn(Schedulers.elastic());

    }

    public Flux<GoalStatusDto> getGoalStatus(String accountId, int month, int year) {

        Flux<Goal> goals = accountService.getGoals(accountId);

        Mono<Map<String, Double>> expensePerMonthMapByCategory = expenseService.getExpenseAmountGroupByCategoryForMonth(
                accountId, month, year
        );

        Mono<Map<String, Double>> expenseAvgByCategory = expenseService.getExpenseAvgGroupByCategory(accountId, year);


        return goals.flatMap(goal ->
                Mono.zip(Mono.just(goal), expensePerMonthMapByCategory, expenseAvgByCategory)
                        .map(goalStatusData -> GoalStatusDto.builder()
                                .goalId(goal.getGoalId().toString())
                                .category(goal.getCategoryName())
                                .threshold(goal.getAmount())
                                .currentStatus(goalStatusData.getT2().getOrDefault(goal.getCategoryName(), 0.0))
                                .avg(goalStatusData.getT3().getOrDefault(goal.getCategoryName(), 0.0))
                                .build())

        );

    }

    public Flux<CategoryToAmountDto> getTotalExpensePerCategoryFor(String accountId, Integer month, int year) {

        Mono<Map<String, Double>> expensePerCategory  = null;
        if (month == null){
             expensePerCategory = this.expenseService.getExpenseAmountGroupByCategoryForYear(accountId, year);
        }
        else {
            expensePerCategory = expenseService.getExpenseAmountGroupByCategoryForMonth(accountId, month, year);
        }

        return expensePerCategory
                .flatMapMany(categoryToAmount -> Flux.fromIterable(
                        categoryToAmount.entrySet().stream()
                                .map(entry -> CategoryToAmountDto.builder()
                                        .category(entry.getKey())
                                        .amount(entry.getValue())
                                        .build()).collect(Collectors.toList())
                ));
    }

    /**
     * Return the total amount spend on category @param for the last 12 month
     * @param accountId
     * @param category
     * @return
     */
    public Flux<CategoryToAmountDto> getTotalExpensePerCategoryLast12Month(String accountId, String category) {
        if (category == null){
            return expenseService.getTotalSpentInLast12Month(accountId);
        }
        return expenseService.getTotalSpentInLast12MonthForCategory(accountId, category);
    }
}
