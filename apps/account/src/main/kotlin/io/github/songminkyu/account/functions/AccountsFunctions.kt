package io.github.songminkyu.account.functions

import io.github.songminkyu.account.debezium.data.DebeziumEventDetails
import io.github.songminkyu.account.debezium.data.Operation
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.service.AccountService
import lombok.extern.slf4j.Slf4j
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

@Configuration
@Slf4j
class AccountsFunctions {

    @Bean
    fun updateCommunication(accountsService: AccountService): Consumer<Long?> {
        return Consumer { accountNumber: Long? ->
            logger.info { "Updating Communication status for the account number : " + accountNumber.toString() }
            accountsService.updateCommunicationStatus(accountNumber)
        }
    }

    @Bean
    fun processAccountDebeziumEvent(): Consumer<DebeziumEventDetails<Account?>?> {
        return Consumer { debeziumEvent: DebeziumEventDetails<Account?>? ->
            val payload = debeziumEvent!!.payload
            when (payload.operation) {
                Operation.CREATE -> logger.info { "created account on debezium event: " + payload.after }
                Operation.UPDATE -> logger.info { "updated account on debezium event: " + payload.after }
                Operation.DELETE -> logger.info { "deleted account on debezium event: " + payload.before }
            }
        }
    }
}