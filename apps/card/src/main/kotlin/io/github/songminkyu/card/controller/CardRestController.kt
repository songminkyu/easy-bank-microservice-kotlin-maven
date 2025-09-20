package io.github.songminkyu.card.controller

import com.github.loki4j.slf4j.marker.LabelMarker
import io.github.songminkyu.card.constants.CardConstants
import io.github.songminkyu.card.dto.CardContactInfoDTO
import io.github.songminkyu.card.dto.CardDTO
import io.github.songminkyu.card.dto.ErrorResponseDTO
import io.github.songminkyu.card.dto.ResponseDTO
import io.github.songminkyu.card.service.CardService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@Tag(
    name = "card",
    description = "CRUD REST APIs for Cards in EazyBank"
)
@RestController
@RequestMapping(path = ["/api"], produces = [MediaType.APPLICATION_JSON_VALUE])
@Validated
class CardRestController(
    private val cardService: CardService,
    private val environment: Environment,
    private val cardContactInfo: CardContactInfoDTO
) {

    private val log = LoggerFactory.getLogger(CardRestController::class.java)

    @Value("\${build.version}")
    private lateinit var buildVersion: String

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Create Card REST API",
        description = "REST API to create new Card inside EazyBank"
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
    @PostMapping("/card")
    fun createCard(
        @RequestParam
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        mobileNumber: String
    ): ResponseEntity<CardDTO> {
        val cardDTO = cardService.createCard(mobileNumber)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(cardDTO)
    }

    @Operation(
        summary = "Fetch Card Details REST API",
        description = "REST API to fetch card details based on a mobile number"
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
    @GetMapping("/card")
    fun fetchCardDetails(
        @RequestHeader("X-Correlation-Id") correlationId: String,
        @RequestParam
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        mobileNumber: String
    ): ResponseEntity<CardDTO> {
        val marker = LabelMarker.of("mobileNumber") { mobileNumber }
        log.info(marker, "card successfully fetched")
        log.debug("fetchCardDetails method start")
        val model = cardService.getCard(mobileNumber)
        log.debug("fetchCardDetails method end")
        return ResponseEntity.ok(model)
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Update Card Details REST API",
        description = "REST API to update card details based on a card number"
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
    @PutMapping("/card/{cardNumber}")
    fun updateCardDetails(
        @NotEmpty(message = "Card Number can not be a null or empty")
        @Pattern(regexp = "(^$|\\d{12})", message = "{jakarta.validation.constraint.CardNumber.Pattern.message}")
        @PathVariable cardNumber: String,
        @Valid @RequestBody card: CardDTO
    ): ResponseEntity<ResponseDTO> {
        val isUpdated = cardService.updateCard(cardNumber, card)
        return if (isUpdated) {
            ResponseEntity
                .status(HttpStatus.OK)
                .body(ResponseDTO(CardConstants.STATUS_200, CardConstants.MESSAGE_200))
        } else {
            ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(ResponseDTO(CardConstants.STATUS_417, CardConstants.MESSAGE_417_UPDATE))
        }
    }

    @SecurityRequirement(name = "auth")
    @Operation(
        summary = "Delete Card Details REST API",
        description = "REST API to delete Card details based on a mobile number"
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
    @DeleteMapping("/card")
    fun deleteCardDetails(
        @RequestParam
        @Pattern(regexp = "(^$|\\d{10})", message = "{jakarta.validation.constraint.MobileNumber.Pattern.message}")
        mobileNumber: String
    ): ResponseEntity<Void> {
        cardService.deleteCard(mobileNumber)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Get Build information",
        description = "Get Build information that is deployed into card microservice"
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
        description = "Get Java versions details that is installed into card microservice"
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
    fun getContactInfo(): ResponseEntity<CardContactInfoDTO> {
        return ResponseEntity.ok(cardContactInfo)
    }
}