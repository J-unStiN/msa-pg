package io.pg.demo.twitterservice.runner.impl

import io.pg.demo.appconfigdata.config.TwitterServiceConfigData
import io.pg.demo.twitterservice.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.io.IOException
import java.net.URISyntaxException

//@Component
//@ConditionalOnProperty(name = ["twitter-service.enable-v2-tweets"], havingValue = "true", matchIfMissing = true)
//@ConditionalOnExpression("\${twitter-service.enable-v2-tweets} && !\${twitter-service.enable-mock-tweets}")
class TwitterV2KafkaStreamRunner(
    private val twitterServiceConfigData: TwitterServiceConfigData,
    private val twitterV2StreamHelper: TwitterV2StreamHelper
) : StreamRunner{

    companion object {
        val LOG = LoggerFactory.getLogger(TwitterKafkaStreamRunner::class.java)
    }

    override fun start() {
        val bearerToken : String = twitterServiceConfigData.twitterV2BearerToken
        require(bearerToken.isNotEmpty()) { "Bearer token is empty" }

        try {
            twitterV2StreamHelper.run {
                setupRules(bearerToken, getRules())
                connectStream(bearerToken)
            }
//            twitterV2StreamHelper.setupRules(bearerToken, getRules())
//            twitterV2StreamHelper.connectStream(bearerToken)
        } catch (e : IOException) {
            LOG.error("Error setting up Twitter V2 stream: ${e.message}")
        } catch (e : URISyntaxException) {
            LOG.error("Error setting up Twitter V2 stream: ${e.message}")
        }
    }


    private fun getRules(): Map<String, String> {
        val keywords : List<String> = twitterServiceConfigData.twitterKeywords
        var rules : MutableMap<String, String> = HashMap()
        keywords.forEach { keyword ->
            rules.put(keyword, "keyword: ${keyword}")
        }

        LOG.info("Created Twitter API keywords: $keywords")
        return rules
    }

}