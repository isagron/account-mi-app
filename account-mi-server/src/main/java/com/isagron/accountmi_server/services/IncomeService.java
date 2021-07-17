package com.isagron.accountmi_server.services;

import com.isagron.accountmi_api.dtos.income.IncomeDto;
import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.dao.IncomeRepository;
import com.isagron.accountmi_server.domain.models.Income;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Service
@Slf4j
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private AccountService accountService;


    public Mono<PageSupport<IncomeDto>> getAll(String accountId, String type, Integer month, Integer year, PageRequest pageable) {
        return this.incomeRepository.find(accountId, type, month, year, pageable)
                .map(page -> new PageSupport<>(
                        page.getContent().stream().map(this::convertToDto).collect(Collectors.toList()),
                        page.getPageNumber(), page.getPageSize(), page.getTotalElements(), page.getTotalAmount()
                ));
    }

    public Mono<IncomeDto> createIncome(IncomeDto createIncomeRequest) {
        return accountService.addIncomeCategory(createIncomeRequest.getAccountId(), createIncomeRequest.getType())
                .map(account -> convertToDoc(createIncomeRequest))
                .flatMap(income -> this.incomeRepository.save(income))
                .map(this::convertToDto);

    }

    private Income convertToDoc(IncomeDto incomeDto) {
        return Income.builder()
                .accountId(incomeDto.getAccountId())
                .amount(incomeDto.getAmount())
                .type(incomeDto.getType())
                .id(incomeDto.getId())
                .date(incomeDto.getDate())
                .build();
    }

    private IncomeDto convertToDto(Income income) {
        return IncomeDto.builder()
                .accountId(income.getAccountId())
                .amount(income.getAmount())
                .consistent(income.isConsistent())
                .date(income.getDate())
                .type(income.getType())
                .id(income.getId())
                .build();
    }

    public Flux<String> getIncomeTypes(String accountId) {
        return this.incomeRepository.findAllTypeByAccountId(accountId);
    }

    public Flux<Integer> getIncomeYears(String accountId) {
        return this.incomeRepository.findAllYearsByAccountId(accountId);
    }

    public Mono<Double> getTotalIncomeDateRange(String accountId, DateRange dateRange) {
        return this.incomeRepository.getTotalIncomeDateRange(accountId, dateRange)
                .switchIfEmpty(Mono.just(0.0));
    }

    public Mono<Void> deleteIncome(String incomeId) {
        return this.incomeRepository.deleteById(incomeId);
    }
}
