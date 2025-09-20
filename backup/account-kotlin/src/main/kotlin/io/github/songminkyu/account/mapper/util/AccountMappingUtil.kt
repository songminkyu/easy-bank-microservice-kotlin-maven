package io.github.songminkyu.account.mapper.util

import org.mapstruct.Named
import org.mapstruct.Qualifier

object AccountMappingUtil {

    @Qualifier
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class AccountNumber

    @Qualifier
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.SOURCE)
    annotation class AccountType

    @AccountNumber
    fun accountNumber(input: Map<String, Any?>): Long? {
        return input["accountNumber"] as? Long
    }

    @AccountType
    fun accountType(input: Map<String, Any?>): String? {
        return input["accountType"] as? String
    }

    @Named("branchAddress")
    fun branchAddress(input: Map<String, Any?>): String? {
        return input["branchAddress"] as? String
    }
}