package io.pg.demo.kafka.producer.service

import org.apache.avro.specific.SpecificRecordBase
import java.io.Serializable

interface KafkaProducer <K : Serializable, V : SpecificRecordBase> {
    fun send(topicName : String, key : K, message : V)
}