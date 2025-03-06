package io.pg.demo.twitterservice

import io.pg.demo.twitterservice.config.TwitterServiceConfigData
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TwitterServiceApplication(
    private val twitterServiceConfigData: TwitterServiceConfigData
) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(TwitterServiceApplication::class.java)

    override fun run(vararg args: String?) {

        twitterServiceConfigData.twitterKeywords
            .forEach { keyword ->
                logger.info("Twitter keyword: {}", keyword)
            }
        println("Hello, Twitter Service! ${twitterServiceConfigData.welcomeMessage}")
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterServiceApplication>(*args)
}
