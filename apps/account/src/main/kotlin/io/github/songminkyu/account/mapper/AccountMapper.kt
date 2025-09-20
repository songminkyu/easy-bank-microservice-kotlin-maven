package io.github.songminkyu.account.mapper

import io.github.songminkyu.account.dto.AccountDTO
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.mapper.util.AccountMappingUtil
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [AccountMappingUtil::class])
interface AccountMapper : EntityMapper<AccountDTO, Account> {

    @Mapping(source = ".", target = "accountNumber", qualifiedBy = [AccountMappingUtil.AccountNumber::class])
    @Mapping(source = ".", target = "accountType", qualifiedBy = [AccountMappingUtil.AccountType::class])
    @Mapping(source = ".", target = "branchAddress", qualifiedByName = "branchAddress")
    fun fromMap(map: Map<String, Any?>): AccountDTO
}