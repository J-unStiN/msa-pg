package io.pg.demo.kafka.admin.client

import io.pg.demo.appconfigdata.config.KafkaConfigData
import io.pg.demo.appconfigdata.config.RetryConfigData
import io.pg.demo.kafka.admin.exception.KafkaClientException
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.admin.TopicListing
import org.apache.kafka.common.internals.Topic
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.retry.RetryContext
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient

@Component
class KafkaAdminClient(
    private val kafkaConfigData: KafkaConfigData,
    private val retryConfigData : RetryConfigData,
    private val adminClient: AdminClient,
    private val retryTemplate: RetryTemplate,
    private val webClient: WebClient
) {

    companion object {
        private val LOG = LoggerFactory.getLogger(KafkaAdminClient::class.java)
    }

    fun createTopics() {
        try {
            val createTopicsResult: CreateTopicsResult = retryTemplate.execute<CreateTopicsResult, Throwable>(this::doCreatedTopics)
        } catch (t: Throwable) {
            throw KafkaClientException("Reached max nuber of retry for creating kafka topic ! {$t}")
        }

        checkTopicsCreated()
    }

    fun checkTopicsCreated() {
        var topics : Collection<TopicListing> = getTopics()
        var retryCount : Int = 1;
        val maxRetry : Int = retryConfigData.maxAttempts
        val multiplier : Int = retryConfigData.multiplier.toInt()
        var sleepTimeMs : Long = retryConfigData.sleepTimeMs

        for (topic : String in kafkaConfigData.topicNamesToCreate) {
            while (!isTopicCreated(topics, topic)) {
                checkMaxRetry(retryCount++, maxRetry)
                sleep(sleepTimeMs)
                sleepTimeMs *= multiplier
                topics = getTopics()
            }
        }
    }

    fun checkSchemaRegistry() {
        var retryCount : Int = 1
        var maxRetry : Int = retryConfigData.maxAttempts
        var multiplier : Int = retryConfigData.multiplier.toInt()
        var sleepTimeMs : Long = retryConfigData.sleepTimeMs
        while(getSchemaRegistryStatus()?.is2xxSuccessful == false) {
            checkMaxRetry(retryCount++, maxRetry)
            sleep(sleepTimeMs)
            sleepTimeMs *= multiplier
        }
    }

    private fun getSchemaRegistryStatus() : HttpStatusCode? {
        try {
            return webClient
                .method(HttpMethod.GET)
                .uri(kafkaConfigData.schemaRegistryUrl)
                .exchange()
                .map(ClientResponse::statusCode)
                .block()
        } catch (e: Exception) {
            return HttpStatus.SERVICE_UNAVAILABLE
        }
    }

    private fun sleep(sleepTimeMs: Long) {
        try {
            Thread.sleep(sleepTimeMs)
        } catch (t: InterruptedException) {
            throw KafkaClientException("Error while sleeping ! ${t}")
        }
    }

    private fun checkMaxRetry(retry: Int, maxRetry: Int) {
        if (retry > maxRetry) {
            throw KafkaClientException("Reached max number of retry for creating kafka topic !")
        }
    }

    private fun isTopicCreated(topics: Collection<TopicListing>, topicName: String) : Boolean {
        if (topics.isEmpty()) {
            return false
        }
        return topics.any{ it.name().equals(topicName) }
    }

    private fun doCreatedTopics(retryContext: RetryContext) : CreateTopicsResult {
        val topicNames : List<String> = kafkaConfigData.topicNamesToCreate
        LOG.info("Creating topic names: ${topicNames.size}, attempt ${retryContext.retryCount}")
        val kafkaTopics : List<NewTopic> = topicNames.map { it ->
            NewTopic(it.trim(), kafkaConfigData.numOffPartitions, kafkaConfigData.replicationFactor)
        }

        return adminClient.createTopics(kafkaTopics)
    }

    private fun getTopics() : Collection<TopicListing> {
        val topics : Collection<TopicListing>

        try {
            topics = retryTemplate.execute<Collection<TopicListing>, Throwable>(this::doGetTopics)
        } catch (t: Throwable) {
            throw KafkaClientException("Error while fetching topics from kafka ! ${t}")
        }
        return topics
    }

    private fun doGetTopics(retryContext: RetryContext) : Collection<TopicListing> {
        LOG.info("Reading kafka topic ${kafkaConfigData.topicNamesToCreate.toTypedArray()}, attempt ${retryContext.retryCount}")
        val topics : Collection<TopicListing> = adminClient.listTopics().listings().get()

        topics.let {
            topics.forEach {
                LOG.info("Topic name: ${it.name()}")
            }
        }

        return topics
    }


}