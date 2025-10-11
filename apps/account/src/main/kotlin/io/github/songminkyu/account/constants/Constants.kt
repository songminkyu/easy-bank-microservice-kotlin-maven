package io.github.songminkyu.account.constants

import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.util.*

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object Constants {
    @JvmField
    val RANDOM: Random = Random()

    const val SPRING_PROFILE_DEVELOPMENT: String = "local"
    const val SPRING_PROFILE_PRODUCTION: String = "prod"
    const val DELETED: String = "deleted"
    const val ACCOUNT_TYPE: String = "accountType"
    const val CUSTOMER_ID: String = "customerId"
    const val BRANCH_ADDRESS: String = "branchAddress"
    const val ACCOUNT_NUMBER: String = "accountNumber"
    const val COMMUNICATION_SW: String = "communicationSw"
}
