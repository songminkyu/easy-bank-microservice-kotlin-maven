package io.github.songminkyu.account.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.songminkyu.account.audit.AuditingRevisionListener
import jakarta.persistence.*
import org.hibernate.envers.RevisionEntity
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp

@Entity
@Table(name = "revision_info")
@RevisionEntity(AuditingRevisionListener::class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class RevisionEntity {
    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "seq_revision_info", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private var id: Long? = null

    @RevisionTimestamp
    @Column(name = "timestamp", nullable = false)
    private var timestamp: Long = 0

    @Column(name = "username", nullable = false, length = 20)
    private var username: String? = null

    fun getId(): Long? = id
    fun setId(id: Long?) { this.id = id }

    fun getTimestamp(): Long = timestamp
    fun setTimestamp(timestamp: Long) { this.timestamp = timestamp }

    fun getUsername(): String? = username
    fun setUsername(username: String?) { this.username = username }
}