package com.isagron.accountmi_server.services;

import com.isagron.accountmi_server.AccountMiServerApplication;
import com.isagron.accountmi_server.domain.dao.ExpenseRepository;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.services.utils.DateUtils;
import com.isagron.accountmi_server.test_cases_data.DataLoader;
import com.isagron.accountmi_server.test_cases_data.TestCaseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        AccountMiServerApplication.class,
        ExpenseService.class
})
@MockBeans({
        @MockBean(AccountService.class)
})
class ExpenseServiceTest {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    private List<Expense> expenses;

    @BeforeEach
    public void setup() throws ParseException {
        this.expenses = TestCaseData.DataTestCase1.expenseDataTestCase1();

        this.expenseRepository.deleteAll()
                .thenMany(Flux.fromIterable(expenses))
                .flatMap(expense -> expenseRepository.save(expense))
                .doOnNext(expense -> System.out.println("Inserted expense is: " + expense))
                .blockLast();
    }

    @Test
    public void getTotalExpenseForDateRange(){
        StepVerifier.create(this.expenseService.getTotalExpenseDateRange(TestCaseData.DataTestCase1.ACCOUNT_ID, DateUtils.yearRange(2020)).log())
                .expectSubscription()
                .expectNext(424.0)
                .verifyComplete();
    }

    @Test
    public void getExpenseAvgGroupByCategoryTest() throws ParseException {
        DataLoader.loadData(this.expenseRepository, TestCaseData.DataTestCase2.expenseData());
        StepVerifier.create(this.expenseService.getExpenseAvgGroupByCategory(TestCaseData.DataTestCase2.ACCOUNT_ID, 2020).log())
                .expectSubscription()
                .assertNext((result) -> {
                    assertEquals(818.18, result.get(TestCaseData.LOAN_CATEGORY) );
                    assertEquals(818.18, result.get(TestCaseData.BILL_CATEGORY) );
                    assertEquals(754.55, result.get(TestCaseData.FARM_CATEGORY) );
                    assertEquals(218.18, result.get(TestCaseData.FOOD_CATEGORY) );
                })
                .verifyComplete();
    }

    @Test
    public void getYearsTest() throws ParseException {
        DataLoader.loadData(this.expenseRepository, TestCaseData.DataTestCase2.expenseData());
        this.expenseService.getExpensesYears(TestCaseData.DataTestCase2.ACCOUNT_ID).log()
                .subscribe(s -> {
                    System.out.println(s);
                    int a = s;
                    System.out.println(a);

                });
        StepVerifier.create(this.expenseService.getExpensesYears(TestCaseData.DataTestCase2.ACCOUNT_ID).log())
                .expectSubscription()
                .expectNext(2020)
                .verifyComplete();

    }

}