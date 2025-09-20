package io.github.songminkyu.account.controller

import com.github.loki4j.slf4j.marker.LabelMarker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import io.github.resilience4j.retry.annotation.Retry
import io.github.songminkyu.account.constants.AccountConstants
import io.github.songminkyu.account.dto.AccountContactInfoDTO
import io.github.songminkyu.account.dto.AccountDTO
import io.github.songminkyu.account.dto.CustomerDTO
import io.github.songminkyu.account.dto.ErrorResponseDTO
import io.github.songminkyu.account.dto.ResponseDTO
import io.github.songminkyu.account.service.AccountService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.concurrent.TimeoutException

private val logger = KotlinLogging.logger {}

@Tag(
    name = "account",
    description = "CRUD REST APIs for Accounts in EazyBank"
)
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
class AccountController(
    private val accountService: AccountService,
    private val environment: Environment,
    private val accountContactInfo: AccountContactInfoDTO
) {

    @Value("\${build.version}")
    private lateinit var buildVersion: String

    @GetMapping("/test")
    fun test(): String {
        accountService.test()
        return "test"
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Create Account REST API",
        description = "REST API to create new Customer &  Account inside EazyBank"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Created"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(
            schema = Schema(implementation = ErrorResponseDTO::class)
        )]
    )
    @PostMapping("/account")
    fun createAccount(@Valid @RequestBody customer: CustomerDTO): ResponseEntity<ResponseDTO> {
        return accountService.createAccount(customer)
            .thenApply {
                ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ResponseDTO(AccountConstants.STATUS_201, AccountConstants.MESSAGE_201))
            }
            .join()
    }

    @Operation(
        summary = "Fetch Account Details REST API",
        description = "REST API to fetch Customer &  Account details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Ok"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Not Found"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(
            schema = Schema(implementation = ErrorResponseDTO::class)
        )]
    )
    @GetMapping("/account")
    fun fetchAccountDetails(
        @RequestParam
        @NotBlank
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        mobileNumber: String
    ): ResponseEntity<CustomerDTO> {
        val marker = LabelMarker.of("mobileNumber") { mobileNumber }
        log.info(marker) { "account successfully fetched" }
        val model = accountService.fetchAccount(mobileNumber)
        return ResponseEntity.ok(model)
    }

    @Operation(
        summary = "Fetch Account Revisions REST API",
        description = "REST API to fetch Account revisions based on a account number"
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
    @GetMapping("/account/{accountNumber:\\d+}/revisions")
    fun getAccountRevisions(
        @NotBlank
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        @PathVariable accountNumber: Long
    ): ResponseEntity<List<AccountDTO>> {
        val modelList = accountService.getAccountRevisions(accountNumber)
        return ResponseEntity.ok(modelList)
    }

    @Operation(
        summary = "Fetch Account Creator REST API",
        description = "REST API to fetch Account creator based on a account number"
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
    @GetMapping("/account/{accountNumber:\\d+}/creator")
    fun getCreatorUsername(
        @NotBlank
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        @PathVariable accountNumber: Long
    ): ResponseEntity<String> {
        val model = accountService.geCreatorUsername(accountNumber)
        return ResponseEntity.ok(model)
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Update Account Details REST API",
        description = "REST API to update Customer &  Account details based on a account number"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Ok"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Not Found"
    )
    @ApiResponse(
        responseCode = "417",
        description = "Expectation Failed"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(
            schema = Schema(implementation = ErrorResponseDTO::class)
        )]
    )
    @PutMapping("/account/{accountNumber:^$|\\d{10}}")
    fun updateAccountDetails(
        @NotNull
        @PathVariable accountNumber: Long,
        @Valid @RequestBody customer: CustomerDTO
    ): ResponseEntity<ResponseDTO> {
        val isUpdated = accountService.updateAccount(accountNumber, customer)
        return if (isUpdated) {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO(AccountConstants.STATUS_200, AccountConstants.MESSAGE_200))
        } else {
            ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(ResponseDTO(AccountConstants.STATUS_417, AccountConstants.MESSAGE_417_UPDATE))
        }
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Delete Account & Customer Details REST API",
        description = "REST API to delete Customer &  Account details based on a mobile number"
    )
    @ApiResponse(
        responseCode = "204",
        description = "No content"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Bad Request"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Not Found"
    )
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(
            schema = Schema(implementation = ErrorResponseDTO::class)
        )]
    )
    @DeleteMapping("/account")
    fun deleteAccountDetails(
        @RequestParam
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        mobileNumber: String
    ): ResponseEntity<Void> {
        accountService.deleteAccount(mobileNumber)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Get Build information",
        description = "Get Build information that is deployed into account microservice"
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
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    @Throws(TimeoutException::class)
    fun getBuildInfo(): ResponseEntity<String> {
        log.debug { "getBuildInfo() method Invoked" }
        return ResponseEntity.ok(buildVersion)
    }

    fun getBuildInfoFallback(): ResponseEntity<String> {
        log.debug { "getBuildInfoFallback() method Invoked" }
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("0.9")
    }

    @Operation(
        summary = "Get Java version",
        description = "Get Java versions details that is installed into account microservice"
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
    @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    fun getJavaVersion(): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(environment.getProperty("JAVA_HOME"))
    }

    fun getJavaVersionFallback(): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("Java 17")
    }

    @Operation(
        summary = "Get Contact Info",
        description = "Contact Info details that can be reached out in case of any issues"
    )
    @ApiResponse(responseCode = "200", description = "Ok")
    @ApiResponse(
        responseCode = "500",
        description = "Internal Server Error",
        content = [Content(
            schema = Schema(implementation = ErrorResponseDTO::class)
        )]
    )
    @GetMapping("/contact-info")
    fun getContactInfo(): ResponseEntity<AccountContactInfoDTO> {
        return ResponseEntity.ok(accountContactInfo)
    }
}