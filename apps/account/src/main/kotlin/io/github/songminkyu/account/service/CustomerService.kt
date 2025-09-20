package io.github.songminkyu.account.service

import io.github.songminkyu.account.dto.CustomerDetailsDTO

interface CustomerService {
    fun fetchCustomerDetails(mobileNumber: String, correlationId: String): CustomerDetailsDTO
}