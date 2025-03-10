package io.pg.demo.common.config

import io.pg.demo.appconfigdata.config.RetryConfigData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate


@Configuration
class RetryConfig(
    private val retryConfigData: RetryConfigData
) {


    @Bean
    fun retryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()

        val exponentialBackOffPolicy = ExponentialBackOffPolicy()
        exponentialBackOffPolicy.initialInterval = retryConfigData.initialIntervalMs
        exponentialBackOffPolicy.maxInterval = retryConfigData.maxIntervalMs
        exponentialBackOffPolicy.multiplier = retryConfigData.multiplier

        val simpleRetryPolicy = SimpleRetryPolicy()
        simpleRetryPolicy.maxAttempts = retryConfigData.maxAttempts

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy)
        retryTemplate.setRetryPolicy(simpleRetryPolicy)

        return retryTemplate
    }


}