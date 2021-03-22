package com.isagron.accountmi_server.controllers;

import com.isagron.accountmi_api.apis.v1.external.StatisticApi;
import com.isagron.accountmi_api.dtos.account.AccountBalanceDto;
import com.isagron.accountmi_api.dtos.goal.GoalStatusDto;
import com.isagron.accountmi_api.dtos.statistics.CategoryToAmountDto;
import com.isagron.accountmi_server.services.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(StatisticApi.Path.STATISTICS_PATH)
public class StatController {

    @Autowired
    private StatService statService;

    @GetMapping(StatisticApi.Path.STATISTICS_ACCOUNT_BALANCE_PATH)
    public Mono<AccountBalanceDto> getAccountBalance(@RequestParam(StatisticApi.PathVar.ACCOUNT_ID) String accountId) {
        return statService.getAccountBalance(accountId);
    }

    @GetMapping(StatisticApi.Path.GOAL_STATUS_PATH)
    public Flux<GoalStatusDto> getGoalStatus(@RequestParam(StatisticApi.PathVar.ACCOUNT_ID) String accountId,
                                             @RequestParam(StatisticApi.PathVar.MONTH) int month,
                                             @RequestParam(StatisticApi.PathVar.YEAR) int year
    ) {
        return statService.getGoalStatus(accountId, month, year);
    }

    @GetMapping(StatisticApi.Path.CATEGORY_DIVISION_FOR_MONTH_PATH)
    public Flux<CategoryToAmountDto> getExpenseCategoryPerMonth(@RequestParam(StatisticApi.PathVar.ACCOUNT_ID) String accountId,
                                                                @RequestParam(value = StatisticApi.PathVar.MONTH, required = false) Integer month,
                                                                @RequestParam(StatisticApi.PathVar.YEAR) int year
    ) {
        return statService.getTotalExpensePerCategoryFor(accountId, month, year);
    }

    @GetMapping(StatisticApi.Path.CATEGORY_EXPENSE_PER_MONTH_PATH)
    public Flux<CategoryToAmountDto> getTotalExpensePerCategoryForMonth(@RequestParam(StatisticApi.PathVar.ACCOUNT_ID) String accountId,
                                                                @RequestParam(value = StatisticApi.PathVar.CATEGORY, required = false) String category
    ) {
        return statService.getTotalExpensePerCategoryLast12Month(accountId, category);
    }
}
