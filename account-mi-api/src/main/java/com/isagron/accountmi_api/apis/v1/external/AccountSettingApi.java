package com.isagron.accountmi_api.apis.v1.external;

import static com.isagron.accountmi_api.apis.v1.RootApiPath.ROOT_PATH;
import com.isagron.accountmi_api.dtos.account.AccountDto;
import com.isagron.accountmi_api.dtos.account.CreateAccountRequest;
import com.isagron.accountmi_api.dtos.account.UpdateAccountSettingRequest;
import reactor.core.publisher.Mono;

public interface AccountSettingApi {


    class PathVar {
        public static final String ACCOUNT_ID = "accountId";
        public static final String CATEGORY_NAME = "categoryName";
        public static final String GOAL_ID = "goalId";
    }

    class Path {
        public static final String ACCOUNTS_PATH = ROOT_PATH + "/accounts";
        public static final String GET_OR_CREATE_PATH = "/get-or-create";
        public static final String SINGLE_ACCOUNT_PATH = "/{" + PathVar.ACCOUNT_ID + "}";
        public static final String ACCOUNT_EXPENSE_CATEGORIES_PATH = SINGLE_ACCOUNT_PATH + "/expense-categories";
        public static final String ACCOUNT_SINGLE_EXPENSE_CATEGORIES_PATH =
                ACCOUNT_EXPENSE_CATEGORIES_PATH + "/{" + PathVar.CATEGORY_NAME + "}";
        public static final String ACCOUNT_INCOME_CATEGORIES_PATH = SINGLE_ACCOUNT_PATH + "/income-categories";
        public static final String ACCOUNT_SINGLE_INCOME_CATEGORIES_PATH =
                ACCOUNT_INCOME_CATEGORIES_PATH + "/{" + PathVar.CATEGORY_NAME + "}";
        public static final String ACCOUNT_GOALS_PATH = SINGLE_ACCOUNT_PATH + "/goals";
        public static final String ACCOUNT_SINGLE_GOAL_PATH =
                ACCOUNT_GOALS_PATH + "/{" + PathVar.GOAL_ID + "}";
    }

    Mono<AccountDto> createAccount(CreateAccountRequest createAccountRequest);

    AccountDto updateAccountSettings(UpdateAccountSettingRequest updateAccountSettingRequest);

}
