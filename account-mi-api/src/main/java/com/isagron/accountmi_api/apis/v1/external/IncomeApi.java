package com.isagron.accountmi_api.apis.v1.external;

public interface IncomeApi {

    class PathVar {
        public static final String ACCOUNT_ID = "accountId";
        public static final String INCOME_ID = "incomeId";
    }

    class Path {
        public static final String INCOME_PATH = "/v1/incomes";
        public static final String INCOME_TYPES_PATH = "/types";
        public static final String INCOME_YEARS_PATH = "/years";
        public static final String INCOME_PATH_PAGE = "/page";

        public static final String SINGLE_INCOME_PATH = "/{" + PathVar.INCOME_ID + "}";
    }
}
