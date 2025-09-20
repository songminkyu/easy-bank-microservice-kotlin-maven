package io.github.songminkyu.account.entity

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
open class BaseEntity(
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    var createdAt: Instant? = null,

    @CreatedBy
    @Column(name = "created_by", nullable = false, updatable = false, length = 50)
    var createdBy: String? = null,

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    var updatedAt: Instant? = null,

    @LastModifiedBy
    @Column(name = "updated_by", insertable = false, length = 50)
    var updatedBy: String? = null
)