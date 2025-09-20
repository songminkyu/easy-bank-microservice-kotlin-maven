package io.github.songminkyu.account.mapper

import io.github.songminkyu.account.dto.AccountDTO
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.mapper.util.AccountMappingUtil
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(
    uses = [AccountMappingUtil::class],
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface AccountMapper {

    fun toEntity(dto: AccountDTO): Account
    fun toDto(entity: Account): AccountDTO
    fun toEntity(dtoList: List<AccountDTO>): List<Account>
    fun toDto(entityList: List<Account>): List<AccountDTO>

    @org.mapstruct.Named("partialUpdate")
    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(@org.mapstruct.MappingTarget entity: Account, dto: AccountDTO)

    @Mapping(source = ".", target = "accountNumber", qualifiedBy = [AccountMappingUtil.AccountNumber::class])
    @Mapping(source = ".", target = "accountType", qualifiedBy = [AccountMappingUtil.AccountType::class])
    @Mapping(source = ".", target = "branchAddress", qualifiedByName = ["branchAddress"])
    fun fromMap(map: Map<String, Any?>): AccountDTO
}