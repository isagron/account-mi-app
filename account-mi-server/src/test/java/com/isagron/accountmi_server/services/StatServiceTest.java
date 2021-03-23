package com.isagron.accountmi_server.services;

import com.isagron.accountmi_server.AccountMiServerApplication;
import com.isagron.accountmi_server.domain.dao.AccountRepository;
import com.isagron.accountmi_server.domain.dao.ExpenseRepository;
import com.isagron.accountmi_server.domain.dao.ExtendIncomeRepositoryImpl;
import com.isagron.accountmi_server.domain.dao.IncomeRepository;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.test_cases_data.DataLoader;
import com.isagron.accountmi_server.test_cases_data.TestCaseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.isagron.accountmi_server.test_cases_data.TestCaseData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AccountMiServerApplication.class,
        StatService.class,
        ExpenseService.class,
        IncomeService.class,
        ExtendIncomeRepositoryImpl.class,
        IncomeRepository.class,
        AccountService.class
})
@MockBeans({
})
class StatServiceTest {

    @Autowired
    private StatService statService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private AccountRepository accountRepository;

    private List<Expense> expenses;



    @Test
    public void getAccountBalanceForMonth() throws ParseException {
        DataLoader.loadData(this.expenseRepository, TestCaseData.DataTestCase1.expenseDataTestCase1());

        StepVerifier.create(this.statService.getAccountBalanceForMonth(TestCaseData.DataTestCase1.ACCOUNT_ID, 5, 2020).log())
                .expectSubscription()
                .assertNext((result) -> {
                    assertEquals(212.0, result.getCurrentMonth());
                    assertEquals(0.0, result.getPreviousMonth());
                    assertEquals(424.0, result.getTotalThisYear());
                })
                .verifyComplete();
    }

    @Test
    public void getGoalStatusTest() throws ParseException {
        DataLoader.loadData(this.accountRepository, TestCaseData.DataTestCase2.accountData());
        DataLoader.loadData(this.expenseRepository, TestCaseData.DataTestCase2.expenseData());
        Map<String, Double> avgByCategoryResult = Map.of(
                FARM_CATEGORY, 691.67,
                LOAN_CATEGORY, 750.0,
                BILL_CATEGORY, 750.0,
                FOOD_CATEGORY, 200.0
        );
        StepVerifier.create(this.statService.getGoalStatus(TestCaseData.DataTestCase2.ACCOUNT_ID, 0, 2020).log())
                .expectSubscription()
                .assertNext((result) -> {
                    assertEquals(avgByCategoryResult.get(result.getCategory()), result.getAvg());
                })
                .expectNextCount(3)
                .verifyComplete();
    }
}