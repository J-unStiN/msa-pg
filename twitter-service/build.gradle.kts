plugins {
    id("org.springframework.boot") version "3.4.3"
//    id("com.github.davidmc24.gradle.plugin.avro-base") version "1.9.1"

}

dependencies {
    implementation(project(":app-config-data"))
    implementation(project(":kafka:kafka-admin"))
    implementation(project(":kafka:kafka-model"))
    implementation(project(":kafka:kafka-producer"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.twitter4j:twitter4j-stream:4.0.7")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.json:json:20231013")

//    implementation("org.apache.avro:avro:1.12.0")
//    implementation("io.confluent:kafka-avro-serializer:5.5.1")
}
