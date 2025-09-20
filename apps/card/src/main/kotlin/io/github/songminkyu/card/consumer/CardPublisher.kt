package io.github.songminkyu.card.consumer

import io.github.songminkyu.card.debezium.data.DebeziumEventDetails
import io.github.songminkyu.card.debezium.data.Operation
import io.github.songminkyu.card.debezium.data.Payload
import io.github.songminkyu.card.dto.CardDTO
import io.github.songminkyu.card.entity.Card
import io.github.songminkyu.card.mapper.CardMapper
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.observables.ConnectableObservable
import jakarta.persistence.EntityManagerFactory
import jakarta.persistence.PersistenceUnit
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer

@Configuration
class CardPublisher(private val cardMapper: CardMapper) {

    private val log = LoggerFactory.getLogger(CardPublisher::class.java)

    var publisher: Flowable<CardDTO> = Flowable.empty()
    private var emitter: ObservableEmitter<CardDTO>? = null

    @PersistenceUnit
    private var emf: EntityManagerFactory? = null

    init {
        val ratingObservable = Observable.create<CardDTO> { emitter ->
            this.emitter = emitter
        }
        val connectableObservable: ConnectableObservable<CardDTO> = ratingObservable.share().publish()
        connectableObservable.connect()
        this.publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER)
    }

    @Bean
    fun processCardDebeziumEvent(): Consumer<DebeziumEventDetails<Card>> {
        return Consumer { debeziumEvent ->
            emitter?.let { emitter ->
                val payload: Payload<Card> = debeziumEvent.payload
                val entity: Card = when (payload.operation) {
                    Operation.DELETE -> payload.before
                    else -> payload.after
                }
                val dto = cardMapper.toDto(entity)
                emitter.onNext(dto)
                if (payload.operation == Operation.DELETE || payload.operation == Operation.UPDATE) {
                    log.info(
                        "Evicting item {} from 2nd-level cache as TX was not started by this application",
                        entity.cardId
                    )
                    emf?.cache?.evict(Card::class.java, entity.cardId)
                }
            }
        }
    }
}