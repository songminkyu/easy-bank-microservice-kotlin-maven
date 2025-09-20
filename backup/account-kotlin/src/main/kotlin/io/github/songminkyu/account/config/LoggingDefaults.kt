package io.github.songminkyu.account.config

object LoggingDefaults {
    object Loki {
        const val ENABLED = false
        const val URL = "http://localhost:3100/loki/api/v1/push"
    }
}