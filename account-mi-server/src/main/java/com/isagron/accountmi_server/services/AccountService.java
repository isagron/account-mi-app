package com.isagron.accountmi_server.services;

import com.isagron.accountmi_api.dtos.account.AccountDto;
import com.isagron.accountmi_api.dtos.account.CreateAccountRequest;
import com.isagron.accountmi_api.dtos.goal.GoalDto;
import com.isagron.accountmi_server.domain.dao.AccountRepository;
import com.isagron.accountmi_server.domain.models.Account;
import com.isagron.accountmi_server.domain.models.Goal;
import com.isagron.accountmi_server.exceptions.DocumentNotFountException;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    @Lazy
    private ExpenseService expenseService;

    public Mono<AccountDto> createAccount(CreateAccountRequest createAccountRequest) {

        Account account = convertCreateRequestToAccount(createAccountRequest);
        this.accountRepository.findByUserIdAndActive(createAccountRequest.getUserId(), true)
                .flatMap(currentActiveAccount -> {
                    currentActiveAccount.setActive(false);
                    return this.accountRepository.save(currentActiveAccount);
                }).subscribe();
        return this.accountRepository.save(account)
                .map(this::convertToDto);
    }

    private AccountDto convertToDto(Account account) {
        return AccountDto.builder()
                .accountId(account.getId())
                .expenseCategories(account.getExpenseCategories())
                .incomeCategories(account.getIncomeCategories())
                .userId(account.getUserId())
                .accountBalance(account.getAccountBalance())
                .goals(convertToDtos(account.getGoals()))
                .build();
    }

    private Account convertCreateRequestToAccount(CreateAccountRequest createAccountRequest) {

        return Account.builder()
                .accountBalance(createAccountRequest.getAccountBalance())
                .expenseCategories(createAccountRequest.getExpenseCategories())
                .incomeCategories(createAccountRequest.getIncomeCategories())
                .userId(createAccountRequest.getUserId())
                .active(true)
                .build();



    }

    public Mono<AccountDto> getAccount(String accountId) {
        return this.accountRepository.findById(accountId)
                .map(this::convertToDto);
    }

    public Flux<AccountDto> getAccount(String userId, boolean active) {
        return this.accountRepository.findByUserIdAndActive(userId, active)
                .filter(account -> account.getUserId().equals(userId) && account.isActive()==active)
                .map(this::convertToDto);
    }

    public Mono<AccountDto> addExpenseCategory(String accountId, String categoryName) {
        return this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.addExpenseCategory(categoryName);
                    return this.accountRepository.save(account);
                })
                .map(this::convertToDto);
    }

    public Mono<AccountDto> addIncomeCategory(String accountId, String categoryName) {
        return this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.addIncomeCategory(categoryName);
                    return this.accountRepository.save(account);
                })
                .map(this::convertToDto);
    }

    public Mono<AccountDto> deleteIncomeCategory(String accountId, String categoryName) {
        return this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.getIncomeCategories().remove(categoryName);
                    return this.accountRepository.save(account);
                })
                .map(this::convertToDto);
    }

    public Mono<AccountDto> deleteExpenseCategory(String accountId, String categoryName) {
        return this.expenseService.deleteAllByAccountAndCategory(accountId, categoryName)
                .then(this.accountRepository.findById(accountId)
                        .flatMap(account -> {
                            account.getExpenseCategories().remove(categoryName);
                            return this.accountRepository.save(account);
                        })).map(this::convertToDto);
    }

    public Mono<AccountDto> addGoal(String accountId, GoalDto goal) {
        return this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    Goal newGoal = convertToEntity(goal);
                    account.addGoal(newGoal);
                    return this.accountRepository.save(account);
                })
                .map(this::convertToDto);
    }

    public Mono<AccountDto> removeGoal(String accountId, String goalId) {
        return this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.removeGoal(goalId);
                    return this.accountRepository.save(account);
                })
                .map(this::convertToDto);
    }

    private Goal convertToEntity(GoalDto goal) {
        return Goal.builder()
                .amount(goal.getAmount())
                .categoryName(goal.getCategory())
                .goalId(ObjectId.get())
                .goalInterval(goal.getGoalInterval())
                .build();
    }

    public Flux<GoalDto> getGoalsAsDto(String accountId) {
        return this.accountRepository.findById(accountId)
                .flatMapMany(account -> Flux.fromIterable(account.getGoals() != null ? account.getGoals() : List.of()))
                .map(this::convertToDto);
    }

    public Flux<Goal> getGoals(String accountId) {
        return this.accountRepository.findById(accountId)
                .flatMapMany(account -> Flux.fromIterable(account.getGoals() != null ? account.getGoals() : List.of()));
    }



    public void addIncomeType(String accountId, String type){
        this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    account.addIncomeCategory(type);
                    return this.accountRepository.save(account);
                });
    }

    private GoalDto convertToDto(Goal goal) {
        return GoalDto.builder()
                .goalId(goal.getGoalId().toString())
                .amount(goal.getAmount())
                .category(goal.getCategoryName())
                .goalInterval(goal.getGoalInterval())
                .build();
    }

    private List<GoalDto> convertToDtos(Collection<Goal> goals){
        return Objects.isNull(goals) ? new ArrayList<>()  :
                goals.stream().map(this::convertToDto).collect(Collectors.toList());
    }


    public Mono<AccountDto> getOrCreateEmptyAccount(String userId) {
        return this.accountRepository.findByUserIdAndActive(userId, true)
                .singleOrEmpty()
                .switchIfEmpty(
                        this.accountRepository.save(
                                Account.builder()
                                        .accountBalance(0.0)
                                        .userId(userId)
                                        .active(true)
                                        .build()))
        .map(this::convertToDto);
    }

    public Mono<AccountDto> updateGoal(String accountId, String goalId, GoalDto goalDto) {
        return this.accountRepository.findById(accountId)
                .flatMap(account -> {
                    updateGoal(account, goalId, goalDto);
                    return this.accountRepository.save(account);
                })
                .map(this::convertToDto);
    }

    private void updateGoal(Account account, String goalId, GoalDto goalDto) {
        account.getGoals()
                .stream()
                .filter(goal -> goal.getGoalId().toString().equals(goalId))
                .findAny()
                .map(goal -> {
                    goal.setAmount(goalDto.getAmount());
                    goal.setCategoryName(goalDto.getCategory());
                    return goal;
                }).orElseThrow(() -> new DocumentNotFountException(goalId, "Goal"));
    }
}
