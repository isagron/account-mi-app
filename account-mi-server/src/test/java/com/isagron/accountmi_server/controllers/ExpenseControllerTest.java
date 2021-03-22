package com.isagron.accountmi_server.controllers;

import com.isagron.accountmi_server.domain.common_elements.PageSupport;
import com.isagron.accountmi_server.domain.dao.AccountRepository;
import com.isagron.accountmi_server.domain.dao.ExpenseRepository;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.services.AccountService;
import com.isagron.accountmi_server.services.ExpenseService;
import com.isagron.accountmi_server.test_cases_data.TestCaseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.text.ParseException;
import java.util.List;
import java.util.UUID;

import static com.isagron.accountmi_api.apis.v1.external.ExpenseApi.Path.EXPENSE_PATH;
import static com.isagron.accountmi_api.apis.v1.external.ExpenseApi.Path.EXPENSE_PATH_PAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

//@SpringBootTest
@DirtiesContext
@WebFluxTest(controllers = ExpenseController.class ,
        excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        ExpenseController.class,
        ExpenseService.class,
        AccountService.class
})
class ExpenseControllerTest {


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ExpenseRepository expenseRepository;

    @MockBean
    private AccountRepository accountRepository;

    private List<Expense> expenses;


    @BeforeEach
    public void setup() throws ParseException {
        this.expenses = TestCaseData.DataTestCase1.expenseDataTestCase1();

    }

    @Test
    public void getExpensePageWithoutFilterTest() {
        webTestClient.get().uri(EXPENSE_PATH + EXPENSE_PATH_PAGE)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    public void getAllExpensePageWithFilter_TestCase1() {
        String queryParamString = buildRequestParamString(TestCaseData.DataTestCase1.ACCOUNT_ID, TestCaseData.FOOD_CATEGORY, 6,
                2020, 0, 10);
        EntityExchangeResult<PageSupport> result = webTestClient.get().uri(EXPENSE_PATH + EXPENSE_PATH_PAGE + queryParamString)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PageSupport.class)
                .returnResult();


    }

    private String buildRequestParamString(String accountId, String category, Integer month, Integer year,
                                           Integer page, Integer size) {
        StringBuilder stringBuilder = new StringBuilder("?");
        boolean andFlag = false;
        if (accountId != null) {
            stringBuilder.append("accountId=").append(accountId);
            andFlag = true;
        }
        if (category != null) {
            stringBuilder.append(andFlag ? "&" : "").append("category=").append(category);
            andFlag = true;
        }
        if (month != null) {
            stringBuilder.append(andFlag ? "&" : "").append("month=").append(month);
            andFlag = true;
        }
        if (year != null) {
            stringBuilder.append(andFlag ? "&" : "").append("year=").append(year);
            andFlag = true;
        }
        if (page != null) {
            stringBuilder.append(andFlag ? "&" : "").append("page=").append(page);
            andFlag = true;
        }
        if (size != null) {
            stringBuilder.append(andFlag ? "&" : "").append("size=").append(size);
        }

        return stringBuilder.toString();
    }
}