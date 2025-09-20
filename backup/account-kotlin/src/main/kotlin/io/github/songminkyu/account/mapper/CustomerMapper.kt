package io.github.songminkyu.account.mapper

import io.github.songminkyu.account.dto.CardDTO
import io.github.songminkyu.account.dto.CustomerDTO
import io.github.songminkyu.account.dto.CustomerDetailsDTO
import io.github.songminkyu.account.dto.LoanDTO
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.entity.Customer
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface CustomerMapper {

    fun toEntity(dto: CustomerDTO): Customer
    fun toDto(entity: Customer): CustomerDTO
    fun toEntity(dtoList: List<CustomerDTO>): List<Customer>
    fun toDto(entityList: List<Customer>): List<CustomerDTO>

    @org.mapstruct.Named("partialUpdate")
    @org.mapstruct.BeanMapping(nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
    fun partialUpdate(@org.mapstruct.MappingTarget entity: Customer, dto: CustomerDTO)

    fun toDto(customer: Customer, account: Account): CustomerDTO

    @Mapping(source = "customer.mobileNumber", target = "mobileNumber")
    fun toCustomerDetailsDTO(
        customer: Customer,
        account: Account,
        loan: LoanDTO?,
        card: CardDTO?
    ): CustomerDetailsDTO
}