package io.pg.demo.twitterservice.transformer

import io.pg.demo.kafka.avro.model.TwitterAvroModel
import org.springframework.stereotype.Component
import twitter4j.Status

@Component
class TwitterStatusToAvroTransformer {

    fun getTwitterAvroModelFromStatus(status: Status) : TwitterAvroModel {
        return TwitterAvroModel
            .newBuilder()
            .setId(status.id)
            .setUserId(status.user.id)
            .setText(status.text)
            .setCreatedAt(status.createdAt.time)
            .build()
    }
}