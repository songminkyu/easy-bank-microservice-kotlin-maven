package io.github.songminkyu.account.mapper.impl

import io.github.songminkyu.account.dto.AccountDTO
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.mapper.AccountMapper
import io.github.songminkyu.account.mapper.util.AccountMappingUtil
import org.springframework.stereotype.Component

@Component
class AccountMapperImpl : AccountMapper {
    override fun toEntity(dto: AccountDTO?): Account? {
        if (dto == null) {
            return null
        }

        val account = Account.builder()

        account.accountNumber(dto.accountNumber)
        account.accountType(dto.accountType)
        account.branchAddress(dto.branchAddress)

        return account.build()
    }

    override fun toDto(entity: Account?): AccountDTO? {
        if (entity == null) {
            return null
        }

        var accountNumber: Long? = null
        var accountType: String? = null
        var branchAddress: String? = null

        accountNumber = entity.getAccountNumber()
        accountType = entity.getAccountType()
        branchAddress = entity.getBranchAddress()

        val accountDTO = AccountDTO(accountNumber, accountType, branchAddress)

        return accountDTO
    }

    override fun toEntity(dtoList: MutableList<AccountDTO?>?): MutableList<Account?>? {
        if (dtoList == null) {
            return null
        }

        val list: MutableList<Account?> = ArrayList<Account?>(dtoList.size)
        for (accountDTO in dtoList) {
            list.add(toEntity(accountDTO))
        }

        return list
    }

    override fun toDto(entityList: MutableList<Account?>?): MutableList<AccountDTO?>? {
        if (entityList == null) {
            return null
        }

        val list: MutableList<AccountDTO?> = ArrayList<AccountDTO?>(entityList.size)
        for (account in entityList) {
            list.add(toDto(account))
        }

        return list
    }

    override fun partialUpdate(entity: Account?, dto: AccountDTO?) {
        if (dto == null) {
            return
        }

        if (dto.accountNumber != null) {
            entity?.setAccountNumber(dto.accountNumber)
        }
        if (dto.accountType != null) {
            entity?.setAccountType(dto.accountType)
        }
        if (dto.branchAddress != null) {
            entity?.setBranchAddress(dto.branchAddress)
        }
    }

    override fun fromMap(map: MutableMap<String?, Any?>?): AccountDTO? {
        if (map == null) {
            return null
        }

        var accountNumber: Long? = null
        var accountType: String? = null
        var branchAddress: String? = null

        accountNumber = AccountMappingUtil.accountNumber(map)
        accountType = AccountMappingUtil.accountType(map)
        branchAddress = AccountMappingUtil.branchAddress(map)

        val accountDTO = AccountDTO(accountNumber, accountType, branchAddress)

        return accountDTO
    }
}
