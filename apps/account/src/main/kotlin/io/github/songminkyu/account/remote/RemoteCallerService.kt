
package io.github.songminkyu.account.remote

import org.springframework.stereotype.Service

@Service
class RemoteCallerService(
    @RemoteEndpoint
    private val remoteService: RemoteService
) {
    fun doSomething() {
        remoteService.doSomething()
    }
}