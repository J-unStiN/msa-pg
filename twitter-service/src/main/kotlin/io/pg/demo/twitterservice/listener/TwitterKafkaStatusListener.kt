package io.pg.demo.twitterservice.listener

import io.pg.demo.appconfigdata.config.KafkaConfigData
import io.pg.demo.kafka.avro.model.TwitterAvroModel
import io.pg.demo.kafka.producer.service.KafkaProducer
import io.pg.demo.twitterservice.TwitterServiceApplication
import io.pg.demo.twitterservice.transformer.TwitterStatusToAvroTransformer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
class TwitterKafkaStatusListener(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaProducer: KafkaProducer<Long, TwitterAvroModel>,
    private val twitterStatusToAvroTransformer: TwitterStatusToAvroTransformer,
) : StatusAdapter() {

    private val LOG = LoggerFactory.getLogger(TwitterServiceApplication::class.java)


    override fun onStatus(status: Status?) {
        status?.run {
            LOG.info("Twitter Status : ${status.text}, Twitter topic : ${kafkaConfigData.topicName}")
            val twitterAvroModel: TwitterAvroModel = twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status)
            kafkaProducer.send(kafkaConfigData.topicName, twitterAvroModel.userId, twitterAvroModel)
        }
    }
}