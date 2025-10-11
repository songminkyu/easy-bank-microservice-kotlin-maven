package io.github.songminkyu.account.constants

import lombok.AccessLevel
import lombok.NoArgsConstructor


@NoArgsConstructor(access = AccessLevel.PRIVATE)
object AccountConstants {
    const val SAVINGS: String = "Savings"
    const val ADDRESS: String = "123 Main Street, New York"
    const val STATUS_201: String = "201"
    const val MESSAGE_201: String = "Account created successfully"
    const val STATUS_200: String = "200"
    const val MESSAGE_200: String = "Request processed successfully"
    const val STATUS_417: String = "417"
    const val MESSAGE_417_UPDATE: String = "Update operation failed. Please try again or contact Dev team"
    const val MESSAGE_417_DELETE: String = "Delete operation failed. Please try again or contact Dev team"

    const val CUSTOMER_RESOURCE_NAME: String = "Customer"
    const val ACCOUNT_RESOURCE_NAME: String = "Account"
}