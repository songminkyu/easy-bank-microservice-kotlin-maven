package io.github.songminkyu.account.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.github.songminkyu.account.audit.AuditingRevisionListener
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table
import org.hibernate.envers.RevisionNumber
import org.hibernate.envers.RevisionTimestamp

@Entity
@Table(name = "revision_info")
@org.hibernate.envers.RevisionEntity(AuditingRevisionListener::class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class RevisionEntity(
    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceGenerator")
    @SequenceGenerator(name = "SequenceGenerator", sequenceName = "seq_revision_info", allocationSize = 1)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    @RevisionTimestamp
    @Column(name = "timestamp", nullable = false)
    var timestamp: Long = 0,

    @Column(name = "username", nullable = false, length = 20)
    var username: String? = null
)