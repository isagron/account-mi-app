package com.isagron.accountmi_api.apis.v1.external;

public interface StatisticApi {

    class PathVar {
        public static final String ACCOUNT_ID = "accountId";
        public static final String MONTH = "month";
        public static final String YEAR = "year";
        public static final String CATEGORY = "category";
    }

    class Path {
        public static final String STATISTICS_PATH = "/v1/statistics";
        public static final String STATISTICS_ACCOUNT_BALANCE_PATH = "/account-balance";
        public static final String GOAL_STATUS_PATH = "/goal-status";
        public static final String CATEGORY_DIVISION_FOR_MONTH_PATH = "/category-division-per-month";
        public static final String CATEGORY_EXPENSE_PER_MONTH_PATH = "/category-expense-per-month";

    }
}
