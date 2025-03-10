package io.pg.demo.appconfigdata.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import kotlin.properties.Delegates

@Configuration
@ConfigurationProperties(prefix = "kafka-config")
class KafkaConfigData {
    lateinit var bootstrapServers: String
    lateinit var schemaRegistryUrlKey: String
    lateinit var schemaRegistryUrl: String
    lateinit var topicName: String
    lateinit var topicNamesToCreate: List<String>
    var numOffPartitions by Delegates.notNull<Int>()
    var replicationFactor by Delegates.notNull<Short>()
}