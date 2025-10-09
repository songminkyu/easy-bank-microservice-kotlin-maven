package io.github.songminkyu.account.constants;

import java.util.Random;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

    public static final Random RANDOM = new Random();

    public static final String SPRING_PROFILE_DEVELOPMENT = "local";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String DELETED = "deleted";
    public static final String ACCOUNT_TYPE = "accountType";
    public static final String CUSTOMER_ID = "customerId";
    public static final String BRANCH_ADDRESS = "branchAddress";
    public static final String ACCOUNT_NUMBER = "accountNumber";
    public static final String COMMUNICATION_SW = "communicationSw";
}
