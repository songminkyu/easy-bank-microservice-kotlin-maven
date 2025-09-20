package io.github.songminkyu.loan.entity

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
abstract class BaseEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: Instant? = null

    @CreatedBy
    @Column(nullable = false, updatable = false, length = 50)
    var createdBy: String? = null

    @LastModifiedDate
    @Column(insertable = false)
    var updatedAt: Instant? = null

    @LastModifiedBy
    @Column(insertable = false, length = 50)
    var updatedBy: String? = null
}