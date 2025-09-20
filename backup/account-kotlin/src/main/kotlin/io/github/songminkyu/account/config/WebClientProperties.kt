package io.github.songminkyu.account.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.webclient")
data class WebClientProperties(
    var httpClient: HttpClient = HttpClient(),
    var oauth2: Oauth2 = Oauth2()
) {

    data class Oauth2(
        var clientRegistrationId: String? = null
    )

    data class HttpClient(
        var config: Config = Config()
    ) {

        data class Config(
            var connectTimeout: Int = WebClientDefaults.Config.CONNECT_TIMEOUT,
            var readTimeout: Int = WebClientDefaults.Config.READ_TIMEOUT,
            var writeTimeout: Int = WebClientDefaults.Config.WRITE_TIMEOUT
        )
    }
}