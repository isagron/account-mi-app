package com.isagron.accountmi_api.apis.v1.external;

import static com.isagron.accountmi_api.apis.v1.RootApiPath.ROOT_PATH;

public interface UserApi {

    class PathVar {

    }

    class Path {
        public static final String AUTH_PATH = ROOT_PATH + "/auth";
        public static final String REGISTER_PATH = "/signup";
        public static final String SIGNIN_PATH = "/signin";
        public static final String SIGNOUT_PATH = "/signout";
        public static final String REFRESH_TOKEN_PATH = "/refresh-token";
    }
}
