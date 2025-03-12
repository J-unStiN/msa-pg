package io.pg.demo.twitterservice


import io.pg.demo.twitterservice.init.StreamInitializer
import io.pg.demo.twitterservice.runner.StreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.pg.demo"])
class TwitterServiceApplication(
    private val streamRunner: StreamRunner,
    private val streamInitializer: StreamInitializer
) : CommandLineRunner {

    companion object {
        private val LOG = LoggerFactory.getLogger(TwitterServiceApplication::class.java)
    }


    override fun run(vararg args: String?) {
        streamInitializer.init()
        streamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterServiceApplication>(*args)
}
