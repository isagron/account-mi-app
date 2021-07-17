package com.isagron.accountmi_server.controllers;

import com.isagron.accountmi_api.apis.v1.external.ExpenseApi;
import com.isagron.accountmi_api.dtos.expense.ExpenseDto;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

import static com.isagron.accountmi_api.apis.v1.external.ExpenseApi.Path.*;
import static com.isagron.accountmi_api.apis.v1.external.ExpenseApi.PathVar.EXPENSE_ID;

import java.util.List;

@RestController
@RequestMapping(ExpenseApi.Path.EXPENSE_PATH)
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping
    public Flux<ExpenseDto> getAll(@RequestParam @NotNull String accountId,
                                   @RequestParam String category,
                                   @RequestParam int month,
                                   @RequestParam int year){
        return expenseService.getAll(accountId, category, month, year);
    }

    @GetMapping(EXPENSE_PATH_PAGE)
    public Mono<PageSupport<ExpenseDto>> getExpenses(@RequestParam String accountId,
                                         @RequestParam(required = false) String category,
                                         @RequestParam(required = false) Integer month,
                                         @RequestParam(required = false) Integer year,
                                         @RequestParam(required = true, name = "page") int pageIndex,
                                         @RequestParam(required = true, name = "size") int pageSize){
        return expenseService.getAll(accountId, category, month, year, PageRequest.of(pageIndex, pageSize));
    }

    @PostMapping
    public Mono<ExpenseDto> createExpense(@RequestBody ExpenseDto expenseDto){
        return expenseService.createExpense(expenseDto);
    }

    @DeleteMapping(SINGLE_EXPENSE_PATH)
    public Mono<Void> deleteExpense(@PathVariable(name = EXPENSE_ID) String expenseId){
        return expenseService.deleteExpense(expenseId);
    }

    @GetMapping(EXPENSE_STORE_PATH)
    public List<String> getAllStores(@RequestParam String accountId){
        return expenseService.getAllStores(accountId);
    }

    @GetMapping(EXPENSE_SUPPORTED_YEARS_PATH)
    public Flux<Integer> getYears(@RequestParam String accountId, @RequestParam String userId) {
         return expenseService.getExpensesYears(accountId);
    }



}
