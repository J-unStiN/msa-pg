package io.pg.demo.twitterservice.runner.impl

import io.pg.demo.appconfigdata.config.TwitterServiceConfigData
import io.pg.demo.twitterservice.exception.TwitterServiceException
import io.pg.demo.twitterservice.listener.TwitterKafkaStatusListener
import io.pg.demo.twitterservice.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.TwitterException
import twitter4j.TwitterObjectFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

@Component
@ConditionalOnProperty(name = ["twitter-service.enable-mock-tweets"], havingValue = "true")
class MockKafkaStreamRunner(
    private val twitterServiceConfigData: TwitterServiceConfigData,
    private val twitterKafkaStatusListener: TwitterKafkaStatusListener
) : StreamRunner {

    companion object {
        private val LOG = LoggerFactory.getLogger(MockKafkaStreamRunner::class.java)
        private val RANDOM : Random = Random.Default
        private val WORDS : List<String> = mutableListOf(
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetuer",
            "adipiscing",
            "elit",
            "Maecenas",
            "porttitor",
            "congue",
            "massa",
            "Fusce",
            "posuere",
            "magna",
            "sed",
            "pulvinar",
            "ultricies",
            "purus",
            "lectus",
            "malesuada",
            "libero"
        )

        private val tweetAsRawJson : String = """
            {
                "created_at":"{0}",
                "id":"{1}",
                "text":"{2}",
                "user":{"id":"{3}"}
            }
            """.trimIndent()

        private val TWITTER_STATUS_DATE_FORMAT : String = "EEE MMM dd HH:mm:ss zzz yyyy"
    }

    override fun start() {
        val keywords : Array<String> = twitterServiceConfigData.twitterKeywords.toTypedArray()
        val minTweetLength : Int = twitterServiceConfigData.mockMinTweetLength
        val maxTweetLength : Int = twitterServiceConfigData.mockMaxTweetLength
        val sleepTimeMs : Long = twitterServiceConfigData.mockSleepMs
        LOG.info("Mocking tweets for keywords: ${keywords}")


        simulateTwitterStream(keywords, minTweetLength, maxTweetLength, sleepTimeMs)
    }

    private fun simulateTwitterStream(
        keywords: Array<String>,
        minTweetLength: Int,
        maxTweetLength: Int,
        sleepTimeMs: Long
    ) {

        try {
            while (true) {
                val formattedTweetAsRawJson = getFormattedTweet(keywords, minTweetLength, maxTweetLength)
                val status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson)
                twitterKafkaStatusListener.onStatus(status)
                sleep(sleepTimeMs)
            }
        } catch (e: TwitterException) {
            LOG.error("Error creating tweet: ${e.message}")
        }
    }

    private fun sleep(sleepTimeMs: Long) {
        try {
            Thread.sleep(sleepTimeMs)
        } catch (e: InterruptedException) {
            LOG.error("Error sleeping: ${e.message}")
            throw TwitterServiceException("Thread sleep interrupted")
        }
    }

    private fun getFormattedTweet(
        keywords: Array<String>,
        minTweetLength: Int,
        maxTweetLength: Int
    ) : String {
        val params = arrayOf(
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
            ThreadLocalRandom.current().nextLong(Long.MAX_VALUE).toString(),
            getRandomTweetContent(keywords, minTweetLength, maxTweetLength),
            ThreadLocalRandom.current().nextLong(Long.MAX_VALUE).toString(),
        )
        return formatTweetAsJsonWithParams(params)
    }

    private fun formatTweetAsJsonWithParams(params: Array<String>): String {
        var tweet: String = tweetAsRawJson

        for (i in params.indices) {
            tweet = tweet.replace("{$i}", params[i])
        }

        return tweet
    }


    private fun getRandomTweetContent(
        keywords: Array<String>,
        minTweetLength: Int,
        maxTweetLength: Int
    ) : String {
        val tweet : StringBuilder = StringBuilder()
        val tweetLength = RANDOM.nextInt(maxTweetLength - minTweetLength + 1) + minTweetLength
        return constructRandomTweet(tweetLength, tweet, keywords)
    }

    private fun constructRandomTweet(
        tweetLength: Int,
        tweet: StringBuilder,
        keywords: Array<String>
    ): String {
        for (i in 0..tweetLength) {
            tweet.append(WORDS[RANDOM.nextInt(WORDS.size)]).append(" ")
            if (i == tweetLength / 2) {
                tweet.append(keywords[RANDOM.nextInt(keywords.size)]).append(" ")
            }
        }
        return tweet.toString().trim()
    }

}