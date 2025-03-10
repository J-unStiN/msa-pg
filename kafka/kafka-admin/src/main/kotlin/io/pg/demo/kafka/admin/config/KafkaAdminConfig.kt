package io.pg.demo.kafka.admin.config

import io.pg.demo.appconfigdata.config.KafkaConfigData
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@Configuration
class KafkaAdminConfig(
    private val kafkaConfigData: KafkaConfigData
) {


    @Bean
    fun adminClient(): AdminClient {
        return AdminClient.create(
            mapOf(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers)
        )
    }




}