package io.github.songminkyu.loan.controller

import com.github.loki4j.slf4j.marker.LabelMarker
import io.github.songminkyu.loan.constants.LoanConstants
import io.github.songminkyu.loan.dto.ErrorResponseDTO
import io.github.songminkyu.loan.dto.LoanContactInfoDTO
import io.github.songminkyu.loan.dto.LoanDTO
import io.github.songminkyu.loan.dto.ResponseDTO
import io.github.songminkyu.loan.service.LoanService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

private val logger = KotlinLogging.logger {}

@Tag(
    name = "loan",
    description = "CRUD REST APIs for Loans in EazyBank"
)
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
class LoanController(
    private val loanService: LoanService,
    private val environment: Environment,
    private val loanContactInfo: LoanContactInfoDTO,
    @Value("\${build.version}") private val buildVersion: String
) {

    @GetMapping("/test")
    fun test(): String {
        return "test"
    }
    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Create Loan REST API",
        description = "REST API to create new loan inside EazyBank"
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
    @PostMapping("/loan")
    fun createLoan(
        @RequestParam
        @Pattern(
            regexp = "(^$|\\d{10})",
            message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}"
        )
        mobileNumber: String
    ): ResponseEntity<ResponseDTO> {
        loanService.createLoan(mobileNumber)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ResponseDTO(LoanConstants.STATUS_201, LoanConstants.MESSAGE_201))
    }

    @Operation(
        summary = "Fetch Loan Details REST API",
        description = "REST API to fetch loan details based on a mobile number"
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
    @GetMapping("/loan")
    fun fetchLoanDetails(
        @RequestHeader("X-Correlation-Id") correlationId: String,
        @RequestParam
        @Pattern(
            regexp = "(^$|\\d{10})",
            message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}"
        )
        mobileNumber: String
    ): ResponseEntity<LoanDTO> {
        val marker = LabelMarker.of("mobileNumber") { mobileNumber }
        logger.info { "loan successfully fetched" }
        logger.debug { "fetchLoanDetails method start" }
        val model = loanService.fetchLoan(mobileNumber)
        logger.debug { "fetchLoanDetails method end" }
        return ResponseEntity.ok(model)
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Update Loan Details REST API",
        description = "REST API to update loan details based on a loan number"
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
    @PutMapping("/loan/{loanNumber}")
    fun updateLoanDetails(
        @NotEmpty(message = "Loan Number can not be a null or empty")
        @Pattern(
            regexp = "(^$|\\d{12})",
            message = "{jakarta.validation.constraint.LoanNumber.Pattern.message}"
        )
        @PathVariable loanNumber: String,
        @Valid @RequestBody loan: LoanDTO
    ): ResponseEntity<ResponseDTO> {
        val isUpdated = loanService.updateLoan(loanNumber, loan)
        return if (isUpdated) {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO(LoanConstants.STATUS_200, LoanConstants.MESSAGE_200))
        } else {
            ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(ResponseDTO(LoanConstants.STATUS_417, LoanConstants.MESSAGE_417_UPDATE))
        }
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Delete Loan Details REST API",
        description = "REST API to delete Loan details based on a mobile number"
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
    @DeleteMapping("/loan")
    fun deleteLoanDetails(
        @RequestParam
        @Pattern(
            regexp = "(^$|\\d{10})",
            message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}"
        )
        mobileNumber: String
    ): ResponseEntity<Void> {
        loanService.deleteLoan(mobileNumber)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Get Build information",
        description = "Get Build information that is deployed into loan microservice"
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
    @GetMapping("/build-info")
    fun getBuildInfo(): ResponseEntity<String> {
        return ResponseEntity.ok(buildVersion)
    }

    @Operation(
        summary = "Get Java version",
        description = "Get Java versions details that is installed into loan microservice"
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
    @GetMapping("/java-version")
    fun getJavaVersion(): ResponseEntity<String> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(environment.getProperty("JAVA_HOME"))
    }

    @Operation(
        summary = "Get Contact Info",
        description = "Contact Info details that can be reached out in case of any issues"
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
    @GetMapping("/contact-info")
    fun getContactInfo(): ResponseEntity<LoanContactInfoDTO> {
        logger.debug { "Invoked Loans contact-info API" }
        return ResponseEntity.ok(loanContactInfo)
    }
}