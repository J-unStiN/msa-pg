package io.pg.demo.twitterservice

import io.pg.demo.twitterservice.config.TwitterServiceConfigData
import io.pg.demo.twitterservice.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TwitterServiceApplication(
    private val twitterServiceConfigData: TwitterServiceConfigData,
    private val streamRunner: StreamRunner
) : CommandLineRunner {

    private val LOG = LoggerFactory.getLogger(TwitterServiceApplication::class.java)

    override fun run(vararg args: String?) {

        twitterServiceConfigData.twitterKeywords
            .forEach { keyword ->
                LOG.info("Twitter keyword: {}", keyword)
            }
        println("Hello, Twitter Service! ${twitterServiceConfigData.welcomeMessage}")
        streamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterServiceApplication>(*args)
}
