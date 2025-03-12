package io.pg.demo.appconfigdata.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = "retry-config")
class RetryConfigData {

    var initialIntervalMs by Delegates.notNull<Long>()
    var maxIntervalMs by Delegates.notNull<Long>()
    var multiplier by Delegates.notNull<Double>()
    var maxAttempts by Delegates.notNull<Int>()
    var sleepTimeMs by Delegates.notNull<Long>()
}