package io.pg.demo.appconfigdata.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates


@Configuration
@ConfigurationProperties(prefix = "twitter-service")
class TwitterServiceConfigData {
    lateinit var twitterKeywords: List<String>
    lateinit var welcomeMessage: String
    var enableMockTweets by Delegates.notNull<Boolean>()
    var mockSleepMs by Delegates.notNull<Long>()
    var mockMinTweetLength by Delegates.notNull<Int>()
    var mockMaxTweetLength by Delegates.notNull<Int>()

    lateinit var twitterV2BaseUrl: String
    lateinit var twitterV2RulesBaseUrl: String
    lateinit var twitterV2BearerToken : String


}