package io.github.songminkyu.account.audit

import io.github.songminkyu.account.entity.RevisionEntity
import org.hibernate.envers.RevisionListener

class AuditingRevisionListener : RevisionListener {

    override fun newRevision(revisionEntity: Any) {
        val revEntity = revisionEntity as RevisionEntity
        val userName = "ACCOUNTS_MS"
        revEntity.username = userName
    }
}