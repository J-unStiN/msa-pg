package io.pg.demo.kafka.admin.client

import io.pg.demo.appconfigdata.config.KafkaConfigData
import io.pg.demo.appconfigdata.config.RetryConfigData
import org.apache.kafka.clients.admin.AdminClient
import org.slf4j.LoggerFactory
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAdminClient(
    private val kafkaConfigData: KafkaConfigData,
    private val retryConfigData : RetryConfigData,
    private val adminClient: AdminClient,
    private val retryTemplate: RetryTemplate
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(KafkaAdminClient::class.java)
    }


}