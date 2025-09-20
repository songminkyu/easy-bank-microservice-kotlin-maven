package io.github.songminkyu.account.functions

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.songminkyu.account.debezium.data.DebeziumEventDetails
import io.github.songminkyu.account.debezium.data.Operation
import io.github.songminkyu.account.entity.Account
import io.github.songminkyu.account.service.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class AccountsFunctions {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun updateCommunication(accountsService: AccountService): Consumer<Long> {
        return Consumer { accountNumber ->
            logger.info("Updating Communication status for the account number : $accountNumber")
            accountsService.updateCommunicationStatus(accountNumber)
        }
    }

    @Bean
    fun processAccountDebeziumEvent(): Consumer<DebeziumEventDetails<Account>> {
        return Consumer { debeziumEvent ->
            val payload = debeziumEvent.payload
            when (payload.operation) {
                Operation.CREATE ->
                    logger.info("created account on debezium event: ${payload.after}")
                Operation.UPDATE ->
                    logger.info("updated account on debezium event: ${payload.after}")
                Operation.DELETE ->
                    logger.info("deleted account on debezium event: ${payload.before}")
            }
        }
    }
}