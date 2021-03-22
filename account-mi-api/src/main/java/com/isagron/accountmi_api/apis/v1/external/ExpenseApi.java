package com.isagron.accountmi_api.apis.v1.external;

public interface ExpenseApi {

    class PathVar {
        public static final String ACCOUNT_ID = "accountId";
        public static final String EXPENSE_ID = "expenseId";
    }

    class Path {
        public static final String EXPENSE_PATH = "/v1/expenses";
        public static final String EXPENSE_PATH_PAGE = "/page";
        public static final String EXPENSE_STORE_PATH = "/stores";
        public static final String EXPENSE_SUPPORTED_YEARS_PATH = "/years";

        public static final String SINGLE_EXPENSE_PATH = "/{" + ExpenseApi.PathVar.EXPENSE_ID + "}";
    }
}
