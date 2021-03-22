package com.isagron.accountmi_server.test_cases_data;

import com.isagron.accountmi_server.domain.models.Account;
import com.isagron.accountmi_server.domain.models.Expense;
import com.isagron.accountmi_server.domain.models.Goal;
import org.bson.types.ObjectId;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TestCaseData {

    public final static String FOOD_CATEGORY = "FOOD";
    public final static String FARM_CATEGORY = "FARM";
    public final static String LOAN_CATEGORY = "LOAN";
    public final static String BILL_CATEGORY = "BILL";
    public final static String SUPERFARM_STORE = "SUPERFARM";
    public final static String BANK_STORE = "BANK";
    public final static String CITY_STORE = "CITY";
    public final static String SHUPERSAL_STORE = "SHUPERSAL";
    public final static String RAMILEVI_STORE = "RAMILEVI";



    public static class DataTestCase1 {

        public static final String ACCOUNT_ID = UUID.randomUUID().toString();


        public static List<Expense> expenseDataTestCase1() throws ParseException {
            String accountId = ACCOUNT_ID;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            Date date1 = formatter.parse("07-06-2020");
            Date date2 = formatter.parse("10-06-2020");
            Date date3 = formatter.parse("07-05-2020");
            Date date4 = formatter.parse("07-07-2020");
            Date date5 = formatter.parse("07-09-2019");

            return List.of(
                    Expense.builder()
                            .category(FOOD_CATEGORY)
                            .date(date1)
                            .amount(106.0)
                            .accountId(accountId)
                            .store(SHUPERSAL_STORE)
                            .build(),

                    Expense.builder()
                            .category(FOOD_CATEGORY)
                            .date(date2)
                            .amount(106.0)
                            .accountId(accountId)
                            .store(SHUPERSAL_STORE)
                            .build(),

                    Expense.builder()
                            .category(FOOD_CATEGORY)
                            .date(date3)
                            .amount(105.0)
                            .accountId(accountId)
                            .store(SHUPERSAL_STORE)
                            .build(),

                    Expense.builder()
                            .category(FOOD_CATEGORY)
                            .date(date4)
                            .amount(107.0)
                            .accountId(accountId)
                            .store(RAMILEVI_STORE)
                            .build(),

                    Expense.builder()
                            .category(FOOD_CATEGORY)
                            .date(date5)
                            .amount(109.0)
                            .accountId(accountId)
                            .store(RAMILEVI_STORE)
                            .build()
            );
        }

        public static List<Account> accountDataTestCase1() {
            Account account = Account.builder()
                    .id(ACCOUNT_ID)
                    .build();
            return Collections.singletonList(account);
        }
    }


    public static class DataTestCase2 {

        public static final String ACCOUNT_ID = UUID.randomUUID().toString();

        public static List<Expense> expenseData() throws ParseException {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

            Date jan = formatter.parse("07-01-2020");
            Date feb = formatter.parse("07-02-2020");
            Date mar = formatter.parse("07-03-2020");
            Date apr = formatter.parse("07-04-2020");
            Date may = formatter.parse("07-05-2020");
            Date june = formatter.parse("07-06-2020");
            Date jul = formatter.parse("07-07-2020");
            Date aug = formatter.parse("07-08-2020");
            Date sep = formatter.parse("07-09-2020");
            Date oct = formatter.parse("07-10-2020");
            Date nov = formatter.parse("07-11-2020");

            List<Expense> result = new ArrayList<>();

            List<Expense> foodExpense = expenseFor(ACCOUNT_ID, FOOD_CATEGORY, SHUPERSAL_STORE,
                    Map.of(jan, 100.0,
                            feb, 200.0,
                            mar, 200.0,
                            apr, 200.0,
                            may, 500.0,
                            june, 100.0,
                            jul, 200.0,
                            aug, 300.0,
                            sep, 400.0,
                            oct, 200.0
                    )
            );

            List<Expense> farmExpense = expenseFor(ACCOUNT_ID, FARM_CATEGORY, SUPERFARM_STORE,
                    Map.of(jan, 100.0,
                            feb, 200.0,
                            mar, 200.0,
                            apr, 300.0,
                            may, 5500.0,
                            june, 1100.0,
                            aug, 300.0,
                            sep, 400.0,
                            oct, 200.0
                    )
            );

            List<Expense> billExpense = expenseFor(ACCOUNT_ID, BILL_CATEGORY, CITY_STORE,
                    Map.of(jan, 1000.0,
                            feb, 1000.0,
                            mar, 1000.0,
                            apr, 1000.0,
                            may, 1000.0,
                            june, 1000.0,
                            aug, 1000.0,
                            sep, 1000.0,
                            oct, 1000.0
                    )
            );

            List<Expense> loanExpense = expenseFor(ACCOUNT_ID, LOAN_CATEGORY, BANK_STORE,
                    Map.of(jan, 1000.0,
                            feb, 1000.0,
                            mar, 1000.0,
                            apr, 1000.0,
                            may, 1000.0,
                            june, 1000.0,
                            aug, 1000.0,
                            sep, 1000.0,
                            oct, 1000.0
                    )
            );

            result.addAll(foodExpense);
            result.addAll(farmExpense);
            result.addAll(billExpense);
            result.addAll(loanExpense);

            return result;

        }

        public static List<Account> accountData() {
            Account account = Account.builder()
                    .id(ACCOUNT_ID)
                    .goals(goalData())
                    .build();
            return Collections.singletonList(account);
        }

        private static List<Goal> goalData() {
            return List.of(
                    Goal.builder()
                            .goalId(ObjectId.get())
                            .categoryName(FOOD_CATEGORY)
                            .amount(200)
                            .build(),
                    Goal.builder()
                            .goalId(ObjectId.get())
                            .categoryName(FARM_CATEGORY)
                            .amount(500)
                            .build(),
                    Goal.builder()
                            .goalId(ObjectId.get())
                            .categoryName(LOAN_CATEGORY)
                            .amount(1000)
                            .build(),
                    Goal.builder()
                            .goalId(ObjectId.get())
                            .categoryName(BILL_CATEGORY)
                            .amount(1050)
                            .build()
            );
        }

    }

    private static List<Expense> expenseFor(String accountId, String category, String store, Map<Date, Double> mapDateToAmount) {
        return mapDateToAmount.entrySet()
                .stream()
                .map(entry -> Expense.builder()
                        .category(category)
                        .date(entry.getKey())
                        .amount(entry.getValue())
                        .accountId(accountId)
                        .store(store)
                        .build())
                .collect(Collectors.toList());
    }


}
