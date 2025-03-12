package io.pg.demo.twitterservice.init.impl

import io.pg.demo.appconfigdata.config.KafkaConfigData
import io.pg.demo.kafka.admin.client.KafkaAdminClient
import io.pg.demo.twitterservice.init.StreamInitializer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
open class KafkaStreamInitializer(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaAdminClient: KafkaAdminClient
) : StreamInitializer {

    companion object {
        private val LOG = LoggerFactory.getLogger(KafkaStreamInitializer::class.java)
    }

    override fun init() {
        kafkaAdminClient.createTopics()
        kafkaAdminClient.checkSchemaRegistry()
        LOG.info("Kafka Stream initialized ${kafkaConfigData.topicNamesToCreate.toTypedArray()}")
    }
}