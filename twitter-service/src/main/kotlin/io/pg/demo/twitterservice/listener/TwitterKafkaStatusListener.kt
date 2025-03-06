package io.pg.demo.twitterservice.listener

import io.pg.demo.twitterservice.TwitterServiceApplication
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
class TwitterKafkaStatusListener : StatusAdapter() {

    private val LOG = LoggerFactory.getLogger(TwitterServiceApplication::class.java)

    override fun onStatus(status: Status?) {
        status?.run {
            LOG.info("Twitter Status : ${status.text}")
        }
    }
}