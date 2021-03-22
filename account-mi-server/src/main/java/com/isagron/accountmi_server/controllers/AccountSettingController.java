package com.isagron.accountmi_server.controllers;

import com.isagron.accountmi_api.apis.v1.external.AccountSettingApi;
import com.isagron.accountmi_api.dtos.account.AccountDto;
import com.isagron.accountmi_api.dtos.account.AddCategoryRequest;
import com.isagron.accountmi_api.dtos.account.CreateAccountRequest;
import com.isagron.accountmi_api.dtos.account.UpdateAccountSettingRequest;
import com.isagron.accountmi_api.dtos.goal.GoalDto;
import com.isagron.accountmi_server.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.isagron.accountmi_api.apis.v1.external.AccountSettingApi.Path.*;

@RestController
@RequestMapping(AccountSettingApi.Path.ACCOUNTS_PATH)
public class AccountSettingController implements AccountSettingApi {

    @Autowired
    private AccountService accountService;


    @GetMapping
    public Flux<AccountDto> getAccounts(@RequestParam String userId, @RequestParam boolean active){
        return accountService.getAccount(userId, active);
    }

    @GetMapping(GET_OR_CREATE_PATH)
    public Mono<AccountDto> getOrCreateAccount(@RequestParam String userId){
        return accountService.getOrCreateEmptyAccount(userId);
    }

    @GetMapping(SINGLE_ACCOUNT_PATH)
    public Mono<AccountDto> getAccount(@PathVariable(PathVar.ACCOUNT_ID) String accountId){
        return accountService.getAccount(accountId);
    }

    @Override
    @PostMapping
    public Mono<AccountDto> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return accountService.createAccount(createAccountRequest);
    }

    @PostMapping(ACCOUNT_EXPENSE_CATEGORIES_PATH)
    public Mono<AccountDto> addExpenseCategory(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                        @RequestBody AddCategoryRequest request){
        return accountService.addExpenseCategory(accountId, request.getCategoryName());
    }

    @PostMapping(ACCOUNT_INCOME_CATEGORIES_PATH)
    public Mono<AccountDto> addIncomeCategory(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                               @RequestBody AddCategoryRequest request){
        return accountService.addIncomeCategory(accountId, request.getCategoryName());
    }

    @DeleteMapping(ACCOUNT_SINGLE_EXPENSE_CATEGORIES_PATH)
    public Mono<AccountDto> removeExpenseCategory(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                              @PathVariable(PathVar.CATEGORY_NAME) String category){
        return accountService.deleteExpenseCategory(accountId, category);
    }

    @DeleteMapping(ACCOUNT_SINGLE_INCOME_CATEGORIES_PATH)
    public Mono<AccountDto> removeIncomeCategory(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                                  @PathVariable(PathVar.CATEGORY_NAME) String category){
        return accountService.deleteIncomeCategory(accountId, category);
    }

    @GetMapping(ACCOUNT_GOALS_PATH)
    public Flux<GoalDto> getGoals(@PathVariable(PathVar.ACCOUNT_ID) String accountId){
        return accountService.getGoalsAsDto(accountId);
    }

    @PostMapping(ACCOUNT_GOALS_PATH)
    public Mono<AccountDto> addGoal(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                              @RequestBody GoalDto goal){
        return accountService.addGoal(accountId, goal);
    }

    @DeleteMapping(ACCOUNT_SINGLE_GOAL_PATH)
    public Mono<AccountDto> removeGoal(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                                  @PathVariable(PathVar.GOAL_ID) String goalId){
        return accountService.removeGoal(accountId, goalId);
    }

    @PutMapping(ACCOUNT_SINGLE_GOAL_PATH)
    public Mono<AccountDto> updateGoal(@PathVariable(PathVar.ACCOUNT_ID) String accountId,
                                       @PathVariable(PathVar.GOAL_ID) String goalId,
                                       @RequestBody GoalDto goalDto){
        return accountService.updateGoal(accountId, goalId, goalDto);
    }

    @Override
    @PutMapping(SINGLE_ACCOUNT_PATH)
    public AccountDto updateAccountSettings(@RequestBody UpdateAccountSettingRequest updateAccountSettingRequest) {
        return null;
    }




}
