package io.pg.demo.twitterservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@ConfigurationProperties(prefix = "twitter-service")
class TwitterServiceConfigData {
    lateinit var twitterKeywords: List<String>
    lateinit var welcomeMessage: String
}