package com.isagron.accountmi_server.domain.dao;

import com.isagron.accountmi_server.domain.common_elements.DateRange;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.services.utils.DateUtils;
import com.isagron.accountmi_server.test_cases_data.DataLoader;
import com.isagron.accountmi_server.test_cases_data.TestCaseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.isagron.accountmi_server.test_cases_data.TestCaseData.FOOD_CATEGORY;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class ExtendExpenseRepositoryImplTest {



    @Autowired
    private ExpenseRepository expenseRepository;


    List<Expense> expenses;

    @BeforeEach
    public void setup() throws ParseException {

        this.expenses = TestCaseData.DataTestCase1.expenseDataTestCase1();
        DataLoader.loadData(this.expenseRepository, this.expenses);

        Date date = Calendar.getInstance().getTime();
        int offset = Calendar.getInstance().getTimeZone().getRawOffset();
        System.out.println(offset);

    }

    @Test
    public void findExpenseTest(){

        StepVerifier.create(this.expenseRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();

        StepVerifier.create(this.expenseRepository.find(TestCaseData.DataTestCase1.ACCOUNT_ID, FOOD_CATEGORY, Calendar.JUNE, 2020).log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void findExpenseByMonthTest(){

        StepVerifier.create(this.expenseRepository.findAll().log())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();

        System.out.println("find by month start");
        StepVerifier.create(this.expenseRepository.findByMonth(Calendar.JUNE+1, new ArrayList<>()).log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();

        System.out.println("find by month end");

    }

    @Test
    public void findAllStoresTest(){
        StepVerifier.create(this.expenseRepository.findAllStores(TestCaseData.DataTestCase1.ACCOUNT_ID).log())
                .expectSubscription()
                .expectNext(TestCaseData.SHUPERSAL_STORE)
                .expectNext(TestCaseData.RAMILEVI_STORE)
                .verifyComplete();
    }

    @Test
    public void findAllYearsTest(){
        StepVerifier.create(this.expenseRepository.getExpensesYears(TestCaseData.DataTestCase1.ACCOUNT_ID).log())
                .expectSubscription()
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void getTotalExpenseDateRange() {
        DateRange currentMonthDateRange = DateUtils.getMonthDateRange(4, 2020);

        StepVerifier.create(this.expenseRepository.getTotalExpenseDateRange(TestCaseData.DataTestCase1.ACCOUNT_ID, currentMonthDateRange).log())
                .expectSubscription()
                .expectNext(105.0)
                .verifyComplete();

        currentMonthDateRange = DateUtils.getMonthDateRange(5, 2020);

        StepVerifier.create(this.expenseRepository.getTotalExpenseDateRange(TestCaseData.DataTestCase1.ACCOUNT_ID, currentMonthDateRange).log())
                .expectSubscription()
                .expectNext(212.0)
                .verifyComplete();

        currentMonthDateRange = DateUtils.getMonthDateRange(6, 2020);

        StepVerifier.create(this.expenseRepository.getTotalExpenseDateRange(TestCaseData.DataTestCase1.ACCOUNT_ID, currentMonthDateRange).log())
                .expectSubscription()
                .expectNext(107.0)
                .verifyComplete();

        currentMonthDateRange = DateUtils.getMonthDateRange(8, 2019);

        StepVerifier.create(this.expenseRepository.getTotalExpenseDateRange(TestCaseData.DataTestCase1.ACCOUNT_ID, currentMonthDateRange).log())
                .expectSubscription()
                .expectNext(109.0)
                .verifyComplete();
    }

    @Test
    public void getTotalExpensePerCategoryForMonthTest(){
        StepVerifier.create(this.expenseRepository.getTotalExpensePerCategoryForMonth(TestCaseData.DataTestCase1.ACCOUNT_ID, 6, 2020).log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }



}