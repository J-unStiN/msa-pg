package io.pg.demo.kafka.producer.service.impl

import io.pg.demo.kafka.avro.model.TwitterAvroModel
import io.pg.demo.kafka.producer.service.KafkaProducer
import jakarta.annotation.PreDestroy
import org.glassfish.jersey.internal.guava.ListenableFuture
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.concurrent.CompletableFuture

class TwitterKafkaProducer(
    private val kafkaTemplate : KafkaTemplate<Long, TwitterAvroModel>
) : KafkaProducer<Long, TwitterAvroModel> {

    companion object {
        private val Log = LoggerFactory.getLogger(TwitterKafkaProducer::class.java)
    }

    @PreDestroy
    fun close() {
        if (kafkaTemplate != null) {
            Log.info("Closing Kafka Producer")
            kafkaTemplate.destroy()
        }
    }

    override fun send(topicName: String, key: Long, message: TwitterAvroModel) {
        Log.info("sending message: ${message} to topic ${topicName}")
        val kafkaResultFuture: CompletableFuture<SendResult<Long, TwitterAvroModel>> = kafkaTemplate.send(topicName, key, message)
        kafkaResultFuture.whenComplete { result, exception ->
            when {
                exception != null -> {
                    Log.error("Error sending message: ${message} to topic ${topicName}", exception)
                }
                result != null -> {
                    val metadata = result.recordMetadata
                    Log.info(
                        "Message sent successfully: ${message}" +
                        "to topic ${metadata.topic()} " +
                        "to Partition ${metadata.partition()} " +
                        "to Offset ${metadata.offset()} " +
                        "to Timestamp ${metadata.timestamp()} " +
                        "at time ${System.nanoTime()} "
                    )
                }
            }
        }

    }
}