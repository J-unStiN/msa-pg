plugins {
//    kotlin("jvm") version "1.9.25"
//    kotlin("plugin.spring") version "1.9.25"
//    id("org.springframework.boot") version "3.4.3"
//    id("io.spring.dependency-management") version "1.1.7"
//    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.9.1"
}


dependencies {
//    implementation("org.springframework.boot:spring-boot-starter")
    implementation(project(":kafka:kafka-model"))
    implementation(project(":app-config-data"))

    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-avro-serializer:7.8.0")
//    implementation("io.confluent:kafka-avro-serializer:5.5.1")
}


tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
