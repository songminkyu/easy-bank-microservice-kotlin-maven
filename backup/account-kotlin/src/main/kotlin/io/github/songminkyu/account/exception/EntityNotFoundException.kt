package io.github.songminkyu.account.exception

import org.apache.commons.lang.StringUtils

class EntityNotFoundException(clazz: Class<*>, vararg searchParamsMap: String) : RuntimeException(
    generateMessage(clazz.simpleName, toMap(*searchParamsMap))
) {
    companion object {
        private fun generateMessage(entity: String, searchParams: Map<String, String>): String {
            return "${StringUtils.capitalize(entity)} was not found for parameters $searchParams"
        }

        private fun toMap(vararg entries: String): Map<String, String> {
            if (entries.size % 2 == 1) {
                throw IllegalArgumentException("Invalid entries")
            }
            return entries.indices.step(2).associate { i ->
                entries[i] to entries[i + 1]
            }
        }
    }
}