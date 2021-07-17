package com.isagron.accountmi_server.services;

import com.isagron.accountmi_api.dtos.expense.ExpenseDto;
import com.isagron.accountmi_api.dtos.statistics.CategoryToAmountDto;
import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.dao.ExpenseRepository;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.services.elements.ExpenseCategoryToAmount;
import com.isagron.accountmi_server.services.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.isagron.accountmi_server.services.utils.DataFormatterUtils.doubleFormatter2;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    private final AccountService accountService;

    public Flux<ExpenseDto> getAll(String accountId, String category, int month, int year) {
        return this.expenseRepository.find(accountId, category, month, year)
                .map(this::convertToDto);
    }

    private ExpenseDto convertToDto(Expense expense) {
        return ExpenseDto
                .builder()
                .id(expense.getId())
                .accountId(expense.getAccountId())
                .category(expense.getCategory())
                .store(expense.getStore())
                .date(expense.getDate())
                .amount(expense.getAmount())
                .build();
    }

    private Expense convertToDoc(ExpenseDto expenseDto){
        return Expense
                .builder()
                .accountId(expenseDto.getAccountId())
                .category(expenseDto.getCategory())
                .store(expenseDto.getStore())
                .date(expenseDto.getDate())
                .amount(expenseDto.getAmount())
                .build();
    }

    public Mono<PageSupport<ExpenseDto>> getAll(String accountId, String category, Integer month, Integer year, Pageable pageable) {
        return this.expenseRepository.find(accountId, category, month, year, pageable)
                .map(page -> new PageSupport<>(
                        page.getContent().stream().map(this::convertToDto).collect(Collectors.toList()),
                        page.getPageNumber(), page.getPageSize(), page.getTotalElements(), page.getTotalAmount()
                ));
    }

    public Mono<ExpenseDto> createExpense(ExpenseDto expenseDto) {
        Expense expense = convertToDoc(expenseDto);
        return accountService.addExpenseCategory(expenseDto.getAccountId(), expenseDto.getCategory())
        .flatMap(account -> this.expenseRepository.save(expense))
                .map(this::convertToDto);
    }

    public List<String> getAllStores(String accountId) {
        return this.expenseRepository.findAllStores(accountId);
    }

    public Flux<Integer> getExpensesYears(String accountId) {
        return this.expenseRepository.getExpensesYears(accountId);
    }

    public Mono<Double> getTotalExpenseDateRange(String accountId, DateRange dateRange) {
        return this.expenseRepository.getTotalExpenseDateRange(accountId, dateRange)
                .switchIfEmpty(Mono.just(0.0));
    }

    public Mono<Map<String, Double>> getExpenseAmountGroupByCategoryForMonth(String accountId, int month, int year) {
        return this.expenseRepository.getTotalExpensePerCategoryForMonth(accountId, month, year)
                .collectMap(ExpenseCategoryToAmount::getCategory, ExpenseCategoryToAmount::getTotal);
    }

    public Mono<Map<String, Double>> getExpenseAmountGroupByCategoryForYear(String accountId, int year) {
        return this.expenseRepository.getTotalExpensePerCategoryForYear(accountId, year)
                .collectMap(ExpenseCategoryToAmount::getCategory, ExpenseCategoryToAmount::getTotal);
    }

    public Mono<Map<String, Double>> getExpenseAvgGroupByCategory(String accountId, int year) {

        Calendar calendarInstance = Calendar.getInstance();
        int currentYear = calendarInstance.get(Calendar.YEAR);
        Integer numberOfMonth = 12;
        if (year == currentYear){
            numberOfMonth = calendarInstance.get(Calendar.MONTH) + 1;
        }
        final int nMonth = numberOfMonth;

        return this.expenseRepository.getTotalExpensePerCategoryForYear(accountId, year)
                .collectMap(ExpenseCategoryToAmount::getCategory,
                        categoryToTotal -> Double.parseDouble(doubleFormatter2.format(categoryToTotal.getTotal() / nMonth)));
    }

    public Flux<CategoryToAmountDto> getTotalSpentInLast12MonthForCategory(String accountId, String category) {
        DateRange dateRange = DateUtils.beforeXMonthTillNow(12);
        return this.expenseRepository.getTotalSpentForCategoryPerMonth(accountId, category, dateRange)
                .map(result -> CategoryToAmountDto.builder()
                        .amount(result.getDouble(CategoryToAmountDto.Fields.amount))
                        .category(result.getString(CategoryToAmountDto.Fields.category))
                        .month(result.getInteger(CategoryToAmountDto.Fields.month))
                        .year(result.getInteger(CategoryToAmountDto.Fields.year))
                        .build());
    }

    public Flux<CategoryToAmountDto> getTotalSpentInLast12Month(String accountId) {
        DateRange dateRange = DateUtils.beforeXMonthTillNow(12);
        return this.expenseRepository.getTotalSpentPerMonth(accountId, dateRange)
                .map(result -> CategoryToAmountDto.builder()
                        .amount(result.getDouble(ExpenseCategoryToAmount.Fields.total))
                        .month(((Document) result.get("_id")).getInteger(CategoryToAmountDto.Fields.month))
                        .year(((Document) result.get("_id")).getInteger(CategoryToAmountDto.Fields.year))
                        .build());
    }

    public Mono<Void> deleteExpense(String expenseId) {
        return this.expenseRepository.deleteById(expenseId);
    }

    public Mono<Void> deleteAllByAccountAndCategory(String accountId, String categoryName) {
        return this.expenseRepository.deleteByAccountIdAndCategory(accountId, categoryName);
    }
}
