package io.github.songminkyu.account.controller

import io.github.songminkyu.account.dto.CustomerDetailsDTO
import io.github.songminkyu.account.dto.ErrorResponseDTO
import io.github.songminkyu.account.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.constraints.Pattern
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@Tag(
    name = "REST API for Customer in EazyBank",
    description = "REST APIs in EazyBank to FETCH customer.csv details"
)
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
class CustomerController(
    private val customerService: CustomerService
) {

    @Operation(
        summary = "Fetch Customer Details REST API",
        description = "REST API to fetch Customer details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Ok"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(
            schema = Schema(implementation = ErrorResponseDTO::class)
        )]
    )
    @GetMapping("/customer-details")
    fun fetchCustomerDetails(
        @RequestHeader("X-Correlation-Id") correlationId: String,
        @RequestParam
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        mobileNumber: String
    ): ResponseEntity<CustomerDetailsDTO> {
        logger.debug { "fetchCustomerDetails method start" }
        val model = customerService.fetchCustomerDetails(mobileNumber, correlationId)
        logger.debug { "fetchCustomerDetails method end" }
        return ResponseEntity.ok(model)
    }
}