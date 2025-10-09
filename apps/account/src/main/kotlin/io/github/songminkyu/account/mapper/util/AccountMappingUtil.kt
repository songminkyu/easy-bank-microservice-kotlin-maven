
package io.github.songminkyu.account.mapper.util

import org.mapstruct.Named
import org.mapstruct.Qualifier

object AccountMappingUtil {
    @Qualifier
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class AccountNumber

    @Qualifier
    @Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
    @Retention(AnnotationRetention.SOURCE)
    annotation class AccountType

    @JvmStatic
    @AccountNumber
    fun accountNumber(`in`: MutableMap<String?, Any?>): Long? {
        return `in`["accountNumber"] as Long?
    }

    @JvmStatic
    @AccountType
    fun accountType(`in`: MutableMap<String?, Any?>): String? {
        return `in`["accountType"] as String?
    }

    @JvmStatic
    @Named("branchAddress")
    fun branchAddress(`in`: MutableMap<String?, Any?>): String? {
        return `in`["branchAddress"] as String?
    }
}