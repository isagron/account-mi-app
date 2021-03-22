package com.isagron.accountmi_server.controllers;

import static com.isagron.accountmi_api.apis.v1.external.IncomeApi.PathVar.INCOME_ID;
import com.isagron.accountmi_api.apis.v1.external.IncomeApi;
import com.isagron.accountmi_api.dtos.income.IncomeDto;
import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.services.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(IncomeApi.Path.INCOME_PATH)
public class IncomesController {

    @Autowired
    private IncomeService incomeService;

    @GetMapping(IncomeApi.Path.INCOME_PATH_PAGE)
    public Mono<PageSupport<IncomeDto>> getExpenses(@RequestParam String accountId,
                                                    @RequestParam(required = false) String type,
                                                    @RequestParam(required = false) Integer month,
                                                    @RequestParam(required = false) Integer year,
                                                    @RequestParam(required = true, name = "page") int pageIndex,
                                                    @RequestParam(required = true, name = "size") int pageSize) {
        return incomeService.getAll(accountId, type, month, year, PageRequest.of(pageIndex, pageSize));
    }

    @PostMapping()
    public Mono<IncomeDto> createIncome(@RequestBody IncomeDto createIncomeReq) {
        return incomeService.createIncome(createIncomeReq);
    }

    @DeleteMapping()
    public Mono<Void> deleteIncome(@PathVariable(name = INCOME_ID) String incomeId) {
        return incomeService.deleteIncome(incomeId);
    }

    @GetMapping(IncomeApi.Path.INCOME_TYPES_PATH)
    public Mono<List<String>> getIncomeTypes(@RequestParam String accountId) {
        return incomeService.getIncomeTypes(accountId).collectList();
    }


    @GetMapping(IncomeApi.Path.INCOME_YEARS_PATH)
    public Flux<Integer> getIncomeYears(@RequestParam String accountId) {
        return incomeService.getIncomeYears(accountId);
    }
}
