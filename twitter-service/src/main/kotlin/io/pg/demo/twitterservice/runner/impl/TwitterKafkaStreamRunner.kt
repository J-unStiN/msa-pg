package io.pg.demo.twitterservice.runner.impl

import io.pg.demo.appconfigdata.config.TwitterServiceConfigData
import io.pg.demo.twitterservice.TwitterServiceApplication
import io.pg.demo.twitterservice.listener.TwitterKafkaStatusListener
import io.pg.demo.twitterservice.runner.StreamRunner
import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.FilterQuery
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory

@Component
@ConditionalOnProperty(name = arrayOf("twitter-service.enable-mock-tweets"), havingValue = "false", matchIfMissing = true)
class TwitterKafkaStreamRunner(
    private val twitterServiceConfigData: TwitterServiceConfigData,
    private val twitterKafkaStatusListener: TwitterKafkaStatusListener
) : StreamRunner {

    private val LOG = LoggerFactory.getLogger(TwitterServiceApplication::class.java)
    lateinit var twitterStream : TwitterStream;

    override fun start() {
        twitterStream = TwitterStreamFactory().instance
        twitterStream.addListener(this.twitterKafkaStatusListener)
        addFilter()
    }

    @PreDestroy
    private fun shutdown() {
        LOG.info("Shutting down twitter stream !")
        twitterStream.shutdown()
    }

    private fun addFilter() {
        val keywords: Array<String> = twitterServiceConfigData.twitterKeywords.toTypedArray()
        val filterQuery: FilterQuery = FilterQuery(*keywords)
        twitterStream.filter(filterQuery)
        LOG.info("Twitter Kafka Stream keywords: ${keywords.contentToString()}")
    }
}